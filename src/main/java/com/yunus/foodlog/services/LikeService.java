package com.yunus.foodlog.services;

import com.yunus.foodlog.entities.Like;
import com.yunus.foodlog.entities.Post;
import com.yunus.foodlog.entities.User;
import com.yunus.foodlog.repositories.LikeRepository;
import com.yunus.foodlog.requests.LikeCreateRequest;
import com.yunus.foodlog.responses.LikeResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserService userService;
    private final PostService postService;

    public List<LikeResponse> getAllLikes(Optional<Long> userId, Optional<Long> postId) {
        log.info("LikeService -> getAllLikes() called with userId: " + userId + ", postId: " + postId);
        List<Like> list;
        if(userId.isPresent() && postId.isPresent()) {
            list = likeRepository.findByUserIdAndPostId(userId.get(), postId.get());
        } else if(userId.isPresent()) {
            list = likeRepository.findByUserId(userId.get());
        } else if(postId.isPresent()) {
            list = likeRepository.findByPostId(postId.get());
        } else {
            list = likeRepository.findAll();
        }
        return list.stream().map(LikeResponse::new).collect(Collectors.toList());
    }

    public Like getOneLikeById(Long likeId) {
        log.info("LikeService -> getOneLikeById() called with likeId: " + likeId);
        return likeRepository.findById(likeId).orElse(null);
    }

    public Like createOneLike(LikeCreateRequest likeCreateRequest) {
        log.info("LikeService -> createOneLike() called with likeCreateRequest: " + likeCreateRequest.toString());
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
        log.info("LikeService -> deleteOneLikeById() called with likeId: " + likeId);
        likeRepository.deleteById(likeId);
    }

    public List<Like> getAllLikesByUserId(Long userId){
        log.info("LikeService -> getAllLikesByUserId() called with userId: " + userId);
        return likeRepository.findByUserId(userId);
    }
}
