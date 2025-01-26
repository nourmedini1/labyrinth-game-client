package com.algo.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateChallengeRequest {

    private String challengerId;


    private String challengedId;


    private int difficultyLevel;


    private String theme;
}

