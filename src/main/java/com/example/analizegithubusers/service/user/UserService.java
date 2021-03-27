package com.example.analizegithubusers.service.user;

import com.example.analizegithubusers.dto.UserDto;
import com.example.analizegithubusers.exception.NotFoundException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

    /**
     * Retrieve all users
     *
     * @return {@link Flux} emitting all users
     */
    Flux<UserDto> getAllUsers();

    /**
     * Deletes a given user
     *
     * @param id of the user to be deleted
     * @return empty {@link Mono} signaling when operation has completed.
     * @throws {@link NotFoundException} in case of given id not found
     */
    Mono<UserDto> deleteUser(String id);
}