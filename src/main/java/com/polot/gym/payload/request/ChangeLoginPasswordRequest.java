package com.polot.gym.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeLoginPasswordRequest {
    @NotNull
    private String username;
    @NotNull
    private String oldPassword;
    @NotNull
    private String newPassword;
}
