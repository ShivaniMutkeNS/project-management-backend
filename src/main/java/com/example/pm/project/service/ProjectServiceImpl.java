package com.example.pm.project.service;

import com.example.pm.chat.exception.ChatException;
import com.example.pm.chat.model.Chat;
import com.example.pm.chat.model.ChatUserKey;
import com.example.pm.chat.repository.ChatUserRepository;
import com.example.pm.chat.service.ChatService;
import com.example.pm.project.exception.ProjectException;
import com.example.pm.project.model.Project;
import com.example.pm.project.model.ProjectUser;
import com.example.pm.project.model.ProjectUserKey;
import com.example.pm.project.repository.ProjectRepository;
import com.example.pm.project.repository.ProjectUserRepository;
import com.example.pm.user.exception.UserException;
import com.example.pm.user.model.User;
import com.example.pm.user.repository.UserRepository;
import com.example.pm.user.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectUserRepository projectUserRepository;

    @Autowired
    private ChatUserRepository chatUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Project createProject(Project project, User user) throws UserException {
        Project newProject = new Project();

        newProject.setOwner(user);
        newProject.setTags(project.getTags());
        newProject.setName(project.getName());
        newProject.setCategory(project.getCategory());
        newProject.setDescription(project.getDescription());

        Project saveProject = projectRepository.save(newProject);

        ProjectUser projectUser = new ProjectUser();
        ProjectUserKey projectUserKey = new ProjectUserKey(user.getId(), saveProject.getId());
        projectUser.setId(projectUserKey);
        projectUser.setUser(user);
        projectUser.setProject(saveProject);

        ProjectUser savedProjectUser = projectUserRepository.save(projectUser);

        List<ProjectUser> projectUserList = new ArrayList<>();
        projectUserList.add(savedProjectUser);

        saveProject.setTeamMembers(projectUserList);

        Chat chat = new Chat();
        chat.setProject(saveProject);
        Chat projectChat = chatService.saveChat(chat);

        saveProject.setChat(projectChat);

        userService.updateUsersProjectSize(user, 1);

        return saveProject;
    }

    @Override
    public List<Project> getProjectsByOwner(User owner) throws ProjectException {
        List<Project> projects = projectRepository.findByOwner(owner);

        return projects;
    }

    @Override
    public List<Project> getProjectsByTeam(User user, String category, String tag) throws ProjectException {
        return getProjects(user, category, tag, projectRepository);
    }

    static List<Project> getProjects(User user, String category, String tag, ProjectRepository projectRepository) {
        // Find projects by team members or owner
        List<Project> projects = projectRepository.findByOwnerOrTeamMember(user);

        if (category != null) {
            projects = projects.stream()
                    .filter(project -> project.getCategory().equals(category))
                    .collect(Collectors.toList());
        }

        if (tag != null) {
            projects = projects.stream()
                    .filter(project -> project.getTags().contains(tag))
                    .collect(Collectors.toList());
        }

        return projects;
    }


    @Override
    public Project getProjectById(Long projectId) throws ProjectException {
        Optional<Project> project = projectRepository.findById(projectId);
        if (project.isPresent()) {
            return project.get();
        }
        throw new ProjectException("No project exists with the id " + projectId);
    }

    @Override
    public String deleteProject(Long projectId, Long userId) throws UserException {
        User user = userService.findUserById(userId);
        System.out.println("user ____>" + user);
        if (user != null) {
            projectRepository.deleteById(projectId);
            return "project deleted";
        }
        throw new UserException("User doesnot exists");
    }

    @Override
    public Project updateProject(Project updatedProject, Long id) throws ProjectException {
        Project project = getProjectById(id);

        if (project != null) {
            // Update the existing project with the fields from updatedProject
            if (updatedProject.getName() != null) {
                project.setName(updatedProject.getName());
            }

            if (updatedProject.getDescription() != null) {
                project.setDescription(updatedProject.getDescription());
            }

            if (updatedProject.getTags() != null) {
                project.setTags(updatedProject.getTags());
            }

            // Save the updated project once
            return projectRepository.save(project);
        }

        throw new ProjectException("Project does not exist");

    }

    public List<Project> searchProjectsByName(String keyword, User user) throws ProjectException {

        List<Project> list = projectRepository.findByNameContainingAndTeamMembers_User(keyword, user);
        if (list != null) {
            return list;
        }

        throw new ProjectException("No Projects available");
    }

    @Transactional
    @Override
    public ResponseEntity<?> removeUserFromProject(Long projectId, Long userId) throws UserException, ProjectException {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectException("Project not found"));
        User user = userService.findUserById(userId);
        boolean found = false;
        for (ProjectUser projectUser : project.getTeamMembers()) {
            if (projectUser.getUser().getId().equals(userId)) {
                found = true;
                break;
            }
        }

        if (found) {
            project.getTeamMembers().removeIf(projectUser -> projectUser.getUser().getId().equals(userId));

            project.getChat().getParticipants().remove(user);

            projectRepository.save(project);

        } else {
            throw new UserException("user not found");
        }

        return null;
    }

    @Override
    public Chat getChatByProjectId(Long projectId) throws ProjectException, ChatException {
        Optional<Project> project = projectRepository.findById(projectId);

        if (project.isPresent()) return project.get().getChat();

        throw new ChatException("no chats found");
    }

    @Override
    public void addUserToProject(Long projectId, Long userId) throws UserException, ProjectException, ChatException {
        // Check if the project exists
        Optional<Project> optionalProject = projectRepository.findById(projectId);

        User user = userRepository.getById(userId);

        if (user == null) {
            throw new UserException("user not found");
        }

        if (optionalProject.isPresent()) {
            Project existingProject = optionalProject.get();
            if (!isUserInProject(user, existingProject)) {

                ProjectUser projectUser = new ProjectUser(user, existingProject);
                if (existingProject != null) {
                    ProjectUserKey projectUserKey = new ProjectUserKey(user.getId(), existingProject.getId());
                    projectUser.setId(projectUserKey);
                }
                projectUserRepository.save(projectUser);

                if (existingProject.getChat() != null) {
                    Chat chat = existingProject.getChat();
                    com.example.pm.chat.model.ChatUser chatUser = new com.example.pm.chat.model.ChatUser(chat, user);
                    if (chat != null) {
                        ChatUserKey chatUserKey = new ChatUserKey(user.getId(), chat.getId());
                        chatUser.setId(chatUserKey);
                    }
                    chatUserRepository.save(chatUser);
                    chatService.saveChat(chat);
                }
            } else {
                throw new UserException("USer is already there");
            }
        } else {
            throw new ProjectException("Project not found");
        }
    }

    @Override
    public List<User> getUsersByProjectId(Long projectId) throws ProjectException {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project != null) {
            List<ProjectUser> projectUsers = project.getTeamMembers();
            // Extracting users from ProjectUser objects
            List<User> users = projectUsers.stream()
                    .map(ProjectUser::getUser)
                    .collect(Collectors.toList());
            return users;
        }

        throw new ProjectException("no project found with id " + projectId);
    }

    //
//    @Override
//    public String deleteProject(Long projectId, Long id) throws UserException {
//        User user = userService.findUserById(id);
//        System.out.println("user ____>" + user);
//        if (user != null) {
//            projectRepository.deleteById(projectId);
//            return "project deleted";
//        }
//        throw new UserException("User doesnot exists");
//    }
//
//    @Override
//    public Project updateProject(Project updatedProject, Long id) throws ProjectException {
//        Project project = getProjectById(id);
//
//        if (project != null) {
//            // Update the existing project with the fields from updatedProject
//            if (updatedProject.getName() != null) {
//                project.setName(updatedProject.getName());
//            }
//
//            if (updatedProject.getDescription() != null) {
//                project.setDescription(updatedProject.getDescription());
//            }
//
//            if (updatedProject.getTags() != null) {
//                project.setTags(updatedProject.getTags());
//            }
//
//            // Save the updated project once
//            return projectRepository.save(project);
//        }
//
//        throw new ProjectException("Project does not exist");
//    }
//
//    @Override
//    public List<Project> searchProjects(String keyword, User user) throws ProjectException {
//        String partialName = "%" + keyword + "%";
////			projectRepository.findByPartialNameAndTeamIn(partialName, user);
//        List<Project> list = projectRepository.findByNameContainingAndTeamMembersContains(keyword, user);
//        if (list != null) {
//            return list;
//        }
//        throw new ProjectException("No Projects available");
//    }
//

    private boolean isUserInProject(User user, Project project) {
        return project.getTeamMembers().stream()
                .anyMatch(projectUser -> projectUser.getUser().equals(user));
    }
//
//    @Override
//    public void removeUserFromProject(Long projectId, Long userId) throws UserException, ProjectException {
//        Project project = projectRepository.findById(projectId)
//                .orElseThrow(() -> new ProjectException("project not found"));
//        User user = userService.findUserById(userId);
//
//        if (project.getTeamMembers().contains(user)) {
//            project.getTeamMembers().remove(user);
//            project.getChat().getParticipants().remove(user);
//        }
//
//    }
//
//    @Override
//    public Chat getChatByProjectId(Long projectId) throws ProjectException, ChatException {
//        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectException("Project not found"));
//        if (project != null) return project.getChat();
//
//
//        throw new ChatException("no chats found");
//
//    }
//
//    public List<ChatUser> getUsersByProjectId(Long projectId) throws ProjectException {
//        Project project = projectRepository.findById(projectId).orElse(null);
//        if (project != null) return project.getChat().getParticipants();
//
//        throw new ProjectException("no project found with id " + projectId);
////
//    }


}
