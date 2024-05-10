package com.example.pm.subscription.controller;

import com.example.pm.project.exception.ProjectException;
import com.example.pm.subscription.PlanType;
import com.example.pm.subscription.model.Subscription;
import com.example.pm.subscription.service.SubscriptionService;
import com.example.pm.user.exception.UserException;
import com.example.pm.user.model.User;
import com.example.pm.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private UserService userService;



    @GetMapping("/user")
    public ResponseEntity<Subscription> getUserSubscription(
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Subscription userSubscription = subscriptionService.getUserSubscription(user.getId());

        if (userSubscription != null) {
            return new ResponseEntity<>(userSubscription, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/upgrade")
    public ResponseEntity<Subscription> upgradeSubscription(@RequestHeader("Authorization") String jwt,
                                                            @RequestParam PlanType planType) throws UserException, ProjectException {
        User user = userService.findUserProfileByJwt(jwt);
        Subscription upgradedSubscription = subscriptionService.upgradeSubscription(user.getId(), planType);

        return new ResponseEntity<>(upgradedSubscription, HttpStatus.OK);

    }


}


