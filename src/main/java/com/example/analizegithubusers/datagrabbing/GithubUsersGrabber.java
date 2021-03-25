package com.example.analizegithubusers.datagrabbing;

import com.example.analizegithubusers.config.properties.UsersConfigProperties;
import com.example.analizegithubusers.datagrabbing.dto.GithubUserDto;
import com.example.analizegithubusers.dto.GithubUserRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
public class GithubUsersGrabber {

    private final WebClient webClient;
    private final String path;

    public GithubUsersGrabber(WebClient.Builder webClientBuilder,
                              UsersConfigProperties usersConfigProperties) {
        webClient = webClientBuilder
                .baseUrl(usersConfigProperties.getBaseUrl())
                .build();
        this.path = usersConfigProperties.getUserPath();
    }

    /**
     * Retrieves users from Github api
     *
     * @param page     the number of the page
     * @param pageSize the size of elements per page
     * @return {@link Mono<List>}
     */
    public Mono<List<GithubUserDto>> retrieveGithubUsers(long page, long pageSize) {
        log.info("Retrieving users from github");
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("since", page)
                        .queryParam("per_page", pageSize).build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    log.error("4xx error: {}", clientResponse.bodyToMono(String.class));
                    return Mono.empty();
                })
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    log.error("5xx error: {}", clientResponse.bodyToMono(String.class));
                    return Mono.empty();
                })
                .bodyToMono(new ParameterizedTypeReference<List<GithubUserDto>>() {
                });
    }

    /**
     * Retrieves user details upon the list of Github users
     *
     * @return {@link Flux<GithubUserRequestDto>}
     */
    public Mono<GithubUserDto> retrieveGithubUsersWithDetails(String login) {
        log.info("Retrieving user details");
        return webClient.get()
                .uri(path, login)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    log.error("4xx error: {}", clientResponse.bodyToMono(String.class));
                    return Mono.empty();
                })
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    log.error("5xx error: {}", clientResponse.bodyToMono(String.class));
                    return Mono.empty();
                })
                .bodyToMono(GithubUserDto.class)
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(5))
                        .filter(throwable -> {
                            log.info("5xx error, retrying to retrieve {} users details", login);
                            return ((WebClientResponseException) throwable).getStatusCode().is5xxServerError();
                        }));
    }
}
