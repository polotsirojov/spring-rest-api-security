package com.polot.gym.service;

import com.polot.gym.entity.Trainer;
import com.polot.gym.entity.User;
import com.polot.gym.payload.request.StatusRequest;
import com.polot.gym.payload.request.TrainerRegisterRequest;
import com.polot.gym.payload.request.TrainerUpdateProfileRequest;
import com.polot.gym.payload.request.TrainerUsernameRequest;
import com.polot.gym.payload.response.TrainerProfileResponse;
import com.polot.gym.payload.response.TrainerResponse;
import com.polot.gym.payload.response.TrainingResponse;
import com.polot.gym.payload.response.UsernamePasswordResponse;

import java.time.LocalDate;
import java.util.List;

public interface TrainerService {
    UsernamePasswordResponse register(TrainerRegisterRequest request);

    TrainerProfileResponse getProfile();

    TrainerProfileResponse updateProfile(TrainerUpdateProfileRequest request);

    List<TrainerResponse> getNotAssignedTrainers(String username, String password);

    List<Trainer> getTrainersByUsername(List<TrainerUsernameRequest> trainers);

    List<TrainingResponse> getTrainings(String username, String password, LocalDate periodFrom, LocalDate periodTo, String traineeName);

    Trainer getByUsername(String traineeUsername);

    User activate(Integer trainerId, StatusRequest request);

    User deActivate(Integer trainerId, StatusRequest request);
}
