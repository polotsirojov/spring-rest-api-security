package com.polot.gym.payload.response;

import com.polot.gym.entity.TrainingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrainingResponse {
    private String name;
    private LocalDate date;
    private TrainingType type;
    private Integer duration;
    private String trainerName;
    private String traineeName;
}
