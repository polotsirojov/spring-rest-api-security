package com.polot.gym.repository;

import com.polot.gym.entity.Trainee;
import com.polot.gym.entity.User;
import com.polot.gym.entity.enums.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

@DataJpaTest
class TraineeRepositoryTest {

    @Autowired
    private TraineeRepository traineeRepository;
    @Autowired
    private UserRepository userRepository;
    private User user;

    @BeforeEach
    public void beforeEach() {
        user = userRepository.save(User.builder()
                .firstName("")
                .lastName("")
                .username("login")
                .password("password")
                .isActive(true)
                .role(Role.TRAINEE)
                .build());
    }

    @Test
    void findByUser() {
        traineeRepository.save(Trainee.builder()
                .user(user)
                .dob(LocalDate.now())
                .address("")
                .build());
        Optional<Trainee> traineeOptional = traineeRepository.findByUser(user);
        Assertions.assertThat(traineeOptional).isPresent();
    }
}