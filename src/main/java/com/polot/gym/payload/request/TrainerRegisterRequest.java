package com.polot.gym.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TrainerRegisterRequest {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private Integer specializationId;
}
