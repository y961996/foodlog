package com.yunus.foodlog;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FoodLogApplication {

    private final DataPopulator dataPopulator;

    public FoodLogApplication(DataPopulator dataPopulator) {
        this.dataPopulator = dataPopulator;
    }

    public static void main(String[] args) {
        SpringApplication.run(FoodLogApplication.class, args);
    }

    @Bean
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
