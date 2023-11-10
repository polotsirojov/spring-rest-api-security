package com.polot.gym.repository;

import com.polot.gym.entity.Trainee;
import com.polot.gym.entity.Trainer;
import com.polot.gym.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Integer> {
    List<Training> findAllByTraineeAndTrainerIn(Trainee trainee, List<Trainer> trainers);
    @Transactional
    @Modifying
    long deleteAllByTraineeAndTrainerIn(Trainee trainee, List<Trainer> trainers);
}
