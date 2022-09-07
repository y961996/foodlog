package com.yunus.foodlog.services;

import com.yunus.foodlog.entities.User;
import com.yunus.foodlog.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class UserService {

    private final UserRepository userRepository;

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
        if(user.isPresent()) {
            User foundUser = user.get();
            foundUser.setUserName(newUser.getUserName());
            foundUser.setPassword(newUser.getPassword());
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
}
