package com.polot.gym.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TrainerUsernameRequest {
    @NotNull
    private String username;
}
