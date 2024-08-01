package com.kuba.GymTrackerAPI.trainingPlan;

import com.kuba.GymTrackerAPI.pagination.PaginationDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/training-plans")
@RequiredArgsConstructor
public class TrainingPlanController {
    private final TrainingPlanService trainingPlanService;

    @GetMapping
    public ResponseEntity<PaginationDTO<TrainingPlanExercisesDTO>> getTrainingPlansByUser(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "5") int pageSize, Authentication authenticatedUser) {
        PaginationDTO<TrainingPlanExercisesDTO> trainingPlansByUser = trainingPlanService.getTrainingPlansByUser(pageNumber, pageSize, authenticatedUser);

        return new ResponseEntity<>(trainingPlansByUser, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainingPlanExercisesDTO> getTrainingPlanById(@PathVariable Long id, Authentication authenticatedUser) {
        TrainingPlanExercisesDTO trainingPlanById = trainingPlanService.getTrainingPlanById(id, authenticatedUser);

        return new ResponseEntity<>(trainingPlanById, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TrainingPlanExercisesDTO> createTrainingPlan(@Valid @RequestBody TrainingPlanRequest trainingPlanRequest, Authentication authenticatedUser) {
        TrainingPlanExercisesDTO trainingPlan = trainingPlanService.createTrainingPlan(trainingPlanRequest, authenticatedUser);

        return new ResponseEntity<>(trainingPlan, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrainingPlanById(@PathVariable Long id, Authentication authenticatedUser) {
        trainingPlanService.deleteTrainingPlanById(id, authenticatedUser);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TrainingPlanExercisesDTO> changeTrainingPlanName(@PathVariable Long id, @Valid @RequestBody TrainingPlanRequest trainingPlanRequest, Authentication authenticatedUser) {
        TrainingPlanExercisesDTO trainingPlan = trainingPlanService.changeTrainingPlanName(id, trainingPlanRequest, authenticatedUser);

        return new ResponseEntity<>(trainingPlan, HttpStatus.OK);
    }

    @PutMapping("/{trainingPlanId}/exercises/{exerciseId}")
    public ResponseEntity<TrainingPlanExercisesDTO> addExerciseInTrainingPlan(@PathVariable Long trainingPlanId, @PathVariable Long exerciseId, Authentication authenticatedUser) {
        TrainingPlanExercisesDTO trainingPlan = trainingPlanService.addExerciseInTrainingPlan(trainingPlanId, exerciseId, authenticatedUser);

        return new ResponseEntity<>(trainingPlan, HttpStatus.OK);
    }

    @DeleteMapping("/{trainingPlanId}/exercises/{exerciseId}")
    public ResponseEntity<Void> removeExerciseFromTrainingPlan(@PathVariable Long trainingPlanId, @PathVariable Long exerciseId, Authentication authenticatedUser) {
        trainingPlanService.removeExerciseFromTrainingPlan(trainingPlanId, exerciseId, authenticatedUser);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
