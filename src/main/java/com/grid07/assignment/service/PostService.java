package com.grid07.assignment.service;

import com.grid07.assignment.entity.Comment;
import com.grid07.assignment.entity.Post;
import com.grid07.assignment.repository.CommentRepository;
import com.grid07.assignment.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final RedisService redisService;

    public PostService(PostRepository postRepository,
                       CommentRepository commentRepository,
                       RedisService redisService) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.redisService = redisService;
    }

    // Create Post
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    // Add Comment
    public Comment addComment(Long postId, Comment comment, boolean isBot,
                               Long botId, Long humanId) {
        // Vertical Cap - max depth 20
        if (comment.getDepthLevel() > 20) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Comment depth exceeds maximum level of 20");
        }

        if (isBot) {
            // Horizontal Cap - max 100 bot replies
            if (!redisService.incrementBotCount(postId)) {
                throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                        "Bot reply limit of 100 reached for this post");
            }

            // Cooldown Cap
            if (!redisService.checkCooldown(botId, humanId)) {
                throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                        "Bot cooldown active, try after 10 minutes");
            }

            // Update virality
            redisService.updateViralityScore(postId, "BOT_REPLY");

            // Notification
            if (!redisService.checkNotificationCooldown(humanId)) {
                redisService.addPendingNotification(humanId,
                        "Bot replied to your post");
            } else {
                System.out.println("Push Notification Sent to User " + humanId);
            }
        } else {
            // Human comment
            redisService.updateViralityScore(postId, "HUMAN_COMMENT");
        }

        comment.setPostId(postId);
        return commentRepository.save(comment);
    }

    // Like Post
    public void likePost(Long postId, boolean isBot) {
        if (!isBot) {
            redisService.updateViralityScore(postId, "HUMAN_LIKE");
        }
        System.out.println("Post " + postId + " liked!");
    }
}