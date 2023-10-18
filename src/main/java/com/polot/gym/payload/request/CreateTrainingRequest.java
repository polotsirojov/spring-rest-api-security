package com.polot.gym.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
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
