package com.yunus.foodlog.controllers;

import com.yunus.foodlog.entities.Post;
import com.yunus.foodlog.requests.PostCreateRequest;
import com.yunus.foodlog.requests.PostUpdateRequest;
import com.yunus.foodlog.responses.PostResponse;
import com.yunus.foodlog.services.PostService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public List<PostResponse> getAllPosts(@RequestParam Optional<Long> userId) {
        return postService.getAllPosts(userId);
    }

    @GetMapping(path = "{postId}")
    public Post getPostById(@PathVariable("postId") Long postId) {
        return postService.getOnePostById(postId);
    }

    @PostMapping
    public Post createPost(@RequestBody PostCreateRequest newPostRequest) {
        return postService.createOnePost(newPostRequest);
    }

    @PutMapping(path = "{postId}")
    public Post updatePostById(@PathVariable("postId") Long postId, @RequestBody PostUpdateRequest postUpdateRequest) {
        return postService.updateOnePostById(postId, postUpdateRequest);
    }

    @DeleteMapping(path = "{postId}")
    public void deletePostById(@PathVariable("postId") Long postId) {
        postService.deleteOnePostById(postId);
    }
}
