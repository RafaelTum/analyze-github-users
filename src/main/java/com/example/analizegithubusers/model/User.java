package com.example.analizegithubusers.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document
public class User {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    @Indexed(unique = true)
    @EqualsAndHashCode.Include
    private String username;

    private String password;
    private String name;

    private Set<Role> roles;
}