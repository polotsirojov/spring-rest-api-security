package com.polot.gym.service.impl;

import com.polot.gym.config.RequestContextHolder;
import com.polot.gym.entity.Trainer;
import com.polot.gym.entity.TrainingType;
import com.polot.gym.entity.User;
import com.polot.gym.entity.enums.Role;
import com.polot.gym.payload.request.*;
import com.polot.gym.payload.response.*;
import com.polot.gym.repository.TrainerRepository;
import com.polot.gym.repository.TrainingTypeRepository;
import com.polot.gym.service.AuthService;
import com.polot.gym.service.TrainingService;
import com.polot.gym.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {
    @Mock
    private UserService userService;
    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private TrainingTypeRepository trainingTypeRepository;
    @Mock
    private TrainingService trainingService;
    @Mock
    private AuthService authService;
    @Mock
    private UserSession userSession;

    private User user;
    private User inActiveUser;
    private Trainer trainer;
    private TrainingType trainingType;
    private String password;
    private String username;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    @BeforeEach
    void setUp() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        RequestContextHolder.setRequest(request);
        trainerService = new TrainerServiceImpl(userService, trainerRepository, trainingTypeRepository, trainingService, authService, userSession);
        username = "username";
        password = "pass";
        user = User.builder()
                .id(1L)
                .firstName("firstName")
                .lastName("lastName")
                .username(username)
                .password(password)
                .isActive(true)
                .role(Role.TRAINER)
                .build();
        inActiveUser = User.builder()
                .id(1L)
                .firstName("firstName")
                .lastName("lastName")
                .username(username)
                .password(password)
                .isActive(false)
                .role(Role.TRAINER)
                .build();
        trainingType = new TrainingType(1L, "type");
        trainer = Trainer.builder()
                .id(1L)
                .specialization(trainingType)
                .user(user)
                .trainings(Collections.emptyList())
                .build();
    }

    @Test
    void register() {
        given(trainingTypeRepository.findById(1)).willReturn(Optional.of(trainingType));
        given(userService.createUser(new UserRequest("firstName", "lastName"), Role.TRAINER)).willReturn(new UserPasswordResponse(user, password));
        given(trainerRepository.save(any())).willReturn(trainer);
        given(authService.authenticate(username, password)).willReturn("token");
        UsernamePasswordResponse register = trainerService.register(new TrainerRegisterRequest("firstName", "lastName", 1));
        assertThat(register).isEqualTo(new UsernamePasswordResponse(username, password, "token", "Bearer"));
    }

    @Test
    void getProfile() {
        given(userSession.getUser()).willReturn(user);
        given(trainerRepository.findByUser(user)).willReturn(Optional.of(trainer));
        TrainerProfileResponse profile = trainerService.getProfile();
        assertThat(profile).isNotNull();
    }

    @Test
    void updateProfile() {
        given(trainingTypeRepository.findById(1)).willReturn(Optional.of(trainingType));
        given(userService.selectByUsernameAndPassword(username, password)).willReturn(user);
        given(userService.updateUser(user, "firstName", "lastName", true)).willReturn(user);
        given(trainerRepository.findByUser(user)).willReturn(Optional.of(trainer));
        given(trainerRepository.save(any())).willReturn(trainer);
        TrainerProfileResponse traineeProfileResponse = trainerService.updateProfile(new TrainerUpdateProfileRequest(username, password, "firstName", "lastName", 1, true));
        assertThat(traineeProfileResponse).isNotNull();
    }

    @Test
    void getNotAssignedTrainers() {
        given(userService.selectByUsernameAndPassword(username, password)).willReturn(user);
        given(trainerRepository.getNotAssignedTrainers()).willReturn(List.of(trainer));
        List<TrainerResponse> trainers = trainerService.getNotAssignedTrainers(username, password);
        assertThat(trainers.size()).isEqualTo(1);
    }

    @Test
    void getTrainersByUsername() {
        given(trainerRepository.findAllByUser_usernameIn(List.of(username))).willReturn(List.of(trainer));
        List<Trainer> trainers = trainerService.getTrainersByUsername(List.of(new TrainerUsernameRequest(username)));
        assertThat(trainers.size()).isEqualTo(1);
    }

    @Test
    void getTrainings() {
        given(userService.selectByUsernameAndPassword(username, password)).willReturn(user);
        given(trainerRepository.findByUser(user)).willReturn(Optional.of(trainer));
        given(trainingService.getTrainerTrainings(trainer, null, null, null)).willReturn(Collections.emptyList());

        List<TrainingResponse> trainings = trainerService.getTrainings(username, password, null, null, null);

        assertThat(trainings.size()).isEqualTo(0);
    }

    @Test
    void getByUsername() {
        given(userService.selectByUsername(username)).willReturn(user);
        given(trainerRepository.findByUser(user)).willReturn(Optional.of(trainer));
        Trainer trainer1 = trainerService.getByUsername(username);
        assertThat(trainer1).isEqualTo(trainer);
    }

    @Test
    void activate() {
        given(userService.selectByUsernameAndPassword(username, password)).willReturn(user);
        given(trainerRepository.findById(1)).willReturn(Optional.of(trainer));
        given(userService.updateUserStatus(user, true)).willReturn(user);
        User user1 = trainerService.activate(1,new StatusRequest(username, password, true));

        assertThat(user1.getIsActive()).isTrue();
    }

    @Test
    void deActivate() {
        given(userService.selectByUsernameAndPassword(username, password)).willReturn(user);
        given(trainerRepository.findById(1)).willReturn(Optional.of(trainer));
        given(userService.updateUserStatus(user, false)).willReturn(inActiveUser);
        User user1 = trainerService.deActivate(1,new StatusRequest(username, password, false));

        assertThat(user1.getIsActive()).isFalse();
    }
}