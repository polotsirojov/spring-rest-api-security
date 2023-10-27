package com.polot.gym.repository;

import com.polot.gym.entity.Trainer;
import com.polot.gym.entity.TrainingType;
import com.polot.gym.entity.User;
import com.polot.gym.entity.enums.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TrainerRepositoryTest {
    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private TrainingTypeRepository trainingTypeRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.builder()
                .firstName("")
                .lastName("")
                .username("login")
                .password("password")
                .isActive(true)
                .role(Role.TRAINER)
                .build());

        TrainingType trainingType = trainingTypeRepository.save(TrainingType.builder().name("").build());
        trainerRepository.save(Trainer.builder()
                .specialization(trainingType)
                .user(user)
                .build());
    }

    @Test
    void findByUser() {
        Optional<Trainer> trainerOptional = trainerRepository.findByUser(user);
        Assertions.assertThat(trainerOptional).isPresent();
    }

    @Test
    void getNotAssignedTrainers() {
        List<Trainer> trainers = trainerRepository.getNotAssignedTrainers();
        Assertions.assertThat(trainers.size()).isEqualTo(1);
    }

    @Test
    void findAllByUser_usernameIn() {
        List<Trainer> trainers = trainerRepository.findAllByUser_usernameIn(List.of("login"));
        Assertions.assertThat(trainers.size()).isEqualTo(1);
    }
}