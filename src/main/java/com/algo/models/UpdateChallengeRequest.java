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
public class UpdateChallengeRequest extends JsonMappable {
    private int challengerScore;
    private int challengedScore;
}
