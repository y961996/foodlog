package com.yunus.foodlog.controllers;

import com.yunus.foodlog.entities.Comment;
import com.yunus.foodlog.requests.CommentCreateRequest;
import com.yunus.foodlog.requests.CommentUpdateRequest;
import com.yunus.foodlog.services.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comments")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public List<Comment> getAllComments(@RequestParam Optional<Long> userId, @RequestParam Optional<Long> postId) {
        return commentService.getAllComments(userId, postId);
    }

    @GetMapping(path = "{commentId}")
    public Comment getCommentById(@PathVariable("commentId") Long commentId) {
        return commentService.getOneCommentById(commentId);
    }

    @PostMapping
    public Comment createComment(@RequestBody CommentCreateRequest commentCreateRequest) {
        return commentService.createOneComment(commentCreateRequest);
    }

    @PutMapping(path = "{commentId}")
    public Comment updateCommentById(@PathVariable("commentId") Long commentId, @RequestBody CommentUpdateRequest commentUpdateRequest) {
        return commentService.updateOneCommentById(commentId, commentUpdateRequest);
    }

    @DeleteMapping(path = "{commentId}")
    public void deleteCommentById(@PathVariable("commentId") Long commentId) {
        commentService.deleteOneCommentById(commentId);
    }
}
