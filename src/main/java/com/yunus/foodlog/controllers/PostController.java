package com.yunus.foodlog.controllers;

import com.yunus.foodlog.entities.Post;
import com.yunus.foodlog.requests.PostCreateRequest;
import com.yunus.foodlog.requests.PostUpdateRequest;
import com.yunus.foodlog.responses.PostResponse;
import com.yunus.foodlog.services.PostService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/posts")
@AllArgsConstructor
@Log4j2
public class PostController {

    private final PostService postService;

    @GetMapping
    public List<PostResponse> getAllPosts(@RequestParam Optional<Long> userId) {
        log.trace("PostController -> getAllPosts() called with userId: " + userId);
        return postService.getAllPosts(userId);
    }

    @GetMapping(path = "{postId}")
    public Post getPostById(@PathVariable("postId") Long postId) {
        log.trace("PostController -> getPostById() called with postId: " + postId);
        return postService.getOnePostById(postId);
    }

    @PostMapping
    public Post createPost(@RequestBody PostCreateRequest newPostRequest) {
        log.trace("PostController -> createPost() called with newPostRequest: " + newPostRequest.toString());
        return postService.createOnePost(newPostRequest);
    }

    @PutMapping(path = "{postId}")
    public Post updatePostById(@PathVariable("postId") Long postId, @RequestBody PostUpdateRequest postUpdateRequest) {
        log.trace("PostController -> updatePostById() called with postId: " + postId + ", postUpdateRequest: " + postUpdateRequest.toString());
        return postService.updateOnePostById(postId, postUpdateRequest);
    }

    @DeleteMapping(path = "{postId}")
    public void deletePostById(@PathVariable("postId") Long postId) {
        log.trace("PostController -> deletePostById() called with postId: " + postId);
        postService.deleteOnePostById(postId);
    }
}
