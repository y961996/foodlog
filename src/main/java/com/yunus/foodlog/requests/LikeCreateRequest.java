package com.yunus.foodlog.requests;

import lombok.Data;

@Data
public class LikeCreateRequest {

    private Long userId;
    private Long postId;
}
