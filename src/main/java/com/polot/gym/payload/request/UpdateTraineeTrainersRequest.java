package com.polot.gym.payload.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class UpdateTraineeTrainersRequest {
    @NotNull
    private String traineeUsername;
    @NotEmpty
    private List<TrainerUsernameRequest> trainers;
}
