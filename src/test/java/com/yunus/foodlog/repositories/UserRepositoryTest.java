package com.yunus.foodlog.repositories;

import com.yunus.foodlog.entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldCheckWhenUserExistsByUserName() {
        // given
        String userName = "username";
        User user = new User(userName, "password", 0);
        underTest.save(user);

        // when
        User userFound = underTest.findByUserName(userName);

        // then
        assertThat(userFound).isNotNull();
        assertThat(userFound.getId()).isEqualTo(1L);
        assertThat(userFound.getUserName()).isEqualTo(userName);
        assertThat(userFound.getAvatar()).isEqualTo(0);
    }

    @Test
    void itShouldCheckWhenUserDoesNotExistByUserName() {
        // given
        String userName = "username";

        // when
        User userFound = underTest.findByUserName(userName);

        // then
        assertThat(userFound).isNull();
    }
}