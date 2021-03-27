package com.example.analizegithubusers.service;

import com.example.analizegithubusers.datagrabbing.DataGrabbingScheduler;
import com.example.analizegithubusers.dto.GithubUserRequestDto;
import com.example.analizegithubusers.dto.GithubUserResponseDto;
import com.example.analizegithubusers.model.GithubUser;
import com.example.analizegithubusers.repository.GithubCustomRepository;
import com.example.analizegithubusers.repository.GithubUserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import ma.glasnost.orika.MapperFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.io.File;
import java.util.List;

import static org.mockito.BDDMockito.given;

@SpringBootTest
@ActiveProfiles(profiles = "test")
public class GithubUserServiceImplTest {

    @MockBean
    //added to escapeF scheduler initialization while context loading
    private DataGrabbingScheduler dataGrabbingScheduler;
    @MockBean
    private GithubUserRepository repository;

    @MockBean
    private GithubCustomRepository customRepository;

    @Autowired
    private MapperFacade mapper;

    @Autowired
    private GithubUserServiceImpl service;

    private static List<GithubUser> githubUserList;

    @SneakyThrows
    @BeforeEach
    public void beforeAll() {
        ObjectMapper objectMapper = new ObjectMapper();
        githubUserList = objectMapper.readValue(new File("src/test/resources/githubusers.json"), new TypeReference<List<GithubUser>>() {
        });
    }

    @Test
    public void getAllByPaginationShouldReturnAllUsersForOnePage() {
        given(repository.findAll()).willReturn(Flux.fromIterable(githubUserList));

        long page = 1;
        long pageSize = githubUserList.size();

        StepVerifier.create(service.getAllByPagination(page, pageSize))
                .expectNextCount(pageSize)
                .expectComplete()
                .verify();

        Mockito.verify(repository, Mockito.times(1)).findAll();
    }

    @Test
    public void getAllByPaginationShouldReturnThreeUsersForOnePage() {
        given(repository.findAll()).willReturn(Flux.fromIterable(githubUserList));

        long page = 1;
        long pageSize = 3;

        StepVerifier.create(service.getAllByPagination(page, pageSize))
                .expectNextCount(pageSize)
                .expectComplete()
                .verify();

        long page2 = 2;
        long page2Size = 3;

        StepVerifier.create(service.getAllByPagination(page2, page2Size))
                .expectNextCount(page2Size)
                .expectComplete()
                .verify();

        Mockito.verify(repository, Mockito.times(2)).findAll();
    }

    @Test
    public void getAllByPaginationShouldReturnNoUsersIfCountIsExceeded() {
        given(repository.findAll()).willReturn(Flux.fromIterable(githubUserList));

        long page = 2;
        long pageSize = 100;

        StepVerifier.create(service.getAllByPagination(page, pageSize))
                .expectNextCount(0)
                .expectComplete()
                .verify();

        Mockito.verify(repository, Mockito.times(1)).findAll();
    }

    @Test
    public void getAllByPaginationShouldReturnNoUsersIfCountIsExceeded1() {
        given(repository.findAll()).willReturn(Flux.fromIterable(githubUserList));

        long page = 1;
        long pageSize = -100;

        StepVerifier.create(service.getAllByPagination(page, pageSize))
                .expectError(IllegalArgumentException.class)
                .verify();

        Mockito.verify(repository, Mockito.times(0)).findAll();
    }

    @Test
    public void findByUsernamePartiallyMatching() {
        given(customRepository.findByUsernamePartiallyMatching("cat")).willReturn(Flux.just(githubUserList.get(1)));

        StepVerifier.create(service.findByUsernamePartiallyMatching("cat"))
                .expectSubscription()
                .expectNext(mapper.map(githubUserList.get(1), GithubUserResponseDto.class))
                .verifyComplete();

        Mockito.verify(customRepository, Mockito.times(1))
                .findByUsernamePartiallyMatching(ArgumentMatchers.anyString());
    }

    @Test
    public void saveAll() {
        given(repository.saveAll(githubUserList)).willReturn(Flux.fromIterable(githubUserList));

        StepVerifier.create(service.saveAll(mapper.mapAsList(githubUserList, GithubUserRequestDto.class)))
                .expectSubscription()
                .expectNextCount(0)
                .verifyComplete();

        Mockito.verify(repository, Mockito.times(1)).saveAll(githubUserList);
    }
}