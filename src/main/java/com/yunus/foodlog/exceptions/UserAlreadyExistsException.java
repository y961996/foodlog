package com.yunus.foodlog.exceptions;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) { super(message); }
}
