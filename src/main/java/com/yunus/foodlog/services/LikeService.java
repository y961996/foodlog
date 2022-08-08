package com.yunus.foodlog.services;

import com.yunus.foodlog.entities.Like;
import com.yunus.foodlog.entities.Post;
import com.yunus.foodlog.entities.User;
import com.yunus.foodlog.repositories.LikeRepository;
import com.yunus.foodlog.requests.LikeCreateRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserService userService;
    private final PostService postService;

    public List<Like> getAllLikes(Optional<Long> userId, Optional<Long> postId) {
        if(userId.isPresent() && postId.isPresent()) {
            return likeRepository.findByUserIdAndPostId(userId.get(), postId.get());
        } else if(userId.isPresent()) {
            return likeRepository.findByUserId(userId.get());
        } else if(postId.isPresent()) {
            return likeRepository.findByPostId(postId.get());
        } else {
            return likeRepository.findAll();
        }
    }

    public Like getOneLikeById(Long likeId) {
        return likeRepository.findById(likeId).orElse(null);
    }

    public Like createOneLike(LikeCreateRequest likeCreateRequest) {
        User user = userService.getOneUserById(likeCreateRequest.getUserId());
        Post post = postService.getOnePostById(likeCreateRequest.getPostId());
        if(user != null && post != null) {
            Like likeToSave = new Like();
            likeToSave.setId(likeCreateRequest.getId());
            likeToSave.setPost(post);
            likeToSave.setUser(user);
            return likeRepository.save(likeToSave);
        } else {
            return null;
        }
    }

    public void deleteOneLikeById(Long likeId) {
        likeRepository.deleteById(likeId);
    }
}
