package com.example.pm.project.service;


import com.example.pm.chat.exception.ChatException;
import com.example.pm.chat.model.Chat;
import com.example.pm.project.exception.ProjectException;
import com.example.pm.project.model.Project;
import com.example.pm.user.exception.UserException;
import com.example.pm.user.model.User;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProjectService {
    Project createProject(Project project, User user) throws UserException;

    List<Project> getProjectsByOwner(User owner) throws ProjectException;

    List<Project> getProjectsByTeam(User user, String category, String tag) throws ProjectException;

    //
//
    Project getProjectById(Long projectId) throws ProjectException;

    //
    String deleteProject(Long projectId, Long userId) throws UserException;

    //
    Project updateProject(Project updatedProject, Long id) throws ProjectException;

    List<Project> searchProjectsByName(String keyword, User user) throws ProjectException;

    ResponseEntity<?> removeUserFromProject(Long projectId, Long userId) throws UserException, ProjectException;

    //
    Chat getChatByProjectId(Long projectId) throws ProjectException, ChatException;

    @Transactional
    void addUserToProject(Long projectId, Long userId) throws UserException, ProjectException, ChatException;

    List<User> getUsersByProjectId(Long projectId) throws ProjectException;

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

}
