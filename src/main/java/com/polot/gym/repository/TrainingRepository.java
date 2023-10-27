package com.polot.gym.repository;

import com.polot.gym.entity.Trainee;
import com.polot.gym.entity.Trainer;
import com.polot.gym.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Integer> {
    void deleteAllByTraineeAndTrainerIn(Trainee trainee, List<Trainer> trainers);
}
