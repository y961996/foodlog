package com.yunus.foodlog.requests;

import lombok.Data;

@Data
public class LikeCreateRequest {

    Long userId;
    Long postId;
}
