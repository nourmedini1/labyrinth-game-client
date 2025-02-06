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
public class CreatePlayerRequest extends JsonMappable {
    private String name;
}


