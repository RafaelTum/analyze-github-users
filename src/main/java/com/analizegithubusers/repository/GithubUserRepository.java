package com.analizegithubusers.repository;

import com.analizegithubusers.model.GithubUser;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GithubUserRepository extends ReactiveMongoRepository<GithubUser, String>, GithubCustomRepository {

}
