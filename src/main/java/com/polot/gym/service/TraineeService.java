package com.polot.gym.service;

import com.polot.gym.entity.Trainee;
import com.polot.gym.entity.User;
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

    Boolean deleteProfile(String username, String password);

    List<TrainingResponse> getTrainings(String username, String password, LocalDate periodFrom, LocalDate periodTo, String trainerName, Integer trainingTypeId);

    Trainee getByUsername(String traineeUsername);

    User activate(Integer traineeId,StatusRequest request);

    User deActivate(Integer traineeId,StatusRequest request);

    List<TrainerResponse> updateTraineeTrainers(UpdateTraineeTrainersRequest request);
}
