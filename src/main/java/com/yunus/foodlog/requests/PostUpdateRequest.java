package com.yunus.foodlog.requests;

import lombok.Data;

import java.util.List;

@Data
public class PostUpdateRequest {

    private String title;
    private String text;
    private List<String> imagePaths;
    private String shortVideoPath;
}
