package com.example.analizegithubusers.dto;

import lombok.Data;

@Data
public class SignUpRequestDto {
    private String username;
    private String password;
    private String name;
}