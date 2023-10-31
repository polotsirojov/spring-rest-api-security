package com.polot.gym.controller;

import com.polot.gym.payload.request.StatusRequest;
import com.polot.gym.payload.request.TrainerRegisterRequest;
import com.polot.gym.payload.request.TrainerUpdateProfileRequest;
import com.polot.gym.payload.response.TrainerProfileResponse;
import com.polot.gym.payload.response.TrainerResponse;
import com.polot.gym.payload.response.TrainingResponse;
import com.polot.gym.payload.response.UsernamePasswordResponse;
import com.polot.gym.service.TrainerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/trainer")
@RequiredArgsConstructor
public class TrainerController {
    private final TrainerService trainerService;

    @PostMapping
    public HttpEntity<UsernamePasswordResponse> register(@Valid @RequestBody TrainerRegisterRequest request) {
        return ResponseEntity.ok(trainerService.register(request));
    }

    @GetMapping
    @PreAuthorize(value = "hasRole('TRAINER')")
    public HttpEntity<TrainerProfileResponse> getProfile() {
        return ResponseEntity.ok(trainerService.getProfile());
    }

    @PutMapping
    public HttpEntity<TrainerProfileResponse> updateProfile(@Valid @RequestBody TrainerUpdateProfileRequest request) {
        return ResponseEntity.ok(trainerService.updateProfile(request));
    }

    @GetMapping("not-assigned")
    public HttpEntity<List<TrainerResponse>> getNotAssignedTrainers(@RequestParam String username, @RequestParam String password) {
        return ResponseEntity.ok(trainerService.getNotAssignedTrainers(username, password));
    }

    @GetMapping("trainings")
    public HttpEntity<List<TrainingResponse>> getTrainings(@RequestParam String username, @RequestParam String password,
                                                           @RequestParam(required = false) LocalDate periodFrom,
                                                           @RequestParam(required = false) LocalDate periodTo,
                                                           @RequestParam(required = false) String traineeName) {
        return ResponseEntity.ok(trainerService.getTrainings(username, password, periodFrom, periodTo, traineeName));
    }

    @PatchMapping("activate/{trainerId}")
    public HttpEntity<Void> activate(@PathVariable Integer trainerId, @Valid @RequestBody StatusRequest request) {
        trainerService.activate(trainerId, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("deactivate/{trainerId}")
    public HttpEntity<Void> deActivate(@PathVariable Integer trainerId, @Valid @RequestBody StatusRequest request) {
        trainerService.deActivate(trainerId, request);
        return ResponseEntity.ok().build();
    }

}
