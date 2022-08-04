package com.yunus.foodlog.controllers;

import com.yunus.foodlog.entities.User;
import com.yunus.foodlog.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(path = "{userId}")
    public User getUserById(@PathVariable("userId") Long userId) {
        return userService.getOneUserById(userId);
    }

    @PostMapping
    public User createUser(@RequestBody User newUser) {
        return userService.createOneUser(newUser);
    }

    @PutMapping(path = "{userId}")
    public User updateUserById(@PathVariable("userId") Long userId, @RequestBody User newUser) {
        return userService.updateOneUserById(userId, newUser);
    }

    @DeleteMapping(path = "{userId}")
    public void deleteUserById(@PathVariable("userId") Long userId) {
        userService.deleteOneUserById(userId);
    }
}
