package com.example.analizegithubusers.service;

import com.example.analizegithubusers.dto.AggregationResultDto;
import com.example.analizegithubusers.dto.GithubUserRequestDto;
import com.example.analizegithubusers.dto.GithubUserResponseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface GithubUserService {

    /**
     * Retrieve all github users
     *
     * @return {@link Flux} emitting all users from database
     */
    Flux<GithubUserResponseDto> getAll();

    /**
     * Retrieve github users by pagination
     *
     * @return {@link Flux} emitting all users from database
     */
    Flux<GithubUserResponseDto> getAllByPagination(long page, long size);

    /**
     * Search users by username that partially matching
     *
     * @param login github user login
     * @return {@link Flux} emitting all users matching from database
     */
    Flux<GithubUserResponseDto> findByUsernamePartiallyMatching(String login);

    /**
     * Saves a list github users to the database
     *
     * @param githubUserList github user list to be saved
     * @return {@link Mono} emitting the saved github user.
     */
    Mono<Void> saveAll(List<GithubUserRequestDto> githubUserList);

    /**
     * Drops all the data from mongodb collection
     *
     * @return {@link Mono<Void>}
     */
    Mono<Void> deleteAll();

    /**
     * Finds and groups github users by location
     *
     * @return {@link Flux<AggregationResultDto>} flux of github users grouped by location
     */
    Flux<AggregationResultDto> findGroupedByLocation();

    /**
     * Finds and groups github users by company
     *
     * @return {@link Flux<AggregationResultDto>} flux of github users grouped by company
     */
    Flux<AggregationResultDto> findGroupedByCompany();
}
