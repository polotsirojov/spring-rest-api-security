package com.polot.gym.payload.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTraineeTrainersRequest {
    @NotNull
    private String traineeUsername;
    @NotEmpty
    private List<TrainerUsernameRequest> trainers;
}
