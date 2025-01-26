package com.algo.models;

import com.algo.common.JsonMappable;

import com.algo.common.ObjectMapperSingleton;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Coordinates extends JsonMappable {
    private int x;
    private int y;

}
