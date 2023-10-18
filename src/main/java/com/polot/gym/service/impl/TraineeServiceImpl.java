package com.polot.gym.service.impl;

import com.polot.gym.entity.Trainee;
import com.polot.gym.entity.Trainer;
import com.polot.gym.entity.User;
import com.polot.gym.entity.enums.Role;
import com.polot.gym.payload.request.*;
import com.polot.gym.payload.response.*;
import com.polot.gym.repository.TraineeRepository;
import com.polot.gym.service.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TraineeServiceImpl implements TraineeService {
    private final TraineeRepository traineeRepository;
    private final UserService userService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final AuthService authService;
    private final UserSession userSession;

    public TraineeServiceImpl(TraineeRepository traineeRepository,
                              UserService userService,
                              @Lazy TrainerService trainerService,
                              @Lazy TrainingService trainingService,
                              AuthService authService, UserSession userSession) {
        this.traineeRepository = traineeRepository;
        this.userService = userService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
        this.authService = authService;
        this.userSession = userSession;
    }

    @Override
    @Transactional
    public UsernamePasswordResponse register(TraineeRegisterRequest request) {
        UserPasswordResponse user = userService.createUser(UserRequest.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build(), Role.TRAINEE);

        traineeRepository.save(Trainee.builder()
                .dob(request.getDob())
                .address(request.getAddress())
                .user(user.getUser())
                .build());

        String accessToken = authService.authenticate(user.getUser().getUsername(), user.getPassword());

        return UsernamePasswordResponse.builder()
                .username(user.getUser().getUsername())
                .password(user.getPassword())
                .accessToken(accessToken)
                .build();
    }

    @Override
    public TraineeProfileResponse getProfile() {
        User user = userSession.getUser();
        Trainee trainee = traineeRepository.findByUser(user).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainee not found"));

        return mapProfile(user, trainee);
    }

    private TraineeProfileResponse mapProfile(User user, Trainee trainee) {
        return TraineeProfileResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dob(trainee.getDob())
                .address(trainee.getAddress())
                .isActive(user.getIsActive())
                .trainers(trainee.getTrainings().stream()
                        .map(training -> {
                            User trainerUser = training.getTrainer().getUser();
                            return TrainerResponse.builder()
                                    .username(trainerUser.getUsername())
                                    .firstName(trainerUser.getFirstName())
                                    .lastName(trainerUser.getLastName())
                                    .specialization(training.getTrainer().getSpecialization())
                                    .build();
                        })
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    @Transactional
    public TraineeProfileResponse updateProfile(TraineeProfileUpdateRequest request) {
        User user = userService.selectByUsernameAndPassword(request.getUsername(), request.getPassword());
        Trainee trainee = traineeRepository.findByUser(user).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainee not found"));
        userService.updateUser(user, request.getFirstName(), request.getLastName(), request.getIsActive());
        trainee.setDob(request.getDob());
        trainee.setAddress(request.getAddress());
        traineeRepository.save(trainee);
        return mapProfile(user, trainee);
    }

    @Override
    @Transactional
    public void deleteProfile(String username, String password) {
        User user = userService.selectByUsernameAndPassword(username, password);
        Trainee trainee = traineeRepository.findByUser(user).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainee not found"));
        traineeRepository.delete(trainee);
        userService.deleteUser(user);
    }

    @Override
    public List<TrainerResponse> updateTrainers(UpdateTraineeTrainersRequest request) {
        User user = userService.selectByUsername(request.getTraineeUsername());
        Trainee trainee = traineeRepository.findByUser(user).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainee not found"));
        List<Trainer> trainers = trainerService.getTrainersByUsername(request.getTrainers());
        return null;
    }

    @Override
    public List<TrainingResponse> getTrainings(String username, String password, LocalDate periodFrom, LocalDate periodTo, String trainerName, Integer trainingTypeId) {
        User user = userService.selectByUsernameAndPassword(username, password);
        Trainee trainee = traineeRepository.findByUser(user).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainee not found"));
        return trainingService.getTraineeTrainings(trainee, periodFrom, periodTo, trainerName, trainingTypeId);
    }

    @Override
    public Trainee getByUsername(String traineeUsername) {
        User user = userService.selectByUsername(traineeUsername);
        return traineeRepository.findByUser(user).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainee not found"));
    }

    @Override
    public void activeDeactive(StatusRequest request) {
        User user = userService.selectByUsernameAndPassword(request.getUsername(), request.getPassword());
        traineeRepository.findByUser(user).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainee not found"));
        userService.updateUserStatus(user, request.getIsActive());
    }
}
