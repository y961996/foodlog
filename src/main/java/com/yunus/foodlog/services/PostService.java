package com.yunus.foodlog.services;

import com.yunus.foodlog.entities.Post;
import com.yunus.foodlog.entities.User;
import com.yunus.foodlog.repositories.PostRepository;
import com.yunus.foodlog.requests.PostCreateRequest;
import com.yunus.foodlog.requests.PostUpdateRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    public List<Post> getAllPosts(Optional<Long> userId) {
        if(userId.isPresent()) {
            return postRepository.findByUserId(userId.get());
        }
        return postRepository.findAll();
    }

    public Post getOnePostById(Long postId) {
        return postRepository.findById(postId).orElse(null);
    }

    public Post createOnePost(PostCreateRequest newPostRequest) {
        User user = userService.getOneUserById(newPostRequest.getUserId());

        if(user == null)
            return null;

        Post toSave = new Post();
        toSave.setId(newPostRequest.getId());
        toSave.setText(newPostRequest.getText());
        toSave.setTitle(newPostRequest.getTitle());
        toSave.setUser(user);

        return postRepository.save(toSave);
    }

    public Post updateOnePostById(Long postId, PostUpdateRequest postUpdateRequest) {
        Optional<Post> post = postRepository.findById(postId);
        if(post.isPresent()) {
            Post toUpdate = post.get();
            toUpdate.setText(postUpdateRequest.getText());
            toUpdate.setTitle(postUpdateRequest.getTitle());
            toUpdate.setImagePaths(postUpdateRequest.getImagePaths());
            toUpdate.setShortVideoPath(postUpdateRequest.getShortVideoPath());
            postRepository.save(toUpdate);
            return toUpdate;
        }
        return null;
    }

    public void deleteOnePostById(Long postId) {
        postRepository.deleteById(postId);
    }
}
