package com.example.analizegithubusers.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
public class ExceptionTransformer {

    public static Mono<? extends ServerResponse> mapToServerResponse(Throwable ex) {
        if (ex instanceof IllegalArgumentException) {
            return ServerResponse.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE).build();
        } else if (ex instanceof BadCredentialsException) {
            return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
        } else if (ex instanceof NotFoundException) {
            return ServerResponse.notFound().build();
        } else if (ex instanceof DuplicationException) {
            return ServerResponse.status(HttpStatus.CONFLICT).build();
        }

        log.error("Unhandled error: {}", ex.getMessage(), ex);
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
