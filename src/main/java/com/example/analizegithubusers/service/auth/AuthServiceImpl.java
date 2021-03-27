package com.example.analizegithubusers.service.auth;

import com.example.analizegithubusers.dto.SignInRequestDto;
import com.example.analizegithubusers.dto.SignInResponseDto;
import com.example.analizegithubusers.dto.SignUpRequestDto;
import com.example.analizegithubusers.dto.SignUpResponseDto;
import com.example.analizegithubusers.exception.DuplicationException;
import com.example.analizegithubusers.exception.BadCredentialsException;
import com.example.analizegithubusers.exception.NotFoundException;
import com.example.analizegithubusers.model.Role;
import com.example.analizegithubusers.model.User;
import com.example.analizegithubusers.repository.UserRepository;
import com.example.analizegithubusers.security.JwtUtils;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Objects;

@Service
public class AuthServiceImpl implements AuthService {

    @Value("${sec.jwt.token-prefix}")
    private String jwtTokenPrefix;

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final MapperFacade mapper;

    public AuthServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder,
                           JwtUtils jwtUtils, MapperFacade mapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.mapper = mapper;
    }

    @Transactional
    @Override
    public Mono<SignUpResponseDto> signUp(SignUpRequestDto signUpRequestDto) {
        return userRepository.findByUsername(signUpRequestDto.getUsername())
                .defaultIfEmpty(mapToUser(signUpRequestDto))
                .filter(user -> Objects.isNull(user.getId()))
                .flatMap(userRepository::save)
                .map(user -> mapper.map(user, SignUpResponseDto.class))
                .switchIfEmpty(Mono.error(new DuplicationException()));
    }

    @Transactional
    @Override
    public Mono<SignInResponseDto> signIn(SignInRequestDto signInRequestDto) {
        return userRepository.findByUsername(signInRequestDto.getUsername())
                .flatMap(existingUser -> isValidUser(signInRequestDto, existingUser) ?
                        Mono.just(new SignInResponseDto(jwtUtils.generateToken(existingUser), jwtTokenPrefix)) :
                        Mono.error(new BadCredentialsException()))
                .switchIfEmpty(Mono.error(new NotFoundException()));
    }

    private boolean isValidUser(SignInRequestDto signInRequestDto, User existingUser) {
        return passwordEncoder.matches(signInRequestDto.getPassword(), existingUser.getPassword());
    }

    private User mapToUser(SignUpRequestDto dto) {
        User user = mapper.map(dto, User.class);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRoles(Collections.singleton(new Role("USER", "Default role")));
        return user;
    }
}