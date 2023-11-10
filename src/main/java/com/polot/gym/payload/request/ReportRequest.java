package com.polot.gym.payload.request;

import com.polot.gym.payload.constants.ReportType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportRequest {
    @NotNull
    private String trainerUsername;
    @NotNull
    private String trainerFirstname;
    @NotNull
    private String trainerLastname;
    @NotNull
    private Boolean isActive;
    @NotNull
    private LocalDate trainingDate;
    @NotNull
    private Integer trainingDuration;
    @NotNull
    private ReportType type;
}
