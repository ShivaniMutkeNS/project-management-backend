package com.example.pm.project.controller;

import com.example.pm.chat.exception.ChatException;
import com.example.pm.chat.model.Chat;
import com.example.pm.invitation.exception.MailsException;
import com.example.pm.invitation.model.Invitation;
import com.example.pm.invitation.service.InvitationService;
import com.example.pm.invitation.serviceimpl.InviteServiceImpl;
import com.example.pm.project.exception.ProjectException;
import com.example.pm.project.model.Project;
import com.example.pm.project.repository.ProjectRepository;
import com.example.pm.project.service.ProjectService;
import com.example.pm.res.request.ProjectInvitationRequest;
import com.example.pm.res.response.MessageResponse;
import com.example.pm.user.exception.UserException;
import com.example.pm.user.model.User;
import com.example.pm.user.service.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private InvitationService invitationService;


    @PostMapping
    public ResponseEntity<Project> createProject(
            @RequestBody Project project,
            @RequestHeader("Authorization") String token) throws UserException, ProjectException {
        User user = userService.findUserProfileByJwt(token);

        Project createdProject = projectService.createProject(project, user);

        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    //API for delete project
    @DeleteMapping("/{projectId}")
    public ResponseEntity<MessageResponse> deleteProject(@PathVariable Long projectId, @RequestHeader("Authorization") String token) throws UserException, ProjectException {
        User user = userService.findUserProfileByJwt(token);

        MessageResponse response = new MessageResponse(projectService.deleteProject(projectId, user.getId()));
        userService.updateUsersProjectSize(user, -1);
        return ResponseEntity.ok(response);
    }

    //api for update project
    @PutMapping("/{projectId}")
    public ResponseEntity<Project> updateProject(@RequestBody Project updatedProject, @PathVariable Long projectId, @RequestHeader("Authorization") String token) throws UserException, ProjectException {
        User user = userService.findUserProfileByJwt(token);
        Project updated = projectService.updateProject(updatedProject, projectId);
        return updated != null ?
                new ResponseEntity<>(updated, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    //api for get all projects
    @GetMapping("/all-projects")
    public ResponseEntity<List<Project>> getAllUsers() {
        List<Project> projects = projectRepository.findAll();

        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    //api for get all projects of specific user
    @GetMapping
    public ResponseEntity<List<Project>> getProjects(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String tag,
            @RequestHeader("Authorization") String token) throws ProjectException, UserException {
        User user = userService.findUserProfileByJwt(token);
        List<Project> projects = projectService.getProjectsByTeam(user, category, tag);

        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    //get project by id
    @GetMapping("/{projectId}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long projectId) throws ProjectException {
        Project project = projectService.getProjectById(projectId);
        return project != null ?
                new ResponseEntity<>(project, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    //search project
    @GetMapping("/search-by-name")
    public ResponseEntity<List<Project>> searchProjects(
            @RequestParam(required = false) String keyword,
            @RequestHeader("Authorization") String jwt
    ) throws ProjectException, UserException {
        User user = userService.findUserProfileByJwt(jwt);
        List<Project> projects = projectService.searchProjectsByName(keyword, user);
        return ResponseEntity.ok(projects);
    }


    //get chat by project id
    @GetMapping("/{projectId}/chat")
    public ResponseEntity<Chat> getChatByProjectId(@PathVariable Long projectId)
            throws ProjectException, ChatException {
        Chat chat = projectService.getChatByProjectId(projectId);
        return chat != null ? ResponseEntity.ok(chat) : ResponseEntity.notFound().build();
    }

    //add user to project
    @PostMapping("/{userId}/add-to-project/{projectId}")
    public ResponseEntity<MessageResponse> addUserToProject(
            @PathVariable Long userId,
            @PathVariable Long projectId) throws UserException, ProjectException, ChatException {
        projectService.addUserToProject(projectId, userId);
        MessageResponse response = new MessageResponse("User added to the project successfully");
        return ResponseEntity.ok(response);
    }

    //get project by owner
    @GetMapping("/user")
    public ResponseEntity<List<Project>> getProjectsByOwner(@RequestHeader("Authorization") String token) throws ProjectException {
        try {
            User owner = userService.findUserProfileByJwt(token);
            List<Project> projects = projectService.getProjectsByOwner(owner);
            return new ResponseEntity<>(projects, HttpStatus.OK);
        } catch (UserException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{projectId}/users")
    public ResponseEntity<List<User>> getUsersByProjectId(@PathVariable Long projectId) {
        try {
            List<User> projectUsers = projectService.getUsersByProjectId(projectId);
            return ResponseEntity.ok(projectUsers);
        } catch (ProjectException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @DeleteMapping("/{projectId}/users/{userId}")
    public ResponseEntity<?> removeUserFromProject(@PathVariable Long projectId, @PathVariable Long userId) throws UserException, ProjectException {
        return projectService.removeUserFromProject(projectId, userId);
    }

    //invite project


    @PostMapping("/invite")
    public ResponseEntity<MessageResponse> inviteToProject(
            @RequestBody ProjectInvitationRequest req) throws MailsException, MessagingException {

        invitationService.sendInvitation(req.getEmail(), req.getProjectId());

        MessageResponse res=new MessageResponse();
        res.setMessage("User invited to the project successfully");
        return ResponseEntity.ok(res);

    }

    @GetMapping("/accept_invitation")
    public ResponseEntity<Invitation> acceptInvitation(@RequestParam String token,
                                                       @RequestHeader("Authorization") String jwt) throws Exception {

        User user=userService.findUserProfileByJwt(jwt);

        Invitation invitation = invitationService.acceptInvitation(token,user.getId());
        projectService.addUserToProject(invitation.getProjectId(),user.getId());

        return new ResponseEntity<>(invitation,HttpStatus.ACCEPTED);
    }

}
