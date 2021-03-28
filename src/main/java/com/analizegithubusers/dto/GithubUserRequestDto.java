package com.analizegithubusers.dto;

import lombok.Data;

@Data
public class GithubUserRequestDto {

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
