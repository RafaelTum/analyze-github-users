package com.example.analizegithubusers.handler;

import com.example.analizegithubusers.AbstractIntegrationTest;
import com.example.analizegithubusers.datagrabbing.DataGrabbingScheduler;
import com.example.analizegithubusers.dto.AggregationResultDto;
import com.example.analizegithubusers.dto.GithubUserResponseDto;
import com.example.analizegithubusers.model.GithubUser;
import com.example.analizegithubusers.repository.GithubUserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import ma.glasnost.orika.MapperFacade;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureWebTestClient
@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
class GithubUserHandlerTest extends AbstractIntegrationTest {

    @Autowired
    private GithubUserRepository githubUserRepository;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    //added to escape scheduler initialization while context loading
    private DataGrabbingScheduler dataGrabbingScheduler;

    private static List<GithubUser> githubUserList;

    @SneakyThrows
    @BeforeAll
    static void beforeAll() {
        githubUserList = new ObjectMapper().readValue(new File("src/test/resources/githubusers.json"), new TypeReference<List<GithubUser>>() {
        });
    }

    @BeforeEach
    void clearDB() {
        githubUserRepository.deleteAll();
    }

    @WithMockUser
    @Test
    void getAll() {
        githubUserRepository.saveAll(githubUserList).subscribe();
        webTestClient.get()
                .uri("/api/v1/github-users")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GithubUserResponseDto.class)
                .value(dtoList -> {
                    assertThat(dtoList).contains(mapperFacade.map(githubUserList.get(5), GithubUserResponseDto.class));
                    assertThat(dtoList).hasSize(7);
                });
    }

    @WithMockUser
    @Test
    void getAllWithWrongPaginationWillReturnErrorStatus() {
        githubUserRepository.saveAll(githubUserList).subscribe();
        webTestClient.get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/github-users")
                                .queryParam("page", 1)
                                .queryParam("size", -3)
                                .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBodyList(GithubUserResponseDto.class);
    }

    @WithMockUser
    @Test
    void findByUsernamePartiallyMatching() {
        githubUserRepository.saveAll(githubUserList).subscribe();
        webTestClient.get()
                .uri("/api/v1/github-users/evan")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GithubUserResponseDto.class)
                .value(dtoList -> {
                    assertThat(dtoList.get(0).getLogin()).isEqualTo("evanphx");
                    assertThat(dtoList).hasSize(1);
                });
    }

    @WithMockUser
    @Test
    void findGroupedByLocation() {
        githubUserRepository.saveAll(githubUserList).subscribe();
        webTestClient.get()
                .uri("/api/v1/github-users/group/location")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AggregationResultDto.class)
                .value(dtoList -> {
                    assertThat(dtoList).hasSize(3);
                });
    }

    @WithMockUser
    @Test
    void findGroupedByCompany() {
        githubUserRepository.saveAll(githubUserList).subscribe();
        webTestClient.get()
                .uri("/api/v1/github-users/group/company")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AggregationResultDto.class)
                .value(dtoList -> {
                    assertThat(dtoList).hasSize(4);
                });
    }
}