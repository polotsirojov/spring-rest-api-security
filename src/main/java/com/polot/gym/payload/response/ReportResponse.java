package com.polot.gym.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReportResponse {
    private String trainerUsername;
    private String trainerFirstname;
    private String trainerLastname;
    private Boolean status;
    private List<YearResponse> years;
}
