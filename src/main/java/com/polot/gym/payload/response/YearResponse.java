package com.polot.gym.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class YearResponse {
    private Integer year;
    private List<MonthResponse> months;
}
