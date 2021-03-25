package com.example.analizegithubusers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories(basePackages = "com.example.analizegithubusers.repository")
public class AnalizeGithubUsersApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnalizeGithubUsersApplication.class, args);
    }

}