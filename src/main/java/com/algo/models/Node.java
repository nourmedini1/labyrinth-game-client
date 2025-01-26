package com.algo.models;

import com.algo.common.JsonMappable;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Node extends JsonMappable {

    private Coordinates coordinates;
    private boolean isWall;
    private char value;
    private List<Coordinates> neighbors;

}
