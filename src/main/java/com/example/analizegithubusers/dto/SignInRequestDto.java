package com.example.analizegithubusers.dto;

import lombok.Data;

@Data
public class SignInRequestDto {
    private String username;
    private String password;
}