package com.yunus.foodlog.requests;

import lombok.Data;

import java.util.List;

@Data
public class PostUpdateRequest {

    String title;
    String text;
    List<String> imagePaths;
    String shortVideoPath;
}
