package com.yunus.foodlog;

import com.yunus.foodlog.entities.Like;
import com.yunus.foodlog.entities.Post;
import com.yunus.foodlog.entities.User;
import com.yunus.foodlog.requests.CommentCreateRequest;
import com.yunus.foodlog.requests.LikeCreateRequest;
import com.yunus.foodlog.requests.PostCreateRequest;
import com.yunus.foodlog.services.CommentService;
import com.yunus.foodlog.services.LikeService;
import com.yunus.foodlog.services.PostService;
import com.yunus.foodlog.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class DataPopulator {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final PostService postService;
    private final CommentService commentService;
    private final LikeService likeService;
    private final Random random = new Random();

    public DataPopulator(UserService userService, PasswordEncoder passwordEncoder, PostService postService, CommentService commentService, LikeService likeService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.postService = postService;
        this.commentService = commentService;
        this.likeService = likeService;
    }

    private User getUserWithParam(String username, String password, int avatar) {
        User user = new User();
        user.setUserName(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setAvatar(avatar);
        return user;
    }

    public void createUser() {
        for (int i = 0; i < 6; i++)
            userService.createOneUser(getUserWithParam("test" + i, "test" + i, i));
        userService.createOneUser(getUserWithParam("user", "password", 2));
        userService.createOneUser(getUserWithParam("admin", "admin", 3));
    }

    private PostCreateRequest getPostWithParam(String title, String text) {
        PostCreateRequest postCreateRequest = new PostCreateRequest();

        User user = pickRandomUser();

        postCreateRequest.setTitle(title);
        postCreateRequest.setText(text);
        postCreateRequest.setUserId(user.getId());
        postCreateRequest.setImagePaths(null);
        postCreateRequest.setShortVideoPath("");

        return postCreateRequest;
    }

    public void createPost() {
        for (int i = 1; i <= 10; i++)
            postService.createOnePost(getPostWithParam("Title " + i, "Text" + i));
    }

    private CommentCreateRequest getCommentWithParam(String text) {
        CommentCreateRequest commentCreateRequest = new CommentCreateRequest();

        User user = pickRandomUser();
        Post post = pickRandomPost();

        commentCreateRequest.setUserId(user.getId());
        commentCreateRequest.setPostId(post.getId());
        commentCreateRequest.setText(text);

        return commentCreateRequest;
    }

    public void createComment() {
        for (int i = 1; i <= 15; i++)
            commentService.createOneComment(getCommentWithParam("This is a comment " + i));
    }

    private LikeCreateRequest getLikeWithParam() {
        LikeCreateRequest likeCreateRequest = new LikeCreateRequest();

        User user = pickRandomUser();
        Post post = pickRandomPost();

        List<Like> likes = likeService.getAllLikesByUserId(user.getId());
        for (Like like : likes) {
            if (post.getId().longValue() == like.getPost().getId().longValue())
                return null;
        }

        likeCreateRequest.setUserId(user.getId());
        likeCreateRequest.setPostId(post.getId());

        return likeCreateRequest;
    }

    public void createLike() {
        for (int i = 1; i <= 15; i++) {
            LikeCreateRequest likeCreateRequest = getLikeWithParam();
            if (likeCreateRequest != null)
                likeService.createOneLike(likeCreateRequest);
            else
                i = i > 0 ? i-1 : 0;
        }
    }

    private User pickRandomUser() {
        long userCount = userService.getCount();
        long pickedUserId = random.nextLong(userCount) + 1;
        return userService.getOneUserById(pickedUserId);
    }

    private Post pickRandomPost() {
        long postCount = postService.getCount();
        long pickedPostId = random.nextLong(postCount) + 1;
        return postService.getOnePostById(pickedPostId);
    }
}
