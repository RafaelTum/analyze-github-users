package com.example.analizegithubusers.repository;

import com.example.analizegithubusers.dto.AggregationResultDto;
import com.example.analizegithubusers.model.GithubUser;
import reactor.core.publisher.Flux;

public interface GithubCustomRepository {

    /**
     * Searches users by username that partially matching
     *
     * @param githubUserLogin username to be searched
     * @return {@link Flux<GithubUser>}
     */
    Flux<GithubUser> findByUsernamePartiallyMatching(String githubUserLogin);

    /**
     * Finds and groups users from the same company
     *
     * @return {@link Flux<AggregationResultDto>}
     */
    Flux<AggregationResultDto> findGroupedByCompany();

    /**
     * Finds and groups users from the same location
     *
     * @return {@link Flux<AggregationResultDto>}
     */
    Flux<AggregationResultDto> findGroupedByLocation();

}
