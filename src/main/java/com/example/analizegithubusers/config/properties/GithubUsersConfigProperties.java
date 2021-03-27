package com.example.analizegithubusers.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Component
@ConfigurationProperties(prefix = "github.users")
@Validated
public class GithubUsersConfigProperties {
    @NotEmpty
    private String baseUrl;
    @NotEmpty
    private String userPath;
    @Valid
    private Filter filter;

    @Data
    public static class Filter {
        @NotNull
        @Min(value = 0)
        private long publicReposCount;
        @NotNull
        @Min(value = 0)
        private long followersCount;
        @NotNull
        private LocalDate createdDate;
    }
}
