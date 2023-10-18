package com.polot.gym.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TraineeRegisterRequest {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    private LocalDate dob;
    private String address;
}
