package com.polot.gym.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TraineeProfileUpdateRequest {
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    private LocalDate dob;
    private String address;
    @NotNull
    private Boolean isActive;
}
