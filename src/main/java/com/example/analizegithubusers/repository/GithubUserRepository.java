package com.example.analizegithubusers.repository;

import com.example.analizegithubusers.model.GithubUser;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GithubUserRepository extends ReactiveMongoRepository<GithubUser, String> {

}
