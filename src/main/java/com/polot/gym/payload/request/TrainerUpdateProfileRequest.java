package com.polot.gym.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TrainerUpdateProfileRequest {
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private Integer specializationId;
    @NotNull
    private Boolean isActive;
}
