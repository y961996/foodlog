package com.yunus.foodlog.services;

import com.yunus.foodlog.entities.Like;
import com.yunus.foodlog.entities.Post;
import com.yunus.foodlog.entities.User;
import com.yunus.foodlog.exceptions.PostNotFoundException;
import com.yunus.foodlog.repositories.PostRepository;
import com.yunus.foodlog.requests.PostCreateRequest;
import com.yunus.foodlog.requests.PostUpdateRequest;
import com.yunus.foodlog.responses.LikeResponse;
import com.yunus.foodlog.responses.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;
    @Mock
    private UserService userService;
    @Mock
    private LikeService likeService;
    private PostService underTest;

    @BeforeEach
    void setUp() {
        underTest = new PostService(postRepository, userService, likeService);
    }

    @Test
    void shouldReturnAllPostsWhenUserIdIsNotGivenGetAllPosts() {
        // given
        Long userId = 72L;
        given(userService.getOneUserById(userId)).willReturn(
                new User(userId, "username", "password", 1)
        );

        Long userId2 = 73L;
        given(userService.getOneUserById(userId2)).willReturn(
                new User(userId2, "username2", "password2", 2)
        );

        User user = userService.getOneUserById(userId);
        User user2 = userService.getOneUserById(userId2);

        Post post1 = new Post();
        post1.setId(1L);
        post1.setUser(user);
        post1.setTitle("Post 1 Title");
        post1.setText("Post 1 Text");
        post1.setImagePaths(List.of());
        post1.setShortVideoPath("/post1Video.mp4");
        post1.setCreateDate(Date.from(Instant.now()));

        Post post2 = new Post();
        post2.setId(2L);
        post2.setUser(user);
        post2.setTitle("Post 2 Title");
        post2.setText("Post 2 Text");
        post2.setImagePaths(List.of());
        post2.setShortVideoPath("/post2Video.mp4");
        post2.setCreateDate(Date.from(Instant.now()));

        Post post3 = new Post();
        post3.setId(3L);
        post3.setUser(user2);
        post3.setTitle("Post 3 Title");
        post3.setText("Post 3 Text");
        post3.setImagePaths(List.of());
        post3.setShortVideoPath("/post3Video.mp4");
        post3.setCreateDate(Date.from(Instant.now()));

        List<Post> postRepositoryFindAllReturnList = List.of(
                post1,
                post2,
                post3
        );

        given(postRepository.findAll()).willReturn(
                postRepositoryFindAllReturnList
        );

        Like like1 = new Like();
        like1.setId(1L);
        like1.setUser(user);
        like1.setPost(post1);

        Like like2 = new Like();
        like2.setId(2L);
        like2.setUser(user);
        like2.setPost(post2);

        Like like3 = new Like();
        like3.setId(3L);
        like3.setUser(user2);
        like3.setPost(post2);

        Like like4 = new Like();
        like4.setId(4L);
        like4.setUser(user2);
        like4.setPost(post3);

        Like like5 = new Like();
        like5.setId(5L);
        like5.setUser(user2);
        like5.setPost(post1);

        List<Like> likesList = List.of(
                like1,
                like2,
                like3,
                like4,
                like5
        );

        List<LikeResponse> likeResponsesList = likesList.stream().map(LikeResponse::new).toList();

        given(likeService.getAllLikes(eq(Optional.empty()), any()))
                .willAnswer(invocationOnMock -> likesList
                        .stream()
                        .filter(like -> Objects.equals(
                                like.getPost().getId(),
                                ((Optional<?>) invocationOnMock.getArguments()[1]).orElseThrow())
                        )
                        .map(LikeResponse::new)
                        .collect(Collectors.toList())
                );

        // when
        List<PostResponse> allPosts = underTest.getAllPosts(Optional.empty());

        // then
        verify(postRepository).findAll();
        verify(likeService, times(postRepositoryFindAllReturnList.size())).getAllLikes(eq(Optional.empty()), any());
        assertThat(allPosts).isNotNull();
        assertThat(allPosts.size()).isGreaterThan(0);
        assertThat(allPosts.get(0).getUserId()).isEqualTo(post1.getUser().getId());
        assertThat(allPosts.get(1).getUserId()).isEqualTo(post2.getUser().getId());
        assertThat(allPosts.get(2).getUserId()).isEqualTo(post3.getUser().getId());
        assertThat(allPosts.get(0).getPostLikes()).isEqualTo(likeResponsesList.stream().filter(likeResponse -> Objects.equals(likeResponse.getPostId(), allPosts.get(0).getId())).collect(Collectors.toList()));
        assertThat(allPosts.get(1).getPostLikes()).isEqualTo(likeResponsesList.stream().filter(likeResponse -> Objects.equals(likeResponse.getPostId(), allPosts.get(1).getId())).collect(Collectors.toList()));
        assertThat(allPosts.get(2).getPostLikes()).isEqualTo(likeResponsesList.stream().filter(likeResponse -> Objects.equals(likeResponse.getPostId(), allPosts.get(2).getId())).collect(Collectors.toList()));
    }

    @Test
    void shouldReturnPostsByUserIdWhenUserIdIsGivenAndUserExistsGetAllPosts() {
        // given
        Long userId = 72L;
        given(userService.getOneUserById(userId)).willReturn(
                new User(userId, "username", "password", 1)
        );

        User user = userService.getOneUserById(userId);

        Post post1 = new Post();
        post1.setId(1L);
        post1.setUser(user);
        post1.setTitle("Post 1 Title");
        post1.setText("Post 1 Text");
        post1.setImagePaths(List.of());
        post1.setShortVideoPath("/post1Video.mp4");
        post1.setCreateDate(Date.from(Instant.now()));

        Post post2 = new Post();
        post2.setId(2L);
        post2.setUser(user);
        post2.setTitle("Post 2 Title");
        post2.setText("Post 2 Text");
        post2.setImagePaths(List.of());
        post2.setShortVideoPath("/post2Video.mp4");
        post2.setCreateDate(Date.from(Instant.now()));

        Post post3 = new Post();
        post3.setId(3L);
        post3.setUser(user);
        post3.setTitle("Post 3 Title");
        post3.setText("Post 3 Text");
        post3.setImagePaths(List.of());
        post3.setShortVideoPath("/post3Video.mp4");
        post3.setCreateDate(Date.from(Instant.now()));

        List<Post> postRepositoryFindAllReturnList = List.of(
                post1,
                post2,
                post3
        );

        given(postRepository.findByUserId(userId)).willReturn(
                postRepositoryFindAllReturnList
        );

        Like like1 = new Like();
        like1.setId(1L);
        like1.setUser(user);
        like1.setPost(post1);

        Like like2 = new Like();
        like2.setId(2L);
        like2.setUser(user);
        like2.setPost(post2);

        Like like3 = new Like();
        like3.setId(3L);
        like3.setUser(user);
        like3.setPost(post2);

        Like like4 = new Like();
        like4.setId(4L);
        like4.setUser(user);
        like4.setPost(post3);

        Like like5 = new Like();
        like5.setId(5L);
        like5.setUser(user);
        like5.setPost(post1);

        List<Like> likesList = List.of(
                like1,
                like2,
                like3,
                like4,
                like5
        );

        List<LikeResponse> likeResponsesList = likesList.stream().map(LikeResponse::new).toList();

        given(likeService.getAllLikes(eq(Optional.empty()), any()))
                .willAnswer(invocationOnMock -> likesList
                        .stream()
                        .filter(like -> Objects.equals(
                                like.getPost().getId(),
                                ((Optional<?>) invocationOnMock.getArguments()[1]).orElseThrow())
                        )
                        .map(LikeResponse::new)
                        .collect(Collectors.toList())
                );

        // when
        List<PostResponse> allPosts = underTest.getAllPosts(Optional.of(userId));

        // then
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);

        verify(postRepository).findByUserId(userIdCaptor.capture());
        verify(likeService, times(postRepositoryFindAllReturnList.size())).getAllLikes(eq(Optional.empty()), any());

        assertThat(userIdCaptor.getValue()).isEqualTo(userId);
        assertThat(allPosts).isNotNull();
        assertThat(allPosts.size()).isGreaterThan(0);
        assertThat(allPosts.get(0).getUserId()).isEqualTo(post1.getUser().getId());
        assertThat(allPosts.get(1).getUserId()).isEqualTo(post2.getUser().getId());
        assertThat(allPosts.get(2).getUserId()).isEqualTo(post3.getUser().getId());
        assertThat(allPosts.get(0).getPostLikes()).isEqualTo(likeResponsesList.stream().filter(likeResponse -> Objects.equals(likeResponse.getPostId(), allPosts.get(0).getId())).collect(Collectors.toList()));
        assertThat(allPosts.get(1).getPostLikes()).isEqualTo(likeResponsesList.stream().filter(likeResponse -> Objects.equals(likeResponse.getPostId(), allPosts.get(1).getId())).collect(Collectors.toList()));
        assertThat(allPosts.get(2).getPostLikes()).isEqualTo(likeResponsesList.stream().filter(likeResponse -> Objects.equals(likeResponse.getPostId(), allPosts.get(2).getId())).collect(Collectors.toList()));
    }

    @Test
    void shouldReturnEmptyListByUserIdWhenUserIdIsGivenAndUserDoesNotExistGetAllPosts() {
        // given
        Long userId = 72L;

        // when
        List<PostResponse> allPosts = underTest.getAllPosts(Optional.of(userId));

        // then
        verify(postRepository).findByUserId(userId);
        verify(likeService, never()).getAllLikes(eq(Optional.empty()), any());
        assertThat(allPosts.size()).isEqualTo(0);
    }

    @Test
    void shouldReturnPostWhenPostExistsByIdGetOnePostById() {
        // given
        Long postId = 72L;
        Long userId = 73L;

        Post post = new Post();
        post.setId(postId);
        post.setTitle("Post Title");
        post.setText("Post Text");
        post.setUser(new User(userId, "username", "password", 0));
        post.setImagePaths(List.of());
        post.setShortVideoPath("/post.mp4");
        post.setCreateDate(Date.from(Instant.now()));

        given(postRepository.findById(postId)).willReturn(Optional.of(post));

        // when
        Post onePostById = underTest.getOnePostById(postId);

        // then
        ArgumentCaptor<Long> postIdCaptor = ArgumentCaptor.forClass(Long.class);

        verify(postRepository).findById(postIdCaptor.capture());

        assertThat(postIdCaptor.getValue()).isEqualTo(postId);
        assertThat(onePostById).isNotNull();
        assertThat(onePostById).isEqualTo(post);
    }

    @Test
    void shouldReturnNullWhenPostDoesNotExistByIdGetOnePostById() {
        // given
        Long postId = 72L;

        // when
        Post onePostById = underTest.getOnePostById(postId);

        // then
        ArgumentCaptor<Long> postIdCaptor = ArgumentCaptor.forClass(Long.class);

        verify(postRepository).findById(postIdCaptor.capture());

        assertThat(postIdCaptor.getValue()).isEqualTo(postId);
        assertThat(onePostById).isNull();
    }

    @Test
    void shouldReturnPostWithLikesWhenPostExistsGetOnePostByIdWithLikes() {
        // given
        Long postId = 72L;
        Long userId = 73L;
        Long userId2 = 74L;
        Long userId3 = 75L;

        User user = new User(userId, "username", "password", 0);
        User user2 = new User(userId2, "username2", "password2", 1);
        User user3 = new User(userId3, "username3", "password3", 2);

        Post post = new Post();
        post.setId(postId);
        post.setTitle("Post Title");
        post.setText("Post Text");
        post.setUser(user);
        post.setImagePaths(List.of());
        post.setShortVideoPath("/post.mp4");
        post.setCreateDate(Date.from(Instant.now()));

        Like like1 = new Like();
        like1.setId(1L);
        like1.setUser(user);
        like1.setPost(post);

        Like like2 = new Like();
        like2.setId(2L);
        like2.setUser(user2);
        like2.setPost(post);

        Like like3 = new Like();
        like3.setId(3L);
        like3.setUser(user3);
        like3.setPost(post);

        List<Like> likesList = List.of(
                like1,
                like2,
                like3
        );

        List<LikeResponse> likeResponsesList = likesList.stream().map(LikeResponse::new).toList();

        PostResponse postWithLikes = new PostResponse(post, likeResponsesList);

        given(postRepository.findById(postId)).willReturn(Optional.of(post));
        given(likeService.getAllLikes(Optional.empty(), Optional.of(postId))).willReturn(likeResponsesList);

        // when
        PostResponse onePostByIdWithLikes = underTest.getOnePostByIdWithLikes(postId);

        // then
        ArgumentCaptor<Long> postIdCaptor = ArgumentCaptor.forClass(Long.class);

        verify(postRepository).findById(postIdCaptor.capture());
        verify(likeService).getAllLikes(eq(Optional.empty()), any());

        assertThat(postIdCaptor.getValue()).isEqualTo(postId);
        assertThat(onePostByIdWithLikes).isNotNull();
        assertThat(onePostByIdWithLikes).isEqualTo(postWithLikes);
    }

    @Test
    void willThrowPostNotFoundExceptionWhenPostDoesNotExistGetOnePostByIdWithLikes() {
        // given
        // when
        Long postId = 72L;

        // then
        assertThatThrownBy(() -> underTest.getOnePostByIdWithLikes(postId))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessageContaining("Post with id: " + postId + " does not exist!");

        ArgumentCaptor<Long> postIdCaptor = ArgumentCaptor.forClass(Long.class);

        verify(postRepository).findById(postIdCaptor.capture());
        verify(likeService, never()).getAllLikes(eq(Optional.empty()), any());

        assertThat(postIdCaptor.getValue()).isEqualTo(postId);
    }

    @Test
    void shouldReturnPostWhenUserExistsCreateOnePost() {
        // given
        Long userId = 72L;
        User user = new User(userId, "username", "password", 0);

        String text = "Post Text";
        String title = "Post Title";
        List<String> imagePaths = List.of();
        String shortVideoPath = "/video.mp4";
        PostCreateRequest postCreateRequest = new PostCreateRequest();
        postCreateRequest.setUserId(user.getId());
        postCreateRequest.setTitle(title);
        postCreateRequest.setText(text);
        postCreateRequest.setImagePaths(imagePaths);
        postCreateRequest.setShortVideoPath(shortVideoPath);

        Post toSaveFromPostRequest = new Post();
        toSaveFromPostRequest.setTitle(postCreateRequest.getTitle());
        toSaveFromPostRequest.setText(postCreateRequest.getText());
        toSaveFromPostRequest.setShortVideoPath(postCreateRequest.getShortVideoPath());
        toSaveFromPostRequest.setImagePaths(postCreateRequest.getImagePaths());
        toSaveFromPostRequest.setUser(user);
        toSaveFromPostRequest.setCreateDate(new Date());

        given(userService.getOneUserById(userId)).willReturn(user);
        given(postRepository.save(any())).willReturn(toSaveFromPostRequest);

        // when
        Post onePost = underTest.createOnePost(postCreateRequest);

        // then
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);

        verify(userService).getOneUserById(userIdCaptor.capture());
        verify(postRepository).save(postCaptor.capture());

        assertThat(userIdCaptor.getValue()).isEqualTo(userId);
        assertThat(postCaptor.getValue())
                .usingRecursiveComparison()
                .ignoringFields("createDate")
                .isEqualTo(toSaveFromPostRequest);
        assertThat(onePost)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(toSaveFromPostRequest);
    }

    @Test
    void shouldReturnNullWhenUserDoesNotExistCreateOnePost() {
        // given
        Long userId = 72L;

        PostCreateRequest postCreateRequest = new PostCreateRequest();
        postCreateRequest.setUserId(userId);

        given(userService.getOneUserById(any())).willReturn(null);

        // when
        Post onePost = underTest.createOnePost(postCreateRequest);

        // then
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);

        verify(userService).getOneUserById(userIdCaptor.capture());
        verify(postRepository, never()).save(any());

        assertThat(userIdCaptor.getValue()).isEqualTo(userId);
        assertThat(onePost).isNull();
    }

    @Test
    void shouldUpdateAndReturnOnePostById() {
        // given
        Long postId = 72L;
        Long userId = 73L;

        User user = new User(userId, "username", "password", 0);

        Post post = new Post();
        post.setId(postId);
        post.setUser(user);
        post.setTitle("Post Title");
        post.setText("Post Text");
        post.setImagePaths(List.of());
        post.setShortVideoPath("/video.mp4");
        post.setCreateDate(Date.from(Instant.now()));

        Post postUpdate = new Post();
        postUpdate.setId(post.getId());
        postUpdate.setUser(post.getUser());
        postUpdate.setTitle("Post Title Updated");
        postUpdate.setText("Post Text Updated");
        postUpdate.setImagePaths(List.of());
        postUpdate.setShortVideoPath("/video_updated.mp4");
        postUpdate.setCreateDate(post.getCreateDate());

        PostUpdateRequest postUpdateRequest = new PostUpdateRequest();
        postUpdateRequest.setTitle(postUpdate.getTitle());
        postUpdateRequest.setText(postUpdate.getText());
        postUpdateRequest.setImagePaths(postUpdate.getImagePaths());
        postUpdateRequest.setShortVideoPath(postUpdate.getShortVideoPath());

        given(postRepository.findById(postId)).willReturn(Optional.of(post));
        given(postRepository.save(any())).willReturn(postUpdate);

        // when
        Post postReturnedAfterUpdate = underTest.updateOnePostById(postId, postUpdateRequest);

        // then
        ArgumentCaptor<Long> postIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);

        verify(postRepository).findById(postIdCaptor.capture());
        verify(postRepository).save(postCaptor.capture());

        assertThat(postReturnedAfterUpdate).isNotNull();
        assertThat(postIdCaptor.getValue()).isEqualTo(postId);
        assertThat(postCaptor.getValue()).isEqualTo(postUpdate);
        assertThat(postReturnedAfterUpdate).isEqualTo(postUpdate);
    }

    @Test
    void shouldReturnNullWhenPostDoesNotExistOnePostById() {
        // given
        Long postId = 72L;
        given(postRepository.findById(postId)).willReturn(Optional.empty());

        // when
        Post updatedPost = underTest.updateOnePostById(postId, new PostUpdateRequest());

        // then
        ArgumentCaptor<Long> postIdCaptor = ArgumentCaptor.forClass(Long.class);

        verify(postRepository).findById(postIdCaptor.capture());
        verify(postRepository, never()).save(any());

        assertThat(updatedPost).isNull();
        assertThat(postIdCaptor.getValue()).isEqualTo(postId);
    }

    @Test
    void shouldReturnNullIfPostDoesNotExistUpdateOnePostById() {
        // given
        Long postId = 72L;
        given(postRepository.findById(postId)).willReturn(Optional.empty());

        // when
        Post postUpdated = underTest.updateOnePostById(postId, new PostUpdateRequest());

        // then
        verify(postRepository).findById(postId);
        verify(postRepository, never()).save(any());

        assertThat(postUpdated).isNull();
    }

    @Test
    void shouldDeleteOnePostById() {
        // given
        Long postId = 72L;
        given(postRepository.existsById(postId)).willReturn(true);

        // when
        underTest.deleteOnePostById(postId);

        // then
        verify(postRepository).deleteById(postId);
    }

    @Test
    void willThrowPostNotFoundExceptionWhenPostDoesNotExistDeleteOnePostById() {
        // given
        // when
        Long postId = 72L;
        given(postRepository.existsById(postId)).willReturn(false);

        // then
        assertThatThrownBy(() -> underTest.deleteOnePostById(postId))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessageContaining("Tried to delete post with id " + postId + ". Post does not exist!");

        verify(postRepository, never()).deleteById(any());
    }

    @Test
    void shouldReturnCountGetCount() {
        // given
        Long count = 72L;
        given(postRepository.count()).willReturn(count);

        // when
        Long obtainedCount = underTest.getCount();

        // then
        verify(postRepository).count();

        assertThat(obtainedCount).isNotNull();
        assertThat(obtainedCount).isEqualTo(count);
    }
}