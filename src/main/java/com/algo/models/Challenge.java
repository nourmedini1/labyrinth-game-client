package com.algo.models;


import com.algo.common.JsonMappable;

import com.algo.common.ObjectMapperSingleton;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Challenge extends JsonMappable {

    private String id;
    private String labyrinthId;
    private int difficultyLevel;
    private String challengerId;
    private String theme;
    private LocalDateTime createdAt;

}

