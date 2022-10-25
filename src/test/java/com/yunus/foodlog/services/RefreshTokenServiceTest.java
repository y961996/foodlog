package com.yunus.foodlog.services;

import com.yunus.foodlog.entities.RefreshToken;
import com.yunus.foodlog.entities.User;
import com.yunus.foodlog.repositories.RefreshTokenRepository;
import com.yunus.foodlog.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private UserRepository userRepository;
    private RefreshTokenService underTest;

    @BeforeEach
    void setUp() {
        underTest = new RefreshTokenService(refreshTokenRepository);
    }

    //TODO: Check this method and the method below if they can be written more clear
    @Test
    void shouldCreateRefreshTokenWhenTokenIsNull() {
        ReflectionTestUtils.setField(underTest, "expireSeconds", 604800L, Long.class);

        // given
        Long userId = 72L;
        given(userRepository.findById(userId)).willReturn(Optional.of(new User(userId, "username", "password", 3)));

        Optional<User> userOptional = userRepository.findById(userId);
        assertThat(userOptional).isPresent();

        User userFound = userOptional.get();

        // when
        underTest.createRefreshToken(userFound);

        // then
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<RefreshToken> refreshTokenArgumentCaptor = ArgumentCaptor.forClass(RefreshToken.class);

        verify(refreshTokenRepository).findByUserId(userIdCaptor.capture());
        verify(refreshTokenRepository).save(refreshTokenArgumentCaptor.capture());

        Long capturedUserID = userIdCaptor.getValue();
        RefreshToken capturedToken = refreshTokenArgumentCaptor.getValue();

        assertThat(capturedUserID).isEqualTo(userId);
        assertThat(capturedToken).isNotNull();
        assertThat(capturedToken.getUser()).isEqualTo(userFound);
    }

    @Test
    void shouldCreateRefreshTokenWhenTokenIsNotNull() {
        ReflectionTestUtils.setField(underTest, "expireSeconds", 604800L, Long.class);

        // given
        Long userId = 72L;
        given(userRepository.findById(userId)).willReturn(Optional.of(new User(userId, "username", "password", 3)));

        Optional<User> userOptional = userRepository.findById(userId);
        assertThat(userOptional).isPresent();

        User userFound = userOptional.get();

        String initialToken = UUID.randomUUID().toString();
        RefreshToken toSave = new RefreshToken();
        toSave.setUser(userFound);
        toSave.setToken(initialToken);
        toSave.setExpiryDate(Date.from(Instant.now().plusSeconds(604800)));

        // when
        when(refreshTokenRepository.findByUserId(userId)).thenReturn(toSave);
        String refreshTokenRefreshed = underTest.createRefreshToken(userFound);

        // then
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<RefreshToken> capturedRefreshToken = ArgumentCaptor.forClass(RefreshToken.class);

        verify(refreshTokenRepository).findByUserId(userIdCaptor.capture());
        verify(refreshTokenRepository).save(capturedRefreshToken.capture());

        assertThat(userIdCaptor.getValue()).isEqualTo(userId);
        assertThat(capturedRefreshToken.getValue().getUser()).isEqualTo(toSave.getUser());
        assertThat(capturedRefreshToken.getValue())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(toSave);
        assertThat(initialToken).isNotEqualTo(refreshTokenRefreshed);
    }

    //TODO: Write this time tests more clear and better
    //      https://www.youtube.com/watch?v=PrPQ5xHYa0s
    @Test
    void shouldReturnTrueWhenRefreshExpired() {
        // given
        RefreshToken token = new RefreshToken();
        token.setExpiryDate(Date.from(Instant.now().minusSeconds(1)));

        // when
        boolean expired = underTest.isRefreshExpired(token);

        // then
        assertThat(expired).isTrue();
    }

    @Test
    void shouldReturnFalseWhenRefreshNotExpired() {
        // given
        RefreshToken token = new RefreshToken();
        token.setExpiryDate(Date.from(Instant.now().plusSeconds(99999)));

        // when
        boolean expired = underTest.isRefreshExpired(token);

        // then
        assertThat(expired).isFalse();
    }

    @Test
    void shouldReturnRefreshTokenWhenTokenExistsByUserId() {
        // given
        Long userId = 72L;

        RefreshToken token = new RefreshToken();
        token.setId(1L);
        token.setToken(UUID.randomUUID().toString());
        token.setUser(new User(userId, "username", "password", 0));
        token.setExpiryDate(Date.from(Instant.now().plusSeconds(99999)));

        // when
        when(refreshTokenRepository.findByUserId(userId)).thenReturn(token);
        RefreshToken tokenFound = underTest.getByUser(userId);

        // then
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);

        verify(refreshTokenRepository).findByUserId(userIdCaptor.capture());

        assertThat(userIdCaptor.getValue().longValue()).isEqualTo(userId.longValue());
        assertThat(tokenFound).isNotNull();
        assertThat(tokenFound).isEqualTo(token);
    }

    @Test
    void shouldReturnNullWhenTokenDoesNotExistByUserId() {
        // given
        Long userId = 72L;

        // when
        when(refreshTokenRepository.findByUserId(userId)).thenReturn(null);
        RefreshToken tokenFound = underTest.getByUser(userId);

        // then
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);

        verify(refreshTokenRepository).findByUserId(userIdCaptor.capture());

        assertThat(userIdCaptor.getValue().longValue()).isEqualTo(userId.longValue());
        assertThat(tokenFound).isNull();
    }

}