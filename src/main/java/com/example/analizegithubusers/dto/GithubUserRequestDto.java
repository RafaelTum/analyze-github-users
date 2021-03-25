package com.example.analizegithubusers.dto;

import lombok.Data;

@Data
public class GithubUserRequestDto {

    private String login;

    private long id;

    private String url;

    private String name;

    private String company;

    private String location;

    private int publicRepos;

    private long followers;

    private String createdAt;
}
