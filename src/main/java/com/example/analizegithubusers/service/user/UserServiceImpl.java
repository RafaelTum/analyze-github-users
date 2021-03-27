package com.example.analizegithubusers.service.user;

import com.example.analizegithubusers.dto.UserDto;
import com.example.analizegithubusers.exception.NotFoundException;
import com.example.analizegithubusers.repository.UserRepository;
import lombok.AllArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MapperFacade mapper;

    @Transactional(readOnly = true)
    @Override
    public Flux<UserDto> getAllUsers() {
        return userRepository.findAll().map(user -> mapper.map(user, UserDto.class));
    }

    @Transactional
    @Override
    public Mono<UserDto> deleteUser(String id) {
        return userRepository.findById(id)
                .flatMap(existingUser -> userRepository.delete(existingUser)
                        .thenReturn(existingUser)
                        .map(user -> mapper.map(user, UserDto.class))
                        .switchIfEmpty(Mono.error(new NotFoundException())));
    }
}