package com.polot.gym.service.impl;

import com.polot.gym.config.RequestContextHolder;
import com.polot.gym.entity.Trainee;
import com.polot.gym.entity.Trainer;
import com.polot.gym.entity.User;
import com.polot.gym.entity.enums.Role;
import com.polot.gym.payload.request.*;
import com.polot.gym.payload.response.*;
import com.polot.gym.repository.TraineeRepository;
import com.polot.gym.service.AuthService;
import com.polot.gym.service.TrainerService;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TraineeServiceImplTest {

    @Mock
    private TraineeRepository traineeRepository;
    @Mock
    private UserService userService;
    @Mock
    private TrainerService trainerService;
    @Mock
    private TrainingService trainingService;
    @Mock
    private AuthService authService;
    @Mock
    private UserSession userSession;

    private User user;
    private User inActiveUser;
    private Trainee trainee;
    private String password;
    private String username;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    @BeforeEach
    void setUp() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        RequestContextHolder.setRequest(request);
        traineeService = new TraineeServiceImpl(traineeRepository, userService, trainerService, trainingService, authService, userSession);
        username = "username";
        password = "pass";
        user = User.builder()
                .id(1L)
                .firstName("firstName")
                .lastName("lastName")
                .username(username)
                .password(password)
                .isActive(true)
                .role(Role.TRAINEE)
                .build();
        inActiveUser = User.builder()
                .id(1L)
                .firstName("firstName")
                .lastName("lastName")
                .username(username)
                .password(password)
                .isActive(false)
                .role(Role.TRAINEE)
                .build();
        trainee = Trainee.builder()
                .id(1L)
                .dob(LocalDate.now())
                .address("address")
                .user(user)
                .trainings(Collections.emptyList())
                .build();

    }

    @Test
    void register() {
        given(userService.createUser(new UserRequest("firstName", "lastName"), Role.TRAINEE)).willReturn(new UserPasswordResponse(user, password));
        given(traineeRepository.save(any())).willReturn(trainee);
        given(authService.authenticate(username, password)).willReturn("token");
        UsernamePasswordResponse register = traineeService.register(new TraineeRegisterRequest("firstName", "lastName", LocalDate.now(), "address"));
        assertThat(register).isEqualTo(new UsernamePasswordResponse(username, password, "token", "Bearer"));
    }

    @Test
    void getProfile() {
        given(userSession.getUser()).willReturn(user);
        given(traineeRepository.findByUser(user)).willReturn(Optional.of(trainee));
        TraineeProfileResponse profile = traineeService.getProfile();
        assertThat(profile).isNotNull();
    }

    @Test
    void updateProfile() {
        given(userService.selectByUsernameAndPassword(username, password)).willReturn(user);
        given(traineeRepository.findByUser(user)).willReturn(Optional.of(trainee));
        given(userService.updateUser(user, "firstName", "lastName", true)).willReturn(user);
        given(traineeRepository.save(any())).willReturn(trainee);
        TraineeProfileResponse traineeProfileResponse = traineeService.updateProfile(new TraineeProfileUpdateRequest(username, password, "firstName", "lastName", LocalDate.now(), "address", true));
        assertThat(traineeProfileResponse).isNotNull();
    }

    @Test
    void deleteProfile() {
        given(userService.selectByUsernameAndPassword(username, password)).willReturn(user);
        given(traineeRepository.findByUser(user)).willReturn(Optional.of(trainee));
        traineeRepository.delete(trainee);
        verify(traineeRepository, times(1)).delete(trainee);
        userService.deleteUser(user);
        verify(userService, times(1)).deleteUser(user);

        Boolean value = traineeService.deleteProfile(username, password);
        assertThat(value).isTrue();
    }

    @Test
    void getTrainings() {
        given(userService.selectByUsernameAndPassword(username, password)).willReturn(user);
        given(traineeRepository.findByUser(user)).willReturn(Optional.of(trainee));
        given(trainingService.getTraineeTrainings(trainee, null, null, null, null)).willReturn(Collections.emptyList());

        List<TrainingResponse> trainings = traineeService.getTrainings(username, password, null, null, null, null);

        assertThat(trainings.size()).isEqualTo(0);
    }

    @Test
    void getByUsername() {
        given(userService.selectByUsername(username)).willReturn(user);
        given(traineeRepository.findByUser(user)).willReturn(Optional.of(trainee));
        Trainee trainee1 = traineeService.getByUsername(username);
        assertThat(trainee1).isEqualTo(trainee);
    }

    @Test
    void activate() {
        given(userService.selectByUsernameAndPassword(username, password)).willReturn(user);
        given(traineeRepository.findById(1)).willReturn(Optional.of(trainee));
        given(userService.updateUserStatus(user, true)).willReturn(user);
        User user1 = traineeService.activate(1, new StatusRequest(username, password, true));

        assertThat(user1.getIsActive()).isTrue();
    }

    @Test
    void deactivate() {
        given(userService.selectByUsernameAndPassword(username, password)).willReturn(user);
        given(traineeRepository.findById(1)).willReturn(Optional.of(trainee));
        given(userService.updateUserStatus(user, false)).willReturn(inActiveUser);
        User user1 = traineeService.deActivate(1, new StatusRequest(username, password, false));

        assertThat(user1.getIsActive()).isFalse();
    }

    @Test
    void updateTraineeTrainers() {
        List<Trainer> trainerList = new ArrayList<>();
        Trainer trainer1 = new Trainer();
        Trainer trainer2 = new Trainer();
        trainerList.add(trainer1);
        trainerList.add(trainer2);
        given(userService.selectByUsername(username)).willReturn(user);
        given(traineeRepository.findByUser(user)).willReturn(Optional.of(trainee));
        given(trainerService.getTrainersByUsername(anyList())).willReturn(trainerList);
        List<TrainerResponse> response = traineeService.updateTraineeTrainers(new UpdateTraineeTrainersRequest(username, List.of(new TrainerUsernameRequest("username"))));
        verify(trainingService).deleteTraineeTrainers(trainee, trainerList);
        assertThat(response).isNotNull();
    }
}