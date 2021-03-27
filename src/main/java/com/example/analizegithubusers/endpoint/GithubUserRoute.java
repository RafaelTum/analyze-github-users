package com.example.analizegithubusers.endpoint;

import com.example.analizegithubusers.dto.AggregationResultDto;
import com.example.analizegithubusers.dto.GithubUserResponseDto;
import com.example.analizegithubusers.handler.GithubUserHandler;
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
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

/**
 * Functional router for Github users endpoint.
 * Handles all incoming request for /api/v1/github-users and delegates to handler {@link GithubUserHandler}
 */
@Configuration
public class GithubUserRoute {

    @RouterOperations({
            @RouterOperation(path = "/api/v1/github-users", method = RequestMethod.GET,
                    operation = @Operation(operationId = "allGithubUsers", tags = "githubUser", summary = "Get all githubUsers",
                            parameters = {
                                    @Parameter(name = "page", in = ParameterIn.QUERY, example = "5", content = @Content(schema = @Schema(implementation = String.class)), description = "the number of page"),
                                    @Parameter(name = "size", in = ParameterIn.QUERY, example = "10", content = @Content(schema = @Schema(implementation = String.class)), description = "the size of elements in one page")
                            },
                            security = @SecurityRequirement(name = "bearerAuth"),
                            responses = @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = GithubUserResponseDto.class)))))),
            @RouterOperation(path = "/api/v1/github-users/{login}", method = RequestMethod.GET,
                    operation = @Operation(operationId = "matchingGithubUsers",
                            parameters = @Parameter(name = "login", in = ParameterIn.PATH, description = "login or partial login to be searched"), tags = "githubUser", summary = "Search users by username that partially matching",
                            security = @SecurityRequirement(name = "bearerAuth"),
                            responses = @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = GithubUserResponseDto.class)))))),
            @RouterOperation(path = "/api/v1/github-users/group/location", method = RequestMethod.GET,
                    operation = @Operation(operationId = "githubUsersGroupedByLocation", tags = "githubUser", summary = "Find and group users by location",
                            security = @SecurityRequirement(name = "bearerAuth"),
                            responses = @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = AggregationResultDto.class)))))),
            @RouterOperation(path = "/api/v1/github-users/group/company", method = RequestMethod.GET,
                    operation = @Operation(operationId = "githubUsersGroupedByCompany", tags = "githubUser", summary = "Find and group users by company",
                            security = @SecurityRequirement(name = "bearerAuth"),
                            responses = @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = AggregationResultDto.class))))))
    })

    @Bean
    public RouterFunction<ServerResponse> exchangeRateRouter(GithubUserHandler handler) {
        return RouterFunctions
                .route(GET("/api/v1/github-users").and(accept(APPLICATION_JSON)), handler::getAll)
                .andRoute(GET("/api/v1/github-users/{login}").and(accept(APPLICATION_JSON)), handler::findByUsernamePartiallyMatching)
                .andRoute(GET("/api/v1/github-users/group/location").and(accept(APPLICATION_JSON)), handler::findGroupedByLocation)
                .andRoute(GET("/api/v1/github-users/group/company").and(accept(APPLICATION_JSON)), handler::findGroupedByCompany);
    }
}
