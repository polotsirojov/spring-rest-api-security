package com.polot.gym.payload.response;

import com.polot.gym.entity.TrainingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrainerResponse {
    private String username;
    private String firstName;
    private String lastName;
    private TrainingType specialization;
}
