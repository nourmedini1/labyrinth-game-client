package com.algo.models;

import com.algo.common.JsonMappable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginRequest extends JsonMappable {
    private String name;
}


