package com.example.pm.subscription.service;

import com.example.pm.subscription.PlanType;
import com.example.pm.subscription.model.Subscription;
import com.example.pm.user.model.User;

public interface SubscriptionService {
    Subscription createSubscription(User user);
    Subscription getUserSubscription(Long userId) throws Exception;
    Subscription upgradeSubscription(Long userId, PlanType planType);

    boolean isValid(Subscription subscription);
}
