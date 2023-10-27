package com.polot.gym.service;

import com.polot.gym.entity.Trainee;
import com.polot.gym.entity.Trainer;
import com.polot.gym.entity.Training;
import com.polot.gym.entity.TrainingType;
import com.polot.gym.payload.request.CreateTrainingRequest;
import com.polot.gym.payload.response.TrainingResponse;

import java.time.LocalDate;
import java.util.List;

public interface TrainingService {
    List<TrainingResponse> getTraineeTrainings(Trainee trainee, LocalDate periodFrom, LocalDate periodTo, String trainerName, Integer trainingTypeId);
    List<TrainingResponse> getTrainerTrainings(Trainer trainer, LocalDate periodFrom, LocalDate periodTo, String traineeName);

    Training create(CreateTrainingRequest request);

    List<TrainingType> getTrainingTypes();

    void deleteTraineeTrainers(Trainee trainee, List<Trainer> trainers);
}
