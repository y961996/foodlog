package com.yunus.foodlog.services;

import com.yunus.foodlog.entities.Post;
import com.yunus.foodlog.entities.User;
import com.yunus.foodlog.repositories.PostRepository;
import com.yunus.foodlog.requests.PostCreateRequest;
import com.yunus.foodlog.requests.PostUpdateRequest;
import com.yunus.foodlog.responses.LikeResponse;
import com.yunus.foodlog.responses.PostResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final LikeService likeService;

    public PostService(PostRepository postRepository, UserService userService, @Lazy LikeService likeService) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.likeService = likeService;
    }

    public List<PostResponse> getAllPosts(Optional<Long> userId) {
        log.info("PostService -> getAllPosts() called with userId: " + userId);
        List<Post> list;
        if(userId.isPresent()) {
            list = postRepository.findByUserId(userId.get());
        } else {
            list = postRepository.findAll();
        }
        return list.stream().map(p -> {
            List<LikeResponse> likes = likeService.getAllLikes(Optional.empty(), Optional.of(p.getId()));
            return new PostResponse(p, likes);
        }).collect(Collectors.toList());
    }

    public Post getOnePostById(Long postId) {
        log.info("PostService -> getOnePostById() called with postId: " + postId);
        return postRepository.findById(postId).orElse(null);
    }

    public PostResponse getOnePostByIdWithLikes(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        List<LikeResponse> likes = likeService.getAllLikes(Optional.empty(), Optional.of(postId));
        return new PostResponse(post, likes);
    }

    public Post createOnePost(PostCreateRequest newPostRequest) {
        log.info("PostService -> createOnePost() called with newPostRequest: " + newPostRequest.toString());
        User user = userService.getOneUserById(newPostRequest.getUserId());

        if(user == null)
            return null;

        Post toSave = new Post();
        toSave.setText(newPostRequest.getText());
        toSave.setTitle(newPostRequest.getTitle());
        toSave.setUser(user);
        toSave.setImagePaths(newPostRequest.getImagePaths());
        toSave.setShortVideoPath(newPostRequest.getShortVideoPath());
        toSave.setCreateDate(new Date());

        return postRepository.save(toSave);
    }

    public Post updateOnePostById(Long postId, PostUpdateRequest postUpdateRequest) {
        log.info("PostService -> updateOnePostById() called with postUpdateRequest: " + postUpdateRequest.toString());
        Optional<Post> post = postRepository.findById(postId);
        if(post.isPresent()) {
            Post toUpdate = post.get();
            if(postUpdateRequest.getText() != null)
                if(!postUpdateRequest.getText().isBlank() || !postUpdateRequest.getText().isEmpty())
                    toUpdate.setText(postUpdateRequest.getText());
            if(postUpdateRequest.getTitle() != null)
                if(!postUpdateRequest.getTitle().isBlank() || !postUpdateRequest.getTitle().isEmpty())
                    toUpdate.setTitle(postUpdateRequest.getTitle());
            if(postUpdateRequest.getImagePaths() != null)
                if(postUpdateRequest.getImagePaths() != null && !postUpdateRequest.getImagePaths().isEmpty())
                    toUpdate.setImagePaths(postUpdateRequest.getImagePaths());
            if(postUpdateRequest.getShortVideoPath() != null)
                if(!postUpdateRequest.getShortVideoPath().isBlank() || !postUpdateRequest.getShortVideoPath().isEmpty())
                    toUpdate.setShortVideoPath(postUpdateRequest.getShortVideoPath());
            postRepository.save(toUpdate);
            return toUpdate;
        }
        return null;
    }

    public void deleteOnePostById(Long postId) {
        log.info("PostService -> deleteOnePostById() called with postId: " + postId);
        postRepository.deleteById(postId);
    }

    public Long getCount(){
        return postRepository.count();
    }
}
