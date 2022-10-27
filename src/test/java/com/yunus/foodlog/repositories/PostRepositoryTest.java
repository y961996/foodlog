package com.yunus.foodlog.repositories;

import com.yunus.foodlog.entities.Post;
import com.yunus.foodlog.entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Date;
import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class PostRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldReturnPostsListsWhenUserExistsById() {
        // given
        User user = new User("username", "password", 0);
        userRepository.save(user);

        User userFound = userRepository.findByUserName(user.getUserName());
        Long userId = userFound.getId();

        List<Post> postListToSave = new ArrayList<>();
        for(long i = 0; i < 3; i++){
            Post post = new Post();
            post.setId(i+1);
            post.setUser(userFound);
            post.setTitle("Post" + (i+1) + " Title");
            post.setText("Post" + (i+1) + " Text");
            post.setShortVideoPath("/post" + (i+1) + ".mp4");
            post.setImagePaths(new ArrayList<>());
            post.setCreateDate(Date.from(Instant.now()));

            postListToSave.add(post);
        }

        underTest.saveAll(postListToSave);

        // when
        List<Post> postsByUserId = underTest.findByUserId(userId);

        // then
        assertThat(postsByUserId).isNotNull();
        assertThat(postsByUserId.size()).isEqualTo(3);
        assertThat(!Collections.disjoint(postsByUserId, postListToSave)).isTrue();
    }


    @Test
    void shouldReturnEmptyListWhenUserDoesNotExistById() {
        // given
        Long userId = 72L;

        // when
        Optional<Post> postListByUserId = underTest.findById(userId);

        // then
        assertThat(postListByUserId).isEmpty();
    }

    @Test
    void shouldReturnTopPostIdsWhenUserExistsAndPostsSizeIsGreaterThanFiveFindTopByUserId() {
        // given
        User user = new User("username", "password", 0);
        userRepository.save(user);

        User userFound = userRepository.findByUserName(user.getUserName());
        Long userId = userFound.getId();

        List<Post> postListToSave = new ArrayList<>();
        for(long i = 0; i < 10; i++){
            Post post = new Post();
            post.setId(i+1);
            post.setUser(userFound);
            post.setTitle("Post" + (i+1) + " Title");
            post.setText("Post" + (i+1) + " Text");
            post.setShortVideoPath("/post" + (i+1) + ".mp4");
            post.setImagePaths(new ArrayList<>());
            post.setCreateDate(Date.from(Instant.now().plusSeconds(i)));
            postListToSave.add(post);

        }

        underTest.saveAll(postListToSave);

        postListToSave.sort((p1, p2) -> p2.getCreateDate().compareTo(p1.getCreateDate()));

        // when
        List<Long> topPostIdListByUserId = underTest.findTopByUserId(userId);

        // then
        assertThat(topPostIdListByUserId).isNotNull();
        assertThat(topPostIdListByUserId.size()).isEqualTo(5);
        assertThat(!Collections.disjoint(
                topPostIdListByUserId,
                postListToSave.subList(0, 5).stream().map(Post::getId).toList()
        )).isTrue();
    }

    @Test
    void shouldReturnTopPostIdsWhenUserExistsAndPostsSizeIsLessThanFiveFindTopByUserId() {
        // given
        User user = new User("username", "password", 0);
        userRepository.save(user);

        User userFound = userRepository.findByUserName(user.getUserName());
        Long userId = userFound.getId();

        List<Post> postListToSave = new ArrayList<>();
        for(long i = 0; i < 3; i++){
            Post post = new Post();
            post.setId(i+1);
            post.setUser(userFound);
            post.setTitle("Post" + (i+1) + " Title");
            post.setText("Post" + (i+1) + " Text");
            post.setShortVideoPath("/post" + (i+1) + ".mp4");
            post.setImagePaths(new ArrayList<>());
            post.setCreateDate(Date.from(Instant.now().plusSeconds(i)));
            postListToSave.add(post);

        }

        underTest.saveAll(postListToSave);

        postListToSave.sort((p1, p2) -> p2.getCreateDate().compareTo(p1.getCreateDate()));

        // when
        List<Long> topPostIdListByUserId = underTest.findTopByUserId(userId);

        System.out.println(topPostIdListByUserId);
        System.out.println(postListToSave.subList(0, 3).stream().map(Post::getId).toList());

        // then
        assertThat(topPostIdListByUserId).isNotNull();
        assertThat(topPostIdListByUserId.size()).isEqualTo(3);
        assertThat(!Collections.disjoint(
                topPostIdListByUserId,
                postListToSave.subList(0, 3).stream().map(Post::getId).toList()
        )).isTrue();
    }

    @Test
    void shouldReturnEmptyListWhenUserDoesNotExistFindTopByUserId() {
        // given
        Long userId = 72L;

        // when
        List<Long> topPostIdsByUserId = underTest.findTopByUserId(userId);

        // then
        assertThat(topPostIdsByUserId.isEmpty()).isTrue();
    }
}