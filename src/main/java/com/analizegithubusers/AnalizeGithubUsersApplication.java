package com.analizegithubusers;

import com.analizegithubusers.model.Role;
import com.analizegithubusers.model.User;
import com.analizegithubusers.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashSet;

@Slf4j
@SpringBootApplication
@EnableReactiveMongoRepositories(basePackages = "com.analizegithubusers.repository")
public class AnalizeGithubUsersApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnalizeGithubUsersApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(UserRepository userRepository,
                                  BCryptPasswordEncoder passwordEncoder) {
        return args -> {
            userRepository.save(new User(
                    null,
                    "admin",
                    passwordEncoder.encode("password"),
                    "admin",
                    new HashSet<>(Arrays.asList(
                            new Role("USER"),
                            new Role("ADMIN")))))
                    .onErrorResume(throwable -> {
                        log.info("Already exist");
                        return Mono.empty();
                    })
                    .subscribe(user -> log.info("Admin created"));
        };
    }
}