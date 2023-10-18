package com.polot.gym.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TraineeProfileResponse {
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String address;
    private Boolean isActive;
    private List<TrainerResponse> trainers;
}
