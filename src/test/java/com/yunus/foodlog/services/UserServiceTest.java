package com.yunus.foodlog.services;

import com.yunus.foodlog.entities.User;
import com.yunus.foodlog.exceptions.UserAlreadyExistsException;
import com.yunus.foodlog.exceptions.UserNotFoundException;
import com.yunus.foodlog.repositories.CommentRepository;
import com.yunus.foodlog.repositories.LikeRepository;
import com.yunus.foodlog.repositories.PostRepository;
import com.yunus.foodlog.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

        // given(userRepository.selectExistsUserName(user.getUserName()))
        //        .willReturn(true);

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
    @Disabled
    void getOneUserById() {
    }

    @Test
    @Disabled
    void updateOneUserById() {
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
    @Disabled
    void getOneUserByUserName() {
    }

    @Test
    @Disabled
    void getCount() {
    }

    @Test
    @Disabled
    void getUserActivity() {
    }
}