package com.analizegithubusers.repository;

import com.analizegithubusers.model.GithubUser;
import com.analizegithubusers.dto.AggregationResultDto;
import reactor.core.publisher.Flux;

public interface GithubCustomRepository {

    /**
     * Searches users by username that partially matching
     *
     * @param githubUsername username to be searched
     * @return {@link Flux< GithubUser >}
     */
    Flux<GithubUser> findByUsernamePartiallyMatching(String githubUsername);

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
