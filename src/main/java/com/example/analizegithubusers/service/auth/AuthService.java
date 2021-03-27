package com.example.analizegithubusers.service.auth;


import com.example.analizegithubusers.dto.SignInRequestDto;
import com.example.analizegithubusers.dto.SignInResponseDto;
import com.example.analizegithubusers.dto.SignUpRequestDto;
import com.example.analizegithubusers.dto.SignUpResponseDto;
import com.example.analizegithubusers.exception.DuplicationException;
import com.example.analizegithubusers.exception.BadCredentialsException;
import com.example.analizegithubusers.exception.NotFoundException;
import reactor.core.publisher.Mono;

public interface AuthService {

    /**
     * Authenticates user based on credentials
     *
     * @param signInRequestDto object containing fields for username and password
     * @return object containing token and token type {@link SignInResponseDto}
     * @throws BadCredentialsException in case of incorrect credentials
     * @throws NotFoundException in case of username is not found
     */
    Mono<SignInResponseDto> signIn(SignInRequestDto signInRequestDto);

    /**
     * Registers user with role USER
     *
     * @param signUpRequestDto object containing necessary fields for user registration
     * @return persisted user {@link SignUpResponseDto}
     * @throws DuplicationException in case of duplicate username
     */
    Mono<SignUpResponseDto> signUp(SignUpRequestDto signUpRequestDto);
}