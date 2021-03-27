package com.example.analizegithubusers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResponseDto {
    private String id;
    private String username;
    private String name;
    private Set<String> roles;
}