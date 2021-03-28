package com.analizegithubusers.service;

import com.analizegithubusers.dto.AggregationResultDto;
import com.analizegithubusers.model.GithubUser;
import com.analizegithubusers.repository.GithubUserRepository;
import com.analizegithubusers.dto.GithubUserRequestDto;
import com.analizegithubusers.dto.GithubUserResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class GithubUserServiceImpl implements GithubUserService {

    private final GithubUserRepository githubUserRepository;
    private final MapperFacade mapperFacade;

    @Transactional(readOnly = true)
    @Override
    public Flux<GithubUserResponseDto> getAll() {
        return githubUserRepository.findAll()
                .map(githubUser -> mapperFacade.map(githubUser, GithubUserResponseDto.class));
    }

    @Transactional(readOnly = true)
    @Override
    public Flux<GithubUserResponseDto> getAllByPagination(long page, long size) {
        long elementsToBeSkipped = (page - 1) * size;
        if (page <= 0 || size < 0) {
            log.error("wrong values passed, page {}, size {}", page, size);
            return Flux.error(() -> new IllegalArgumentException("wrong values passed for pagination"));
        }
        return githubUserRepository.findAll()
                .skip(elementsToBeSkipped)
                .take(size)
                .map(githubUser -> mapperFacade.map(githubUser, GithubUserResponseDto.class));
    }

    @Transactional(readOnly = true)
    @Override
    public Flux<GithubUserResponseDto> findByUsernamePartiallyMatching(String githubUsername) {
        return githubUserRepository.findByUsernamePartiallyMatching(githubUsername)
                .map(githubUser -> mapperFacade.map(githubUser, GithubUserResponseDto.class));
    }

    @Transactional
    @Override
    public Mono<Void> saveAll(List<GithubUserRequestDto> githubUserList) {
        return Mono.just(githubUserList)
                .map(githubUserRequestDtos -> mapperFacade.mapAsList(githubUserRequestDtos, GithubUser.class))
                .flatMap(githubUsers -> githubUserRepository.saveAll(githubUsers).then());
    }

    @Transactional
    @Override
    public Mono<Void> deleteAll() {
        return githubUserRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Flux<AggregationResultDto> findGroupedByLocation() {
        return githubUserRepository.findGroupedByLocation();
    }

    @Transactional(readOnly = true)
    @Override
    public Flux<AggregationResultDto> findGroupedByCompany() {
        return githubUserRepository.findGroupedByCompany();
    }

}
