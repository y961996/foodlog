package com.yunus.foodlog.controllers;

import com.yunus.foodlog.entities.User;
import com.yunus.foodlog.exceptions.UserAlreadyExistsException;
import com.yunus.foodlog.exceptions.UserNotFoundException;
import com.yunus.foodlog.responses.UserResponse;
import com.yunus.foodlog.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
@Log4j2
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        log.info("UserController -> getAllUsers() called");
        return userService.getAllUsers();
    }

    @GetMapping(path = "{userId}")
    public UserResponse getUserById(@PathVariable("userId") Long userId) {
        log.info("UserController -> getUserById() called with userId: " + userId);
        User user = userService.getOneUserById(userId);
        if (user == null) {
            log.error("User with id: " + userId + " not found!");
            throw new UserNotFoundException("User with id: " + userId + " not found!");
        }
        return new UserResponse(userService.getOneUserById(userId));
    }

    @GetMapping("/activity/{userId}")
    public List<Object> getUserActivity(@PathVariable("userId") Long userId) {
        return userService.getUserActivity(userId);
    }

    @PostMapping
    public User createUser(@RequestBody User newUser) {
        log.info("UserController -> createUser() called with newUser: " + newUser.toString());
        return userService.createOneUser(newUser);
    }

    @PutMapping(path = "{userId}")
    public User updateUserById(@PathVariable("userId") Long userId, @RequestBody User newUser) {
        log.info("UserController -> updateUserById() called with userId: " + userId + ", newUser: " + newUser.toString());
        return userService.updateOneUserById(userId, newUser);
    }

    @DeleteMapping(path = "{userId}")
    public void deleteUserById(@PathVariable("userId") Long userId) {
        log.info("UserController -> deleteUserById() called with userId: " + userId);
        userService.deleteOneUserById(userId);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    //@ResponseBody => If wanted body can be returned. Maybe return the message here?
    private void handleUserNotFoundException() {

    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.SEE_OTHER)
    @ResponseBody
    private ResponseEntity<Object> handleUserAlreadyExistsException() {
        return new ResponseEntity<>("Username already exists!", new HttpHeaders(), HttpStatus.SEE_OTHER);
    }

    /*
    @ResponseBody
    @ResponseStatus(HttpStatus.SEE_OTHER)
    @ExceptionHandler(UserAlreadyExistsException.class)
    private Error handleUserAlreadyExistsException() {
        return new Error("Username already exists!");
    }
    */
}
