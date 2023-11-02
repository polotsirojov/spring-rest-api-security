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
import com.polot.gym.service.TrainerService;
import com.polot.gym.service.TrainingService;
import com.polot.gym.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainerServiceImpl implements TrainerService {
    private final UserService userService;
    private final TrainerRepository trainerRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final TrainingService trainingService;
    private final AuthService authService;
    private final UserSession userSession;
    private Logger log = LoggerFactory.getLogger(this.getClass());

    public TrainerServiceImpl(UserService userService,
                              @Lazy TrainerRepository trainerRepository,
                              TrainingTypeRepository trainingTypeRepository,
                              @Lazy TrainingService trainingService,
                              AuthService authService, UserSession userSession) {
        this.userService = userService;
        this.trainerRepository = trainerRepository;
        this.trainingTypeRepository = trainingTypeRepository;
        this.trainingService = trainingService;
        this.authService = authService;
        this.userSession = userSession;
    }

    @Override
    public UsernamePasswordResponse register(TrainerRegisterRequest request) {
        log.info("TrainerService register trainee method, TransactionId: {}, RequestBody {}", RequestContextHolder.getTransactionId(), request);
        TrainingType trainingType = trainingTypeRepository.findById(request.getSpecializationId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Training Type not found"));
        UserPasswordResponse user = userService.createUser(UserRequest.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build(), Role.TRAINER);
        log.info("Trainer user created. username: {}", user.getUser().getUsername());

        Trainer trainer = trainerRepository.save(Trainer.builder()
                .specialization(trainingType)
                .user(user.getUser())
                .build());
        log.info("Trainer has created. id: {}", trainer.getId());

        String accessToken = authService.authenticate(user.getUser().getUsername(), user.getPassword());
        log.info("Access token has created for trainer from trainer register method");

        return UsernamePasswordResponse.builder()
                .username(user.getUser().getUsername())
                .password(user.getPassword())
                .accessToken(accessToken)
                .build();
    }

    @Override
    public TrainerProfileResponse getProfile() {
        User user = userSession.getUser();
        log.info("TrainerService getProfile method username:{}, TransactionId: {}", user.getUsername(), RequestContextHolder.getTransactionId());
        Trainer trainer = trainerRepository.findByUser(user).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainer not found"));
        return mapTrainer(user, trainer);
    }

    @Override
    @Transactional
    public TrainerProfileResponse updateProfile(TrainerUpdateProfileRequest request) {
        log.info("TrainerService updateProfile method. data: {}, TransactionId: {}", request, RequestContextHolder.getTransactionId());
        TrainingType trainingType = trainingTypeRepository.findById(request.getSpecializationId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, " Training type not found"));
        User user = userService.selectByUsernameAndPassword(request.getUsername(), request.getPassword());
        userService.updateUser(user, request.getFirstName(), request.getLastName(), request.getIsActive());

        Trainer trainer = trainerRepository.findByUser(user).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainer not found"));
        trainer.setSpecialization(trainingType);
        trainerRepository.save(trainer);
        return mapTrainer(user, trainer);
    }

    @Override
    public List<TrainerResponse> getNotAssignedTrainers(String username, String password) {
        log.info("TrainerService getNotAssignedTrainers method username:{}, password:{}, TransactionId: {}", username, password, RequestContextHolder.getTransactionId());
        User user = userService.selectByUsernameAndPassword(username, password);
        List<Trainer> trainers = trainerRepository.getNotAssignedTrainers();
        return trainers.stream().map(trainer -> TrainerResponse.builder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .specialization(trainer.getSpecialization())
                .build()).collect(Collectors.toList());
    }

    @Override
    public List<Trainer> getTrainersByUsername(List<TrainerUsernameRequest> trainers) {
        return trainerRepository.findAllByUser_usernameIn(trainers.stream().map(TrainerUsernameRequest::getUsername).collect(Collectors.toList()));
    }

    @Override
    public List<TrainingResponse> getTrainings(String username, String password, LocalDate periodFrom, LocalDate periodTo, String traineeName) {
        log.info("TraineeService getTrainings username:{}, password:{}, periodFrom:{}, periodTo:{}, traineeName:{}, TransactionId: {}", username, password, periodFrom, periodTo, traineeName, RequestContextHolder.getTransactionId());
        User user = userService.selectByUsernameAndPassword(username, password);
        Trainer trainer = trainerRepository.findByUser(user).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainer not found"));
        return trainingService.getTrainerTrainings(trainer, periodFrom, periodTo, traineeName);
    }

    @Override
    public Trainer getByUsername(String traineeUsername) {
        log.info("TrainerService getByUsername. username:{}, TransactionId: {}", traineeUsername, RequestContextHolder.getTransactionId());
        User user = userService.selectByUsername(traineeUsername);
        return trainerRepository.findByUser(user).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainer not found"));
    }

    @Override
    public User activate(Integer trainerId, StatusRequest request) {
        log.info("TraineeService activate. data:{}, TransactionId: {}", request, RequestContextHolder.getTransactionId());
        userService.selectByUsernameAndPassword(request.getUsername(), request.getPassword());
        Trainer trainer = trainerRepository.findById(trainerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainer not found"));
        return userService.updateUserStatus(trainer.getUser(), true);
    }

    @Override
    public User deActivate(Integer trainerId, StatusRequest request) {
        log.info("TraineeService deActivate. data:{}, TransactionId: {}", request, RequestContextHolder.getTransactionId());
        userService.selectByUsernameAndPassword(request.getUsername(), request.getPassword());
        Trainer trainer = trainerRepository.findById(trainerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainer not found"));
        return userService.updateUserStatus(trainer.getUser(), false);
    }

    private TrainerProfileResponse mapTrainer(User user, Trainer trainer) {
        return TrainerProfileResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .specialization(trainer.getSpecialization())
                .isActive(user.getIsActive())
                .trainees(trainer.getTrainings().stream().map(training -> {
                    User trainerUser = training.getTrainer().getUser();
                    return TrainerResponse.builder()
                            .username(trainerUser.getUsername())
                            .firstName(trainerUser.getFirstName())
                            .lastName(trainerUser.getLastName())
                            .specialization(training.getTrainer().getSpecialization())
                            .build();
                }).collect(Collectors.toList()))
                .build();
    }
}
