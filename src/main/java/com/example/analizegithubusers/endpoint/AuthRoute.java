package com.example.analizegithubusers.endpoint;

import com.example.analizegithubusers.dto.SignInRequestDto;
import com.example.analizegithubusers.dto.SignInResponseDto;
import com.example.analizegithubusers.dto.SignUpRequestDto;
import com.example.analizegithubusers.dto.SignUpResponseDto;
import com.example.analizegithubusers.handler.AuthHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

/**
 * Functional router for authentication endpoint.
 * Handles all incoming request for /api/v1/auth and delegates to handler {@link AuthHandler}
 */
@Configuration
public class AuthRoute {

    @RouterOperations({
            @RouterOperation(path = "/api/v1/auth/signin", method = RequestMethod.POST,
                    operation = @Operation(operationId = "signin", tags = "auth", summary = "Sign in with username and password",
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = SignInRequestDto.class))),
                            responses = {
                                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = SignInResponseDto.class))),
                                    @ApiResponse(responseCode = "404", description = "User not found")
                            })),
            @RouterOperation(path = "/api/v1/auth/signup", method = RequestMethod.POST,
                    operation = @Operation(operationId = "signup", tags = "auth", summary = "Sign up as a user",
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = SignUpRequestDto.class))),
                            responses = {
                                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = SignUpResponseDto.class))),
                                    @ApiResponse(responseCode = "409", description = "User already exists")
                            }))
    })
    @Bean
    public RouterFunction<ServerResponse> authRouter(AuthHandler handler) {
        return RouterFunctions
                .route(POST("/api/v1/auth/signin").and(accept(APPLICATION_JSON)), handler::signIn)
                .andRoute(POST("/api/v1/auth/signup").and(accept(APPLICATION_JSON)), handler::signUp);
    }
}