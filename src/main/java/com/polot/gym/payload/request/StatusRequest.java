package com.polot.gym.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusRequest {
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private Boolean isActive;
}
