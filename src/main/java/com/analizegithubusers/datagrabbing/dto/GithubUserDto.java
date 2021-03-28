package com.analizegithubusers.datagrabbing.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GithubUserDto {

    @JsonProperty("login")
    private String login;

    @JsonProperty("id")
    private long id;

    @JsonProperty("url")
    private String url;

    @JsonProperty("name")
    private String name;

    @JsonProperty("company")
    private String company;

    @JsonProperty("location")
    private String location;

    @JsonProperty("public_repos")
    private int publicRepos;

    @JsonProperty("followers")
    private long followers;

    @JsonProperty("created_at")
    private String createdAt;
}