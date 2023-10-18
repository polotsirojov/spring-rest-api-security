package com.polot.gym.payload.response;

import com.polot.gym.entity.TrainingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrainerProfileResponse {
    private String firstName;
    private String lastName;
    private TrainingType specialization;
    private Boolean isActive;
    private List<TrainerResponse> trainees;
}
