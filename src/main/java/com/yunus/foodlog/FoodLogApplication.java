package com.yunus.foodlog;

import com.yunus.foodlog.utils.DataPopulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class FoodLogApplication {

    @Autowired(required = false)
    private DataPopulator dataPopulator;

    public static void main(String[] args) {
        SpringApplication.run(FoodLogApplication.class, args);
    }

    @Bean
    @Profile("!test")
    public CommandLineRunner commandLineRunner() {
        return args -> {
            System.out.println("====================================================================");
            System.out.println("Populating test data to db...");

            dataPopulator.createUser();

            dataPopulator.createPost();

            dataPopulator.createComment();

            dataPopulator.createLike();

            System.out.println("Successfully populated all data.");
            System.out.println("====================================================================");
        };
    }
}
