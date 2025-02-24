package com.algo.models;

import com.algo.common.http.JsonMappable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateChallengeRequest extends JsonMappable {

    private String challengerId;


    private String challengedId;


    private int difficultyLevel;


    private String theme;
}

