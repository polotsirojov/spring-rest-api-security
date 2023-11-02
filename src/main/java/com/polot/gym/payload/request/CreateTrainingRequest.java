package com.polot.gym.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTrainingRequest {
    @NotNull
    private String traineeUsername;
    @NotNull
    private String trainerUsername;
    @NotNull
    private String trainingName;
    @NotNull
    private LocalDate trainingDate;
    @NotNull
    private Integer trainingDuration;
}
