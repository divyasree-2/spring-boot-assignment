package com.grid07.assignment.controller;

import com.grid07.assignment.entity.Comment;
import com.grid07.assignment.entity.Post;
import com.grid07.assignment.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // Create Post
    @PostMapping("/posts")
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        return ResponseEntity.ok(postService.createPost(post));
    }

    // Add Comment
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Comment> addComment(
            @PathVariable Long postId,
            @RequestBody Comment comment,
            @RequestParam(defaultValue = "false") boolean isBot,
            @RequestParam(defaultValue = "0") Long botId,
            @RequestParam(defaultValue = "0") Long humanId) {
        return ResponseEntity.ok(
                postService.addComment(postId, comment, isBot, botId, humanId));
    }

    // Like Post
    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<String> likePost(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "false") boolean isBot) {
        postService.likePost(postId, isBot);
        return ResponseEntity.ok("Post liked successfully!");
    }
}