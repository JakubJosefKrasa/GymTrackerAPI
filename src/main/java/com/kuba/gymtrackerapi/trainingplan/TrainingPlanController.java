package com.kuba.gymtrackerapi.trainingplan;

import com.kuba.gymtrackerapi.pagination.PaginationDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/training-plans")
@RequiredArgsConstructor
public class TrainingPlanController {

    private final TrainingPlanService trainingPlanService;

    @GetMapping
    public ResponseEntity<PaginationDTO<TrainingPlanExercisesDTO>> getTrainingPlansByUser(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize
    ) {
        PaginationDTO<TrainingPlanExercisesDTO> trainingPlansByUser = trainingPlanService.getTrainingPlansByUser(
                pageNumber,
                pageSize
        );

        return new ResponseEntity<>(trainingPlansByUser, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainingPlanExercisesDTO> getTrainingPlanById(@PathVariable Long id) {
        TrainingPlanExercisesDTO trainingPlanById = trainingPlanService.getTrainingPlanById(id);

        return new ResponseEntity<>(trainingPlanById, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TrainingPlanExercisesDTO> createTrainingPlan(
            @Valid @RequestBody TrainingPlanRequestDTO trainingPlanRequest
    ) {
        TrainingPlanExercisesDTO trainingPlan = trainingPlanService.createTrainingPlan(trainingPlanRequest);

        return new ResponseEntity<>(trainingPlan, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrainingPlanById(@PathVariable Long id) {
        trainingPlanService.deleteTrainingPlanById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TrainingPlanExercisesDTO> changeTrainingPlanName(
            @PathVariable Long id,
            @Valid @RequestBody TrainingPlanRequestDTO trainingPlanRequest
    ) {
        TrainingPlanExercisesDTO trainingPlan = trainingPlanService.changeTrainingPlanName(id, trainingPlanRequest);

        return new ResponseEntity<>(trainingPlan, HttpStatus.OK);
    }

    @PutMapping("/{trainingPlanId}/exercises/{exerciseId}")
    public ResponseEntity<TrainingPlanExercisesDTO> addExerciseInTrainingPlan(
            @PathVariable Long trainingPlanId,
            @PathVariable Long exerciseId
    ) {
        TrainingPlanExercisesDTO trainingPlan = trainingPlanService.addExerciseInTrainingPlan(
                trainingPlanId,
                exerciseId
        );

        return new ResponseEntity<>(trainingPlan, HttpStatus.OK);
    }

    @DeleteMapping("/{trainingPlanId}/exercises/{exerciseId}")
    public ResponseEntity<Void> removeExerciseFromTrainingPlan(
            @PathVariable Long trainingPlanId,
            @PathVariable Long exerciseId
    ) {
        trainingPlanService.removeExerciseFromTrainingPlan(trainingPlanId, exerciseId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
