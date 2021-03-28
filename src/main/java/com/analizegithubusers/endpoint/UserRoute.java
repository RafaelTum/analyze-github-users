package com.analizegithubusers.endpoint;

import com.analizegithubusers.dto.UserDto;
import com.analizegithubusers.handler.UserHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

/**
 * Functional router for users endpoint.
 * Handles all incoming request for /api/v1/users and delegates to handler {@link UserHandler}
 */
@Configuration
public class UserRoute {

    @RouterOperations({
            @RouterOperation(path = "/api/v1/users", method = RequestMethod.GET,
                    operation = @Operation(operationId = "allUsers", tags = "user", summary = "Get all users",
                            security = @SecurityRequirement(name = "bearerAuth"),
                            responses = @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDto.class)))))),
            @RouterOperation(path = "/api/v1/users/{id}", method = RequestMethod.DELETE,
                    operation = @Operation(operationId = "deleteUser", tags = "user", summary = "Delete user",
                            security = @SecurityRequirement(name = "bearerAuth"),
                            parameters = @Parameter(name = "id", in = ParameterIn.PATH, description = "Id of the user"),
                            responses = {
                                    @ApiResponse(responseCode = "204"),
                                    @ApiResponse(responseCode = "404", description = "User with given id not found")
                            }))
    })
    @Bean
    public RouterFunction<ServerResponse> userRouter(UserHandler handler) {
        return RouterFunctions
                .route(GET("/api/v1/users").and(accept(APPLICATION_JSON)), handler::getAllUsers)
                .andRoute(DELETE("/api/v1/users/{id}").and(accept(APPLICATION_JSON)), handler::deleteUser);
    }
}