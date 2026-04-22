package com.grid07.assignment.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Virality Score
    public void updateViralityScore(Long postId, String interactionType) {
        String key = "post:" + postId + ":virality_score";
        switch (interactionType) {
            case "BOT_REPLY" -> redisTemplate.opsForValue().increment(key, 1);
            case "HUMAN_LIKE" -> redisTemplate.opsForValue().increment(key, 20);
            case "HUMAN_COMMENT" -> redisTemplate.opsForValue().increment(key, 50);
        }
    }

    // Horizontal Cap - max 100 bot replies
    public boolean incrementBotCount(Long postId) {
        String key = "post:" + postId + ":bot_count";
        Long count = redisTemplate.opsForValue().increment(key);
        if (count > 100) {
            redisTemplate.opsForValue().decrement(key);
            return false;
        }
        return true;
    }

    // Cooldown Cap - bot cannot interact with same human within 10 mins
    public boolean checkCooldown(Long botId, Long humanId) {
        String key = "cooldown:bot_" + botId + ":human_" + humanId;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            return false;
        }
        redisTemplate.opsForValue().set(key, "1", 600, TimeUnit.SECONDS);
        return true;
    }

    // Notification cooldown - 15 mins
    public boolean checkNotificationCooldown(Long userId) {
        String key = "notif_cooldown:user_" + userId;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            return false;
        }
        redisTemplate.opsForValue().set(key, "1", 900, TimeUnit.SECONDS);
        return true;
    }

    // Push to pending notifications
    public void addPendingNotification(Long userId, String message) {
        String key = "user:" + userId + ":pending_notifs";
        redisTemplate.opsForList().rightPush(key, message);
    }

    // Get all pending notifications
    public java.util.List<String> getPendingNotifications(Long userId) {
        String key = "user:" + userId + ":pending_notifs";
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    // Clear pending notifications
    public void clearPendingNotifications(Long userId) {
        redisTemplate.delete("user:" + userId + ":pending_notifs");
    }
}