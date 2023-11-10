package com.polot.gym.controller;

import com.polot.gym.payload.request.StatusRequest;
import com.polot.gym.payload.request.TraineeProfileUpdateRequest;
import com.polot.gym.payload.request.TraineeRegisterRequest;
import com.polot.gym.payload.request.UpdateTraineeTrainersRequest;
import com.polot.gym.payload.response.TraineeProfileResponse;
import com.polot.gym.payload.response.TrainerResponse;
import com.polot.gym.payload.response.TrainingResponse;
import com.polot.gym.payload.response.UsernamePasswordResponse;
import com.polot.gym.service.TraineeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/trainee")
@RequiredArgsConstructor
@CrossOrigin(origins = "https://www.google.com")
public class TraineeController {
    private final TraineeService traineeService;

    @PostMapping
    public HttpEntity<UsernamePasswordResponse> register(@Valid @RequestBody TraineeRegisterRequest request) {
        return ResponseEntity.ok(traineeService.register(request));
    }

    @GetMapping
    @PreAuthorize(value = "hasRole('TRAINEE')")
    public HttpEntity<TraineeProfileResponse> getProfile() {
        return ResponseEntity.ok(traineeService.getProfile());
    }

    @PutMapping
    public HttpEntity<TraineeProfileResponse> updateProfile(@Valid @RequestBody TraineeProfileUpdateRequest request) {
        return ResponseEntity.ok(traineeService.updateProfile(request));
    }

    @DeleteMapping
    public HttpEntity<Void> deleteProfile(@RequestParam String username, @RequestParam String password) {
        traineeService.deleteProfile(username, password);
        return ResponseEntity.ok().build();
    }

    @GetMapping("trainings")
    public HttpEntity<List<TrainingResponse>> getTrainings(@RequestParam String username, @RequestParam String password,
                                                           @RequestParam(required = false) LocalDate periodFrom,
                                                           @RequestParam(required = false) LocalDate periodTo,
                                                           @RequestParam(required = false) String trainerName,
                                                           @RequestParam(required = false) Integer trainingTypeId) {
        return ResponseEntity.ok(traineeService.getTrainings(username, password, periodFrom, periodTo, trainerName, trainingTypeId));
    }

    @PatchMapping("activate/{traineeId}")
    public HttpEntity<Void> activate(@PathVariable Integer traineeId, @Valid @RequestBody StatusRequest request) {
        traineeService.activate(traineeId, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("deactivate/{traineeId}")
    public HttpEntity<Void> deActivate(@PathVariable Integer traineeId, @Valid @RequestBody StatusRequest request) {
        traineeService.deActivate(traineeId, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("update-trainers")
    public HttpEntity<List<TrainerResponse>> updateTraineeTrainers(@Valid @RequestBody UpdateTraineeTrainersRequest request) {
        return ResponseEntity.ok(traineeService.updateTraineeTrainers(request));
    }
}
