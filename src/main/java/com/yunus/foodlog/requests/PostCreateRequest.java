package com.yunus.foodlog.requests;

import lombok.Data;

import java.util.List;

@Data
public class PostCreateRequest {

    private String text;
    private String title;
    private Long userId;
    private List<String> imagePaths;
    private String shortVideoPath;
}
