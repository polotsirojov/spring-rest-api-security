package com.polot.gym.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TraineeRegisterRequest {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    private LocalDate dob;
    private String address;
}
