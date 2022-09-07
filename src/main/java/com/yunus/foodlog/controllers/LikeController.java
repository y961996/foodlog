package com.yunus.foodlog.controllers;

import com.yunus.foodlog.entities.Like;
import com.yunus.foodlog.requests.LikeCreateRequest;
import com.yunus.foodlog.responses.LikeResponse;
import com.yunus.foodlog.services.LikeService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/likes")
@AllArgsConstructor
@Log4j2
public class LikeController {

    private final LikeService likeService;

    @GetMapping
    public List<LikeResponse> getAllLikes(@RequestParam Optional<Long> userId, @RequestParam Optional<Long> postId) {
        log.info("LikeController -> getAllLikes() called with userId: " + userId + " postId: " + postId);
        return likeService.getAllLikes(userId, postId);
    }

    @GetMapping(path = "{likeId}")
    public Like getLikeById(@PathVariable("likeId") Long likeId) {
        log.info("LikeController -> getLikeById() called with likeId: " + likeId);
        return likeService.getOneLikeById(likeId);
    }

    @PostMapping
    public Like createLike(@RequestBody LikeCreateRequest likeCreateRequest) {
        log.info("LikeController -> createLike() called with likeCreateRequest: " + likeCreateRequest.toString());
        return likeService.createOneLike(likeCreateRequest);
    }

    @DeleteMapping(path = "{likeId}")
    public void deleteLikeById(@PathVariable("likeId") Long likeId) {
        log.info("LikeController -> deleteLikeById() called with likeId: " + likeId);
        likeService.deleteOneLikeById(likeId);
    }
}
