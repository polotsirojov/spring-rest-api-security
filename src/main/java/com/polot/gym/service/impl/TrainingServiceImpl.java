package com.polot.gym.service.impl;

import com.polot.gym.client.report.ReportServiceClient;
import com.polot.gym.config.RequestContextHolder;
import com.polot.gym.entity.Trainee;
import com.polot.gym.entity.Trainer;
import com.polot.gym.entity.Training;
import com.polot.gym.entity.TrainingType;
import com.polot.gym.payload.constants.ReportType;
import com.polot.gym.payload.request.CreateTrainingRequest;
import com.polot.gym.payload.request.ReportRequest;
import com.polot.gym.payload.response.TrainingResponse;
import com.polot.gym.repository.TrainingRepository;
import com.polot.gym.repository.TrainingTypeRepository;
import com.polot.gym.service.TraineeService;
import com.polot.gym.service.TrainerService;
import com.polot.gym.service.TrainingService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingServiceImpl implements TrainingService {
    private final EntityManager entityManager;
    private final TrainingRepository trainingRepository;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingTypeRepository trainingTypeRepository;
    private final ReportServiceClient reportServiceClient;
    private Logger log = LoggerFactory.getLogger(this.getClass());

    public TrainingServiceImpl(EntityManager entityManager, TrainingRepository trainingRepository, @Lazy TraineeService traineeService, @Lazy TrainerService trainerService, TrainingTypeRepository trainingTypeRepository, ReportServiceClient reportServiceClient) {
        this.entityManager = entityManager;
        this.trainingRepository = trainingRepository;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingTypeRepository = trainingTypeRepository;
        this.reportServiceClient = reportServiceClient;
    }

    @Override
    public List<TrainingResponse> getTraineeTrainings(Trainee trainee, LocalDate periodFrom, LocalDate periodTo, String trainerName, Integer trainingTypeId) {
        log.info("TrainingService getTraineeTrainings traineeId:{}, periodFrom:{}, periodTo:{}, trainerName:{}, trainingTypeId:{}, TransactionId: {}", trainee.getId(), periodFrom, periodTo, trainerName, trainingTypeId, RequestContextHolder.getTransactionId());
        StringBuilder jpql = new StringBuilder("SELECT t FROM Training t WHERE t.trainee = :trainee ");
        if (periodFrom != null) {
            jpql.append("and t.trainingDate >= :periodFrom ");
        }
        if (periodTo != null) {
            jpql.append("and t.trainingDate <= :periodTo ");
        }
        if (trainerName != null) {
            jpql.append("and t.trainer.user.firstName=:trainerName ");
        }
        if (trainingTypeId != null) {
            jpql.append("and t.trainingType.id=:trainingTypeId");
        }
        TypedQuery<Training> query = entityManager.createQuery(jpql.toString(), Training.class);
        query.setParameter("trainee", trainee);
        if (periodFrom != null) {
            query.setParameter("periodFrom", periodFrom);
        }
        if (periodTo != null) {
            query.setParameter("periodTo", periodTo);
        }
        if (trainerName != null) {
            query.setParameter("trainerName", trainerName);
        }
        if (trainingTypeId != null) {
            query.setParameter("trainingTypeId", trainingTypeId);
        }
        List<Training> resultList = query.getResultList();
        return resultList.stream().map(training -> TrainingResponse.builder()
                .name(training.getTrainingName())
                .date(training.getTrainingDate())
                .type(training.getTrainingType())
                .duration(training.getTrainingDuration())
                .trainerName(training.getTrainer().getUser().getFirstName() + " " + training.getTrainer().getUser().getLastName())
                .build()).collect(Collectors.toList());
    }

    @Override
    public List<TrainingResponse> getTrainerTrainings(Trainer trainer, LocalDate periodFrom, LocalDate periodTo, String traineeName) {
        log.info("TrainingService getTrainerTrainings trainerId:{}, periodFrom:{}, periodTo:{}, traineeName:{}, TransactionId: {}", trainer.getId(), periodFrom, periodTo, traineeName, RequestContextHolder.getTransactionId());
        StringBuilder jpql = new StringBuilder("SELECT t FROM Training t WHERE t.trainer = :trainer ");
        if (periodFrom != null) {
            jpql.append("and t.trainingDate >= :periodFrom ");
        }
        if (periodTo != null) {
            jpql.append("and t.trainingDate <= :periodTo ");
        }
        if (traineeName != null) {
            jpql.append("and t.trainee.user.firstName=:traineeName ");
        }
        TypedQuery<Training> query = entityManager.createQuery(jpql.toString(), Training.class);
        query.setParameter("trainer", trainer);
        if (periodFrom != null) {
            query.setParameter("periodFrom", periodFrom);
        }
        if (periodTo != null) {
            query.setParameter("periodTo", periodTo);
        }
        if (traineeName != null) {
            query.setParameter("traineeName", traineeName);
        }

        List<Training> resultList = query.getResultList();
        return resultList.stream().map(training -> TrainingResponse.builder()
                .name(training.getTrainingName())
                .date(training.getTrainingDate())
                .type(training.getTrainingType())
                .duration(training.getTrainingDuration())
                .traineeName(training.getTrainee().getUser().getFirstName() + " " + training.getTrainee().getUser().getLastName())
                .build()).collect(Collectors.toList());
    }

    @Override
    public Training create(CreateTrainingRequest request) {
        log.info("TrainingService create data:{}, TransactionId: {}", request, RequestContextHolder.getTransactionId());
        Trainer trainer = trainerService.getByUsername(request.getTrainerUsername());
        Training training = trainingRepository.save(Training.builder()
                .trainee(traineeService.getByUsername(request.getTraineeUsername()))
                .trainer(trainer)
                .trainingName(request.getTrainingName())
                .trainingDate(request.getTrainingDate())
                .trainingType(trainer.getSpecialization())
                .trainingDuration(request.getTrainingDuration())
                .build());
        reportServiceClient.postReport(new ReportRequest(
                training.getTrainer().getUser().getUsername(),
                training.getTrainer().getUser().getFirstName(),
                training.getTrainer().getUser().getLastName(),
                training.getTrainer().getUser().getIsActive(),
                training.getTrainingDate(),
                training.getTrainingDuration(),
                ReportType.ADD
        ));
        return training;
    }

    @Override
    public List<TrainingType> getTrainingTypes() {
        log.info("getTrainingTypes TransactionId: {}", RequestContextHolder.getTransactionId());
        return trainingTypeRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteTraineeTrainers(Trainee trainee, List<Trainer> trainers) {
        log.info("deleteTraineeTrainers TransactionId: {}", RequestContextHolder.getTransactionId());
        List<Training> list = trainingRepository.findAllByTraineeAndTrainerIn(trainee, trainers);
        for (Training training : list) {
            reportServiceClient.postReport(new ReportRequest(
                    training.getTrainer().getUser().getUsername(),
                    training.getTrainer().getUser().getFirstName(),
                    training.getTrainer().getUser().getLastName(),
                    training.getTrainer().getUser().getIsActive(),
                    training.getTrainingDate(),
                    training.getTrainingDuration(),
                    ReportType.DELETE
            ));
        }
        trainingRepository.deleteAllByTraineeAndTrainerIn(trainee, trainers);
    }
}
