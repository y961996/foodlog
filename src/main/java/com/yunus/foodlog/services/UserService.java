package com.yunus.foodlog.services;

import com.yunus.foodlog.entities.User;
import com.yunus.foodlog.repositories.CommentRepository;
import com.yunus.foodlog.repositories.LikeRepository;
import com.yunus.foodlog.repositories.PostRepository;
import com.yunus.foodlog.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class UserService {

    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public List<User> getAllUsers() {
        log.info("UserService -> getAllUsers() called");
        return userRepository.findAll();
    }

    public User createOneUser(User newUser) {
        log.info("UserService -> createOneUser() called with newUser: " + newUser.toString());
        return userRepository.save(newUser);
    }

    public User getOneUserById(Long userId) {
        log.info("UserService -> getOneUserById() called with userId: " + userId);
        return userRepository.findById(userId).orElse(null);
    }

    public User updateOneUserById(Long userId, User newUser) {
        log.info("UserService -> updateOneUserById() called with userId: " + userId + ", newUser: " + newUser.toString());
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            User foundUser = user.get();
            // TODO: Temp control for nullness
            if (newUser.getUserName() != null) foundUser.setUserName(newUser.getUserName());
            if (newUser.getPassword() != null) foundUser.setPassword(newUser.getPassword());
            foundUser.setAvatar(newUser.getAvatar());
            userRepository.save(foundUser);
            return foundUser;
        } else {
            return null;
        }
    }

    public void deleteOneUserById(Long userId) {
        log.info("UserService -> deleteOneUserById() called with userId: " + userId);
        userRepository.deleteById(userId);
    }

    public User getOneUserByUserName(String userName) {
        log.info("UserService -> getOneUserByUserName() called with userName: " + userName);
        return userRepository.findByUserName(userName);
    }

    public Long getCount() {
        log.info("UserService -> getCount() called");
        return userRepository.count();
    }

    public List<Object> getUserActivity(Long userId) {
        List<Long> postIds = postRepository.findTopByUserId(userId);
        if (postIds.isEmpty())
            return null;
        List<Object> comments = commentRepository.findUserCommentsByPostId(postIds);
        List<Object> likes = likeRepository.findUserLikesByPostId(postIds);
        List<Object> result = new ArrayList<>();
        result.addAll(comments);
        result.addAll(likes);
        return result;
    }
}
