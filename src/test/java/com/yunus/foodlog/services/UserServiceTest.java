package com.yunus.foodlog.services;

import com.yunus.foodlog.entities.User;
import com.yunus.foodlog.exceptions.UserAlreadyExistsException;
import com.yunus.foodlog.exceptions.UserNotFoundException;
import com.yunus.foodlog.repositories.CommentRepository;
import com.yunus.foodlog.repositories.LikeRepository;
import com.yunus.foodlog.repositories.PostRepository;
import com.yunus.foodlog.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private LikeRepository likeRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private PostRepository postRepository;
    private UserService underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserService(userRepository, likeRepository, commentRepository, postRepository);
    }

    @Test
    void getAllUsers() {
        // when
        underTest.getAllUsers();

        //then
        verify(userRepository).findAll();
    }

    @Test
    void createOneUser() {
        // given
        User user = new User("username", "password", 0);

        // when
        underTest.createOneUser(user);

        // then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser).isEqualTo(user);
    }

    @Test
    void willThrowWhenCreateUserUsernameIsTaken() {
        // given
        User user = new User("username", "password", 0);

        given(userRepository.selectExistsUserName(anyString()))
                .willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> underTest.createOneUser(user))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("Username: " + user.getUserName() + " has already taken!");

        verify(userRepository, never()).save(any());
    }

    @Test
    void getOneUserById() {
        // given
        long id = 72;
        User user = new User(id, "username", "password", 0);
        given(userRepository.findById(id)).willReturn(Optional.of(user));

        //when
        User userById = underTest.getOneUserById(id);

        //then
        verify(userRepository).findById(id);
        assertThat(userById.getId()).isEqualTo(id);
    }

    @Test
    void getOneUserByUserName() {
        // given
        String userName = "username";
        User user = new User(1L, userName, "password", 0);
        given(userRepository.findByUserName(userName)).willReturn(user);

        //when
        User userByUserName = underTest.getOneUserByUserName(userName);

        //then
        verify(userRepository).findByUserName(userName);
        assertThat(userByUserName.getUserName()).isEqualTo(userName);

    }

    @Test
    void willReturnNullWhenUserNotExistsGetOneUserById() {
        // given
        long id = 72;
        given(userRepository.findById(id)).willReturn(Optional.empty());

        //when
        User userById = underTest.getOneUserById(id);

        //then
        verify(userRepository).findById(id);
        assertThat(userById).isNull();
    }

    @Test
    void updateOneUserById() {
        // given
        long id = 72;
        User user = new User(id, "username", "password", 0);
        User newUser = new User("update_username", "updated_password", user.getAvatar());


        //when
        given(userRepository.findById(id)).willReturn(Optional.of(user));

        User updateResult = underTest.updateOneUserById(id, newUser);

        //then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).findById(id);
        verify(userRepository).save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();
        Optional<User> updatedUser = userRepository.findById(id);

        assertThat(updatedUser).isPresent();
        assertThat(capturedUser).isEqualTo(updatedUser.get());
        assertThat(updatedUser).isNotEqualTo(user);
        assertThat(updateResult).isNotNull();
        assertThat(updateResult).isEqualTo(updatedUser.get());
    }

    @Test
    void willUpdateOneUserByIdWhenOnlyUserNameGiven() {
        // given
        long id = 72;
        User user = new User(id, "username", "password", 0);
        User newUser = new User("update_username", null, user.getAvatar());


        //when
        given(userRepository.findById(id)).willReturn(Optional.of(user));

        User updateResult = underTest.updateOneUserById(id, newUser);

        //then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).findById(id);
        verify(userRepository).save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();
        Optional<User> updatedUser = userRepository.findById(id);

        assertThat(updatedUser).isPresent();
        assertThat(capturedUser).isEqualTo(updatedUser.get());
        assertThat(updatedUser).isNotEqualTo(user);
        assertThat(updateResult).isNotNull();
        assertThat(updateResult).isEqualTo(updatedUser.get());
    }

    @Test
    void willUpdateOneUserByIdWhenOnlyPasswordGiven() {
        // given
        long id = 72;
        User user = new User(id, "username", "password", 0);
        User newUser = new User(null, "updated_password", user.getAvatar());


        //when
        given(userRepository.findById(id)).willReturn(Optional.of(user));

        User updateResult = underTest.updateOneUserById(id, newUser);

        //then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).findById(id);
        verify(userRepository).save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();
        Optional<User> updatedUser = userRepository.findById(id);

        assertThat(updatedUser).isPresent();
        assertThat(capturedUser).isEqualTo(updatedUser.get());
        assertThat(updatedUser).isNotEqualTo(user);
        assertThat(updateResult).isNotNull();
        assertThat(updateResult).isEqualTo(updatedUser.get());
    }

    @Test
    void willUpdateOneUserByIdWhenOnlyAvatarGiven() {
        // given
        long id = 72;
        User user = new User(id, "username", "password", 0);
        User newUser = new User(null, null, user.getAvatar() + 72);

        //when
        given(userRepository.findById(id)).willReturn(Optional.of(user));

        User updateResult = underTest.updateOneUserById(id, newUser);

        //then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).findById(id);
        verify(userRepository).save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();
        Optional<User> updatedUser = userRepository.findById(id);

        assertThat(updatedUser).isPresent();
        assertThat(capturedUser).isEqualTo(updatedUser.get());
        assertThat(updatedUser).isNotEqualTo(user);
        assertThat(updateResult).isNotNull();
        assertThat(updateResult).isEqualTo(updatedUser.get());
    }

    @Test
    void willUpdateOneUserByIdWhenUserNameAndPasswordGiven() {
        // given
        long id = 72;
        User user = new User(id, "username", "password", 0);
        User newUser = new User("updated_username", "updated_password", user.getAvatar());

        //when
        given(userRepository.findById(id)).willReturn(Optional.of(user));

        User updateResult = underTest.updateOneUserById(id, newUser);

        //then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).findById(id);
        verify(userRepository).save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();
        Optional<User> updatedUser = userRepository.findById(id);

        assertThat(updatedUser).isPresent();
        assertThat(capturedUser).isEqualTo(updatedUser.get());
        assertThat(updatedUser).isNotEqualTo(user);
        assertThat(updateResult).isNotNull();
        assertThat(updateResult).isEqualTo(updatedUser.get());
    }

    @Test
    void willUpdateOneUserByIdWhenUserNameAndAvatarGiven() {
        // given
        long id = 72;
        User user = new User(id, "username", "password", 0);
        User newUser = new User("updated_username", null, user.getAvatar() + 72);

        //when
        given(userRepository.findById(id)).willReturn(Optional.of(user));

        User updateResult = underTest.updateOneUserById(id, newUser);

        //then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).findById(id);
        verify(userRepository).save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();
        Optional<User> updatedUser = userRepository.findById(id);

        assertThat(updatedUser).isPresent();
        assertThat(capturedUser).isEqualTo(updatedUser.get());
        assertThat(updatedUser).isNotEqualTo(user);
        assertThat(updateResult).isNotNull();
        assertThat(updateResult).isEqualTo(updatedUser.get());
    }

    @Test
    void willUpdateOneUserByIdWhenPasswordAndAvatarGiven() {
        // given
        long id = 72;
        User user = new User(id, "username", "password", 0);
        User newUser = new User(null, "updated_password", user.getAvatar() + 72);

        //when
        given(userRepository.findById(id)).willReturn(Optional.of(user));

        User updateResult = underTest.updateOneUserById(id, newUser);

        //then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).findById(id);
        verify(userRepository).save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();
        Optional<User> updatedUser = userRepository.findById(id);

        assertThat(updatedUser).isPresent();
        assertThat(capturedUser).isEqualTo(updatedUser.get());
        assertThat(updatedUser).isNotEqualTo(user);
        assertThat(updateResult).isNotNull();
        assertThat(updateResult).isEqualTo(updatedUser.get());
    }

    @Test
    void willReturnNullIfUserNotPresentUpdateOneUserById() {
        // given
        long id = 72;
        User user = new User(id, "username", "password", 0);
        User newUser = new User(null, null, user.getAvatar() + 72);

        //when
        given(userRepository.findById(id)).willReturn(Optional.empty());

        User updateResult = underTest.updateOneUserById(id, newUser);

        //then
        verify(userRepository).findById(id);
        verify(userRepository, never()).save(any());

        assertThat(updateResult).isNull();
    }

    @Test
    void deleteOneUserById() {
        // given
        long id = 72;
        given(userRepository.existsById(id)).willReturn(true);

        // when
        underTest.deleteOneUserById(id);

        // then
        verify(userRepository).deleteById(id);
    }

    @Test
    void willThrowWhenDeleteUserNotFound() {
        // given
        // when
        long id = 72;
        given(userRepository.existsById(id)).willReturn(false);

        // then
        assertThatThrownBy(() -> underTest.deleteOneUserById(id))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User with id " + id + " does not exist!");

        verify(userRepository, never()).deleteById(any());
    }

    @Test
    void getCount() {
        // given
        Long count = 72L;
        given(userRepository.count()).willReturn(count);

        //when
        Long receivedCount = underTest.getCount();

        //then
        verify(userRepository).count();
        assertThat(receivedCount.longValue()).isEqualTo(count.longValue());
    }

    @Test
    void getUserActivity() {
        // given
        class ListClass {
            final String commentedOn;
            final int postId;
            final int avatar;
            final String userName;

            public ListClass(Integer postId, Integer avatar, String userName) {
                this.commentedOn = "commented_on";
                this.postId = postId;
                this.avatar = avatar;
                this.userName = userName;
            }
        }

        class LikeClass {
            final String liked;
            final int postId;
            final int avatar;
            final String userName;

            public LikeClass(Integer postId, Integer avatar, String userName) {
                this.liked = "liked";
                this.postId = postId;
                this.avatar = avatar;
                this.userName = userName;
            }
        }

        long id = 72;
        List<Long> postIds = List.of(1L, 2L, 3L, 4L, 5L);
        List<Object> comments = List.of(
                new ListClass(1, 1, "username"),
                new ListClass(2, 1, "username"),
                new ListClass(3, 1, "username"),
                new ListClass(4, 1, "username"),
                new ListClass(5, 1, "username")
                );
        List<Object> likes = List.of(
                new LikeClass(1, 1, "username"),
                new LikeClass(2, 1, "username"),
                new LikeClass(4, 1, "username")
        );
        List<List<Object>> resultedList = new ArrayList<>();
        resultedList.add(comments);
        resultedList.add(likes);

        given(postRepository.findTopByUserId(id)).willReturn(postIds);
        given(commentRepository.findUserCommentsByPostId(postIds)).willReturn(Collections.singletonList(comments));
        given(likeRepository.findUserLikesByPostId(postIds)).willReturn(Collections.singletonList(likes));

        //when
        List<Object> receivedUserActivity = underTest.getUserActivity(id);

        //then
        verify(postRepository).findTopByUserId(id);
        verify(commentRepository).findUserCommentsByPostId(postIds);
        verify(likeRepository).findUserLikesByPostId(postIds);

        assertThat(receivedUserActivity).isEqualTo(resultedList);
    }

    @Test
    void willReturnNullWhenPostIdsEmptyGetUserActivity() {
        // given
        long id = 72;
        given(postRepository.findTopByUserId(id)).willReturn(List.of());

        // when
        List<Object> receivedUserActivity = underTest.getUserActivity(id);

        // then
        verify(postRepository).findTopByUserId(id);
        verify(commentRepository, never()).findUserCommentsByPostId(any());
        verify(likeRepository, never()).findUserLikesByPostId(any());

        assertThat(receivedUserActivity).isNull();
    }
}