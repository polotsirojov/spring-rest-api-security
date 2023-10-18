package com.polot.gym.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsernamePasswordResponse {
    private String username;
    private String password;
    private String accessToken;
    @Builder.Default
    private String tokenType="Bearer";

}
