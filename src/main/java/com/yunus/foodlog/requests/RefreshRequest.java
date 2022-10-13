package com.yunus.foodlog.requests;

import lombok.Data;

@Data
public class RefreshRequest {

    private Long userId;
    private String refreshToken;
}
