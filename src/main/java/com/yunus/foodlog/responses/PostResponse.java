package com.yunus.foodlog.responses;

import com.yunus.foodlog.entities.Post;
import lombok.Data;

import java.util.List;

@Data
public class PostResponse {

    private Long id;
    private Long userId;
    private String userName;
    private String title;
    private String text;
    private List<String> imagePaths;
    private String shortVideoPath;
    private List<LikeResponse> postLikes;

    public PostResponse(Post entity, List<LikeResponse> likes) {
        this.id = entity.getId();
        this.userId = entity.getUser().getId();
        this.userName = entity.getUser().getUserName();
        this.title = entity.getTitle();
        this.text = entity.getText();
        this.imagePaths = entity.getImagePaths();
        this.shortVideoPath = entity.getShortVideoPath();
        this.postLikes = likes;
    }
}
