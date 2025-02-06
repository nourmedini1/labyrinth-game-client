package com.algo.models;

import com.algo.common.http.JsonMappable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Labyrinth extends JsonMappable {

    private String id;

    private List<List<Node>> nodes;

    private Coordinates start;

    private Coordinates end;

    private int width;

    private int height;

    private List<Coordinates> shortestPath;

}


