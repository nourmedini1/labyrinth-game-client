package com.algo.models;

import com.algo.common.http.JsonMappable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Player extends JsonMappable {

    private String id;

    private String name;

    private int score;

}


