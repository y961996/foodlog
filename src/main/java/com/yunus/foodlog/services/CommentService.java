package com.yunus.foodlog.services;

import com.yunus.foodlog.entities.Comment;
import com.yunus.foodlog.entities.Post;
import com.yunus.foodlog.entities.User;
import com.yunus.foodlog.repositories.CommentRepository;
import com.yunus.foodlog.requests.CommentCreateRequest;
import com.yunus.foodlog.requests.CommentUpdateRequest;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PostService postService;

    public List<Comment> getAllComments(Optional<Long> userId, Optional<Long> postId) {
        log.info("CommentService -> getAllComments() called with userId: " + userId + ", postId: " + postId);
        if(userId.isPresent() && postId.isPresent()) {
            return commentRepository.findByUserIdAndPostId(userId.get(), postId.get());
        } else if(userId.isPresent()) {
            return commentRepository.findByUserId(userId.get());
        } else if(postId.isPresent()) {
            return commentRepository.findByPostId(postId.get());
        } else {
            return commentRepository.findAll();
        }
    }

    public Comment getOneCommentById(Long commentId) {
        log.info("CommentService -> getOneCommentById() called with commentId: " + commentId);
        return commentRepository.findById(commentId).orElse(null);
    }

    public Comment createOneComment(CommentCreateRequest commentCreateRequest) {
        log.info("CommentService -> createOneComment() called with commentCreateRequest: " + commentCreateRequest.toString());
        User user = userService.getOneUserById(commentCreateRequest.getUserId());
        Post post = postService.getOnePostById(commentCreateRequest.getPostId());
        if(user != null && post != null) {
            Comment commentToSave = new Comment();
            commentToSave.setId(commentCreateRequest.getId());
            commentToSave.setPost(post);
            commentToSave.setUser(user);
            commentToSave.setText(commentCreateRequest.getText());
            return commentRepository.save(commentToSave);
        } else {
            return null;
        }
    }

    public Comment updateOneCommentById(Long commentId, CommentUpdateRequest commentUpdateRequest) {
        log.info("CommentService -> updateOneCommentById() called with commentId: " + commentId + ", commentUpdateRequest: " + commentUpdateRequest.toString());
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isPresent()) {
            Comment commentToUpdate = comment.get();
            commentToUpdate.setText(commentUpdateRequest.getText());
            return commentRepository.save(commentToUpdate);
        } else {
            return null;
        }
    }

    public void deleteOneCommentById(Long commentId) {
        log.info("CommentService -> deleteOneCommentById() called with commentId: " + commentId);
        commentRepository.deleteById(commentId);
    }
}
