package com.analizegithubusers.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GithubUserResponseDto {
    @NotNull
    private long id;

    private String login;

    private String url;

    private String name;

    private String company;

    private String location;

    private int publicRepos;

    private long followers;

    private String createdAt;
}
