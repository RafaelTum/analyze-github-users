package com.analizegithubusers.dto;

import lombok.Data;

import java.util.List;

@Data
public class AggregationResultDto {
    private List<String> users;
}