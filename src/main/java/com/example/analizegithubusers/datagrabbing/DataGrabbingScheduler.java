package com.example.analizegithubusers.datagrabbing;

import com.example.analizegithubusers.config.properties.UsersConfigProperties;
import com.example.analizegithubusers.datagrabbing.dto.GithubUserDto;
import com.example.analizegithubusers.dto.GithubUserRequestDto;
import com.example.analizegithubusers.service.GithubUserService;
import com.example.analizegithubusers.util.DateParser;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * Scheduler class which makes periodic(every hour) calls to github api
 * for retrieving github users and storing them to database
 */
@EnableScheduling
@Configuration
@ConfigurationProperties(prefix = "app.data-fetch")
@Slf4j
public class DataGrabbingScheduler {

    private final GithubUsersGrabber githubUsersGrabber;
    private final GithubUserService githubUserService;
    private final MapperFacade mapperFacade;
    private final UsersConfigProperties usersConfigProperties;
    private final long pageSize;
    private final int startPage;
    private final int pageCount;

    public DataGrabbingScheduler(GithubUsersGrabber githubUsersGrabber,
                                 GithubUserService githubUserService,
                                 MapperFacade mapperFacade,
                                 UsersConfigProperties usersConfigProperties,
                                 @Value("${app.data-fetch.page-size}") long pageSize,
                                 @Value("${app.data-fetch.start}")int startPage,
                                 @Value("${app.data-fetch.count}")int pageCount) {
        this.githubUsersGrabber = githubUsersGrabber;
        this.githubUserService = githubUserService;
        this.mapperFacade = mapperFacade;
        this.usersConfigProperties = usersConfigProperties;
        this.pageSize = pageSize;
        this.startPage = startPage;
        this.pageCount = pageCount;
    }

    @Scheduled(fixedDelayString = "${app.data-fetch.fixed-delay}")
    public void run() {

        Flux.range(startPage, pageCount)
                .delayElements(Duration.ofSeconds(5))
                .flatMap(integer -> this.fetchData(integer * pageSize, pageSize))
                .collectList()
                .flatMap(githubUserRequestDtos -> githubUserService.deleteAll()
                        .thenReturn(githubUserRequestDtos))
                .flatMap(githubUserService::saveAll)
                .subscribe();
    }

    /**
     * Fetches the users data from Github
     *
     * @param page the page number
     * @param pageSize the number of elements per page
     * @return {@link Flux<GithubUserRequestDto>}
     */
    public Flux<GithubUserRequestDto> fetchData(long page, long pageSize) {
        return githubUsersGrabber.retrieveGithubUsers(page, pageSize)
                .flatMapMany(Flux::fromIterable)
                .flatMap(userDto -> githubUsersGrabber.retrieveGithubUsersWithDetails(userDto.getLogin()))
                .filter(this::passFilter)
                .map(userDto -> mapperFacade.map(userDto, GithubUserRequestDto.class))
                .onErrorContinue((err, userDto) -> log.info("error processing userDto {}", userDto));
    }

    /**
     * Filters github user upon values from properties file
     *
     * @param userDto user to be filtered
     * @return boolean
     */
    private boolean passFilter(GithubUserDto userDto) {
        return userDto.getPublicRepos() >= usersConfigProperties.getFilter().getPublicReposCount()
                && userDto.getFollowers() >= usersConfigProperties.getFilter().getFollowersCount()
                && DateParser.parse(userDto.getCreatedAt()).isAfter(usersConfigProperties.getFilter().getCreatedDate());
    }
}
