package com.yunus.foodlog.responses;

import com.yunus.foodlog.entities.User;
import lombok.Data;

@Data
public class UserResponse {

    private Long id;
    private int avatarId;
    private String userName;

    public UserResponse(User entity) {
        this.id = entity.getId();
        this.avatarId = entity.getAvatar();
        this.userName = entity.getUserName();
    }
}
