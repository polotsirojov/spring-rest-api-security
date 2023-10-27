package com.polot.gym.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerRegisterRequest {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private Integer specializationId;
}
