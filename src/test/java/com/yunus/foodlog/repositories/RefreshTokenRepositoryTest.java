package com.yunus.foodlog.repositories;

import com.yunus.foodlog.entities.RefreshToken;
import com.yunus.foodlog.entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository underTest;
    @Autowired
    private UserRepository userRepository;

    @Value("${foodlog.refresh.token.expires.in}")
    private Long expireSeconds;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void itShouldCheckWhenTokenExistsByUserId() {
        // given
        User user = new User("username", "password", 3);
        userRepository.save(user);

        User userFound = userRepository.findByUserName(user.getUserName());

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userFound);
        refreshToken.setExpiryDate(Date.from(Instant.now().plusSeconds(expireSeconds)));
        refreshToken.setToken(UUID.randomUUID().toString());
        underTest.save(refreshToken);

        // when
        RefreshToken refreshTokenFound = underTest.findByUserId(userFound.getId());

        // then
        assertThat(refreshTokenFound).isNotNull();
        assertThat(refreshTokenFound.getId().longValue()).isEqualTo(refreshToken.getId().longValue());
        assertThat(refreshTokenFound.getToken()).isEqualTo(refreshToken.getToken());
        assertThat(refreshTokenFound.getUser().getId().longValue()).isEqualTo(userFound.getId().longValue());
        assertThat(refreshTokenFound.getUser().getUserName()).isEqualTo(userFound.getUserName());
        assertThat(refreshTokenFound.getUser().getPassword()).isEqualTo(userFound.getPassword());
        assertThat(refreshTokenFound.getUser().getAvatar()).isEqualTo(userFound.getAvatar());
        assertThat(refreshTokenFound.getExpiryDate()).isEqualTo(refreshToken.getExpiryDate());
    }

    @Test
    void itShouldCheckWhenTokenDoesNotExistByUserId() {
        // given
        Long userId = 1L;

        // when
        RefreshToken refreshToken = underTest.findByUserId(userId);

        // then
        assertThat(refreshToken).isNull();
    }
}