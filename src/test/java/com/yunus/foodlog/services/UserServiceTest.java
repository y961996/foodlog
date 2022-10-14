package com.yunus.foodlog.services;

import com.yunus.foodlog.entities.User;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
    @Disabled
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
    @Disabled
    void getOneUserById() {
    }

    @Test
    @Disabled
    void updateOneUserById() {
    }

    @Test
    @Disabled
    void deleteOneUserById() {
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