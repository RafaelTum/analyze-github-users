package com.analizegithubusers.repository;

import com.analizegithubusers.model.GithubUser;
import com.analizegithubusers.dto.AggregationResultDto;
import com.mongodb.BasicDBObject;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@AllArgsConstructor
public class GithubCustomRepositoryImpl implements GithubCustomRepository {

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Flux<GithubUser> findByUsernamePartiallyMatching(String githubUsername) {
        Query query = new Query(Criteria.where("login").regex(githubUsername));
        return reactiveMongoTemplate.find(query, GithubUser.class);
    }

    @Override
    public Flux<AggregationResultDto> findGroupedByCompany() {
        TypedAggregation<GithubUser> companyAggregation = Aggregation.newAggregation(GithubUser.class,
                Aggregation.group("company").
                        push(new BasicDBObject
                                ("company", "$company").append
                                ("url", "$url").append
                                ("login", "$login")).as("users"));
        return reactiveMongoTemplate.aggregate(companyAggregation, GithubUser.class, AggregationResultDto.class);
    }

    @Override
    public Flux<AggregationResultDto> findGroupedByLocation() {
        TypedAggregation<GithubUser> locationAggregation = Aggregation.newAggregation(GithubUser.class,
                Aggregation.group("location").
                        push(new BasicDBObject
                                ("location", "$location").append
                                ("url", "$url").append
                                ("login", "$login")).as("users"));
        return reactiveMongoTemplate.aggregate(locationAggregation, GithubUser.class, AggregationResultDto.class);
    }
}
