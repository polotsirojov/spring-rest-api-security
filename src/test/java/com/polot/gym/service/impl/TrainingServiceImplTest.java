package com.polot.gym.service.impl;

import com.polot.gym.config.RequestContextHolder;
import com.polot.gym.entity.*;
import com.polot.gym.entity.enums.Role;
import com.polot.gym.payload.request.CreateTrainingRequest;
import com.polot.gym.payload.response.TrainingResponse;
import com.polot.gym.repository.TrainingRepository;
import com.polot.gym.repository.TrainingTypeRepository;
import com.polot.gym.service.TraineeService;
import com.polot.gym.service.TrainerService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TrainingServiceImplTest {
    @Mock
    private EntityManager entityManager;
    @Mock
    private TrainingRepository trainingRepository;
    @Mock
    private TraineeService traineeService;
    @Mock
    private TrainerService trainerService;
    @Mock
    private TrainingTypeRepository trainingTypeRepository;
    @InjectMocks
    private TrainingServiceImpl trainingService;

    private TrainingType trainingType;
    private String trainerUsername = "trainerUsername";
    private String traineeUsername = "traineeUsername";
    private String password = "pass";
    private Trainer trainer;
    private Trainee trainee;
    private User trainerUser;
    private User traineeUser;
    private Training training;


    @BeforeEach
    void setUp() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        RequestContextHolder.setRequest(request);
        trainingService = new TrainingServiceImpl(entityManager, trainingRepository, traineeService, trainerService, trainingTypeRepository);
        trainingType = new TrainingType(1L, "type");
        trainerUser = User.builder()
                .id(1L)
                .firstName("firstName")
                .lastName("lastName")
                .username(trainerUsername)
                .password(password)
                .isActive(true)
                .role(Role.TRAINER)
                .build();
        traineeUser = User.builder()
                .id(2L)
                .firstName("firstName")
                .lastName("lastName")
                .username(traineeUsername)
                .password(password)
                .isActive(true)
                .role(Role.TRAINEE)
                .build();
        trainer = Trainer.builder()
                .id(1L)
                .specialization(trainingType)
                .user(trainerUser)
                .trainings(Collections.emptyList())
                .build();
        trainee = Trainee.builder()
                .id(2L)
                .dob(LocalDate.now())
                .address("address")
                .user(traineeUser)
                .trainings(Collections.emptyList())
                .build();
        training = Training.builder()
                .trainee(trainee)
                .trainer(trainer)
                .trainingName("")
                .trainingDate(LocalDate.now())
                .trainingType(trainingType)
                .trainingDuration(1)
                .build();
    }

    @Test
    void getTraineeTrainings() {
        TypedQuery<Training> query = (TypedQuery<Training>) Mockito.mock(TypedQuery.class);
        Mockito.when(entityManager.createQuery("SELECT t FROM Training t WHERE t.trainee = :trainee ", Training.class)).thenReturn(query);
        given(query.getResultList()).willReturn(List.of(training));

        List<TrainingResponse> trainings = trainingService.getTraineeTrainings(trainee, null, null, null, null);
        assertThat(trainings.size()).isEqualTo(1);
    }

    @Test
    void getTrainerTrainings() {
        TypedQuery<Training> query = (TypedQuery<Training>) Mockito.mock(TypedQuery.class);
        Mockito.when(entityManager.createQuery("SELECT t FROM Training t WHERE t.trainer = :trainer ", Training.class)).thenReturn(query);
        given(query.getResultList()).willReturn(List.of(training));

        List<TrainingResponse> trainings = trainingService.getTrainerTrainings(trainer, null, null, null);
        assertThat(trainings.size()).isEqualTo(1);
    }

    @Test
    void create() {
        given(trainerService.getByUsername(trainerUsername)).willReturn(trainer);
        given(traineeService.getByUsername(traineeUsername)).willReturn(trainee);
        given(trainingRepository.save(any())).willReturn(training);
        Training training1 = trainingService.create(new CreateTrainingRequest(traineeUsername, trainerUsername, "", LocalDate.now(), 1));
        assertThat(training1).isNotNull();
    }

    @Test
    void getTrainingTypes() {
        given(trainingTypeRepository.findAll()).willReturn(List.of(trainingType));
        List<TrainingType> trainingTypes = trainingService.getTrainingTypes();
        assertThat(trainingTypes.size()).isEqualTo(1);
    }

    @Test
    void deleteTraineeTrainers(){
        Trainee trainee = new Trainee();
        List<Trainer> trainers = Arrays.asList(new Trainer(), new Trainer());
        trainingService.deleteTraineeTrainers(trainee,trainers);

        Mockito.verify(trainingRepository).deleteAllByTraineeAndTrainerIn(trainee, trainers);
        Mockito.verifyNoMoreInteractions(trainingRepository);
    }
}