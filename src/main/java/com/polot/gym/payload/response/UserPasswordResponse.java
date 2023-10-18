package com.polot.gym.payload.response;

import com.polot.gym.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPasswordResponse {
    private User user;
    private String password;
}
