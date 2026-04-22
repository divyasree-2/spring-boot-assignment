package com.grid07.assignment.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;
import java.util.List;
import java.util.Set;

@Service
public class NotificationService {

    private final RedisService redisService;
    private final RedisTemplate<String, String> redisTemplate;

    public NotificationService(RedisService redisService,
                                RedisTemplate<String, String> redisTemplate) {
        this.redisService = redisService;
        this.redisTemplate = redisTemplate;
    }

    @Scheduled(fixedRate = 300000) // runs every 5 minutes
    public void sweepPendingNotifications() {
        Set<String> keys = redisTemplate.keys("user:*:pending_notifs");
        if (keys == null || keys.isEmpty()) return;

        for (String key : keys) {
            // Extract userId from key
            String userId = key.split(":")[1];
            Long userIdLong = Long.parseLong(userId);

            List<String> notifications = redisService
                    .getPendingNotifications(userIdLong);

            if (notifications != null && !notifications.isEmpty()) {
                int count = notifications.size();
                String first = notifications.get(0);
                System.out.println("Summarized Push Notification: " 
                        + first + " and [" + (count - 1) 
                        + "] others interacted with your posts.");
                redisService.clearPendingNotifications(userIdLong);
            }
        }
    }
}