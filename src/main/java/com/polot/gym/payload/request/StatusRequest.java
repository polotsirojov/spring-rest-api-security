package com.polot.gym.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StatusRequest {
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private Boolean isActive;
}
