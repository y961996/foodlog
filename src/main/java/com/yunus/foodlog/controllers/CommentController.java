package com.yunus.foodlog.controllers;

import com.yunus.foodlog.entities.Comment;
import com.yunus.foodlog.requests.CommentCreateRequest;
import com.yunus.foodlog.requests.CommentUpdateRequest;
import com.yunus.foodlog.services.CommentService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/comments")
@AllArgsConstructor
@Log4j2
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public List<Comment> getAllComments(@RequestParam Optional<Long> userId, @RequestParam Optional<Long> postId) {
        log.info("CommentController -> getAllComments() called with userId: " + userId + ", postId: " + postId);
        return commentService.getAllComments(userId, postId);
    }

    @GetMapping(path = "{commentId}")
    public Comment getCommentById(@PathVariable("commentId") Long commentId) {
        log.info("CommentController -> getCommentById() called with commentId: " + commentId);
        return commentService.getOneCommentById(commentId);
    }

    @PostMapping
    public Comment createComment(@RequestBody CommentCreateRequest commentCreateRequest) {
        log.info("CommentController -> createComment() called with commentCreateRequest: " + commentCreateRequest.toString());
        return commentService.createOneComment(commentCreateRequest);
    }

    @PutMapping(path = "{commentId}")
    public Comment updateCommentById(@PathVariable("commentId") Long commentId, @RequestBody CommentUpdateRequest commentUpdateRequest) {
        log.info("CommentController -> updateCommentById() called with commentId: " + commentId + "commentUpdateRequest: " + commentUpdateRequest.toString());
        return commentService.updateOneCommentById(commentId, commentUpdateRequest);
    }

    @DeleteMapping(path = "{commentId}")
    public void deleteCommentById(@PathVariable("commentId") Long commentId) {
        log.info("CommentController -> deleteCommentById() called with commentId: " + commentId);
        commentService.deleteOneCommentById(commentId);
    }
}
