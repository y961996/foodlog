package com.yunus.foodlog.controllers;

import com.yunus.foodlog.entities.Like;
import com.yunus.foodlog.requests.LikeCreateRequest;
import com.yunus.foodlog.responses.LikeResponse;
import com.yunus.foodlog.services.LikeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/likes")
@AllArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @GetMapping
    public List<LikeResponse> getAllLikes(@RequestParam Optional<Long> userId, @RequestParam Optional<Long> postId) {
        return likeService.getAllLikes(userId, postId);
    }

    @GetMapping(path = "{likeId}")
    public Like getLikeById(@PathVariable("likeId") Long likeId) {
        return likeService.getOneLikeById(likeId);
    }

    @PostMapping
    public Like createLike(@RequestBody LikeCreateRequest likeCreateRequest) {
        return likeService.createOneLike(likeCreateRequest);
    }

    @DeleteMapping(path = "{likeId}")
    public void deleteLikeById(@PathVariable("likeId") Long likeId) {
        likeService.deleteOneLikeById(likeId);
    }
}
