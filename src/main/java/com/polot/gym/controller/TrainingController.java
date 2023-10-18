package com.polot.gym.controller;

import com.polot.gym.entity.TrainingType;
import com.polot.gym.payload.request.CreateTrainingRequest;
import com.polot.gym.service.TrainingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/training")
@RequiredArgsConstructor
public class TrainingController {
    private final TrainingService trainingService;

    @PostMapping
    public HttpEntity<Void> create(@Valid @RequestBody CreateTrainingRequest request){
        trainingService.create(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("types")
    public HttpEntity<List<TrainingType>> getTrainingTypes(){
        return ResponseEntity.ok(trainingService.getTrainingTypes());
    }
}
