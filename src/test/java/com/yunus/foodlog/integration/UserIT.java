package com.yunus.foodlog.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.yunus.foodlog.entities.User;
import com.yunus.foodlog.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(
        locations = "classpath:application-it.properties"
)
@AutoConfigureMockMvc
public class UserIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    private final Faker faker = new Faker();

    @Test
    void canCreateNewUser() throws Exception {
        // given
        String userName = String.format("%s", faker.name().username());
        String password = String.format("%s", faker.internet().password());
        int avatar = faker.number().numberBetween(0, 5);
        User user = new User(
                userName,
                password,
                avatar);

        // when
        ResultActions resultActions = mockMvc
                .perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        // then
        resultActions.andExpect(status().isOk());
        List<User> users = userRepository.findAll();
        assertThat(users)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(user);
    }

    @Test
    void canDeleteUserById() throws Exception {
        // given
        String userName = String.format("%s", faker.name().username());
        String password = String.format("%s", faker.internet().password());
        int avatar = faker.number().numberBetween(0, 5);
        User user = new User(
                userName,
                password,
                avatar);

        mockMvc.perform(post("/api/v1/users")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(user)));

        MvcResult getUserResult = mockMvc.perform(get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = getUserResult.getResponse().getContentAsString();

        List<User> users = objectMapper.readValue(
                contentAsString,
                new TypeReference<>() {
                }
        );

        long id = users
                .stream()
                .filter(u -> u.getUserName().equals(user.getUserName()))
                .map(User::getId)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException("User with userName: " + userName + " not found!"));

        // when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/users/" + id));

        // then
        resultActions.andExpect(status().isOk());
        boolean exists = userRepository.existsById(id);
        assertThat(exists).isFalse();
    }
}
