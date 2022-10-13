package com.yunus.foodlog.requests;

import lombok.Data;

import java.util.List;

@Data
public class PostCreateRequest {

    String text;
    String title;
    Long userId;
    List<String> imagePaths;
    String shortVideoPath;
}
