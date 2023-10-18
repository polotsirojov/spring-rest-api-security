package com.polot.gym.service;

import com.polot.gym.entity.Trainee;
import com.polot.gym.payload.request.*;
import com.polot.gym.payload.response.TraineeProfileResponse;
import com.polot.gym.payload.response.TrainerResponse;
import com.polot.gym.payload.response.TrainingResponse;
import com.polot.gym.payload.response.UsernamePasswordResponse;

import java.time.LocalDate;
import java.util.List;

public interface TraineeService {
    UsernamePasswordResponse register(TraineeRegisterRequest request);

    TraineeProfileResponse getProfile();

    TraineeProfileResponse updateProfile(TraineeProfileUpdateRequest request);

    void deleteProfile(String username, String password);

    List<TrainerResponse> updateTrainers(UpdateTraineeTrainersRequest request);

    List<TrainingResponse> getTrainings(String username, String password, LocalDate periodFrom, LocalDate periodTo, String trainerName, Integer trainingTypeId);

    Trainee getByUsername(String traineeUsername);

    void activeDeactive(StatusRequest request);
}