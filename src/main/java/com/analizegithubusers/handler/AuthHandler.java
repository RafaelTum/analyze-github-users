package com.analizegithubusers.handler;

import com.analizegithubusers.dto.SignInRequestDto;
import com.analizegithubusers.dto.SignUpRequestDto;
import com.analizegithubusers.exception.ExceptionTransformer;
import com.analizegithubusers.service.auth.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@Slf4j
public class AuthHandler {

    private final AuthService authService;

    public AuthHandler(AuthService authService) {
        this.authService = authService;
    }

    public Mono<ServerResponse> signUp(ServerRequest request) {
        Mono<SignUpRequestDto> requestDtoMono = request.bodyToMono(SignUpRequestDto.class);
        return requestDtoMono.flatMap(authService::signUp)
                .flatMap(signUpResponseDto -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(signUpResponseDto))
                .onErrorResume(ExceptionTransformer::mapToServerResponse);
    }

    public Mono<ServerResponse> signIn(ServerRequest request) {
        Mono<SignInRequestDto> requestDtoMono = request.bodyToMono(SignInRequestDto.class);
        return requestDtoMono.flatMap(authService::signIn)
                .flatMap(signInResponseDto -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(signInResponseDto))
                .onErrorResume(ExceptionTransformer::mapToServerResponse);
    }

}