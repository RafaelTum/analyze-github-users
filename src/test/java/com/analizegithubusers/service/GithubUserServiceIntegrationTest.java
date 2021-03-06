package com.analizegithubusers.service;

import com.analizegithubusers.model.GithubUser;
import com.analizegithubusers.repository.GithubUserRepository;
import com.analizegithubusers.AbstractIntegrationTest;
import com.analizegithubusers.datagrabbing.DataGrabbingScheduler;
import com.analizegithubusers.dto.GithubUserRequestDto;
import com.analizegithubusers.dto.GithubUserResponseDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import ma.glasnost.orika.MapperFacade;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import reactor.test.StepVerifier;

import java.io.File;
import java.util.List;

@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
class GithubUserServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private GithubUserService githubUserService;
    @Autowired
    private GithubUserRepository githubUserRepository;
    @Autowired
    private MapperFacade mapperFacade;
    @MockBean
    //added to escape initialization while context loading
    private DataGrabbingScheduler dataGrabbingScheduler;
    @MockBean
    //added to escape initialization while context loading
    private CommandLineRunner commandLineRunner;

    private static List<GithubUser> githubUserList;

    @SneakyThrows
    @BeforeAll
    static void beforeAll() {
        ObjectMapper objectMapper = new ObjectMapper();
        githubUserList = objectMapper.readValue(new File("src/test/resources/githubusers.json"), new TypeReference<List<GithubUser>>() {
        });
    }

    @BeforeEach
    void clearDB() {
        githubUserRepository.deleteAll();
    }

    @Test
    void findByUsernamePartiallyMatching() {
        githubUserRepository.saveAll(githubUserList).subscribe();

        StepVerifier.create(githubUserService.findByUsernamePartiallyMatching("evan"))
                .expectSubscription()
                .expectNext(mapperFacade.map(githubUserList.get(4), GithubUserResponseDto.class))
                .expectNextCount(0)
                .expectComplete()
                .verify();
    }

    @Test
    void saveAll() {
        List<GithubUserRequestDto> dtoList = mapperFacade.mapAsList(githubUserList, GithubUserRequestDto.class);

        StepVerifier.create(githubUserService.saveAll(dtoList))
                .expectSubscription()
                .expectNextCount(0)
                .expectComplete()
                .verify();
    }

    @Test
    void deleteAll() {
        StepVerifier.create(githubUserRepository.saveAll(githubUserList))
                .expectSubscription()
                .expectNextCount(7)
                .expectComplete()
                .verify();

        StepVerifier.create(githubUserService.deleteAll())
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void findGroupedByLocation() {
        StepVerifier.create(githubUserRepository.saveAll(githubUserList))
                .expectSubscription()
                .expectNextCount(7)
                .expectComplete()
                .verify();

        StepVerifier.create(githubUserService.findGroupedByLocation())
                .expectSubscription()
                .expectNextCount(3)
                .expectComplete()
                .verify();
    }
}
