package com.analizegithubusers.handler;

import com.analizegithubusers.dto.UserDto;
import com.analizegithubusers.exception.ExceptionTransformer;
import com.analizegithubusers.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@Slf4j
public class UserHandler {

    private final UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public Mono<ServerResponse> getAllUsers(ServerRequest request) {
        Flux<UserDto> allUsers = userService.getAllUsers();
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(allUsers, UserDto.class);
    }

    public Mono<ServerResponse> deleteUser(ServerRequest request) {
        String id = request.pathVariable("id");
        return userService.deleteUser(id)
                .flatMap(userDto -> ServerResponse.noContent().build())
                .onErrorResume(ExceptionTransformer::mapToServerResponse);
    }
}