package com.algo.models;

import com.algo.common.http.JsonMappable;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateChallengeResponse extends JsonMappable {
    private String message;
}
