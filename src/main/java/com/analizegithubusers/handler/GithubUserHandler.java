package com.analizegithubusers.handler;

import com.analizegithubusers.dto.AggregationResultDto;
import com.analizegithubusers.exception.ExceptionTransformer;
import com.analizegithubusers.dto.GithubUserResponseDto;
import com.analizegithubusers.service.GithubUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@Slf4j
@AllArgsConstructor
public class GithubUserHandler {

    private final GithubUserService githubUserService;

    public Mono<ServerResponse> getAll(ServerRequest request) {
        Optional<String> optionalPage = request.queryParam("page");
        Optional<String> optionalSize = request.queryParam("size");

        Flux<GithubUserResponseDto> githubUserResponseDtoFlux;
        if (optionalPage.isPresent() && optionalSize.isPresent()) {
            long size = Long.parseLong(optionalSize.get());
            long page = Long.parseLong(optionalPage.get());
            githubUserResponseDtoFlux = githubUserService.getAllByPagination(page, size);
        } else {
            githubUserResponseDtoFlux = githubUserService.getAll();
        }
        return githubUserResponseDtoFlux
                .collectList()
                .flatMap(dtoList -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(dtoList))
                .onErrorResume(ExceptionTransformer::mapToServerResponse);
    }

    public Mono<ServerResponse> findByUsernamePartiallyMatching(ServerRequest request) {
        String githubUsername = request.pathVariable("githubUsername");
        Flux<GithubUserResponseDto> allUsersMatching = githubUserService.findByUsernamePartiallyMatching(githubUsername);
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(allUsersMatching, GithubUserResponseDto.class);
    }

    public Mono<ServerResponse> findGroupedByLocation(ServerRequest request) {
        Flux<AggregationResultDto> allUsersMatching = githubUserService.findGroupedByLocation();
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(allUsersMatching, GithubUserResponseDto.class);
    }

    public Mono<ServerResponse> findGroupedByCompany(ServerRequest request) {
        Flux<AggregationResultDto> allUsersMatching = githubUserService.findGroupedByCompany();
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(allUsersMatching, GithubUserResponseDto.class);
    }
}