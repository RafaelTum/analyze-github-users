package com.example.analizegithubusers.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@NoArgsConstructor
@Document(collection = "github_users")
public class GithubUser {

    @Id
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