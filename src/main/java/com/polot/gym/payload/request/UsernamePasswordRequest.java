package com.polot.gym.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UsernamePasswordRequest {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
