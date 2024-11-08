package com.kuba.GymTrackerAPI.exercise;

import com.kuba.GymTrackerAPI.pagination.PaginationDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/exercises")
@RequiredArgsConstructor
public class ExerciseController {
    private final ExerciseService exerciseService;

    @GetMapping
    public ResponseEntity<PaginationDTO<ExerciseDTO>> getExercisesByUser(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "5") int pageSize, Authentication authenticatedUser) {
        PaginationDTO<ExerciseDTO> exercisesByUser = exerciseService.getExercisesByUser(pageNumber, pageSize, authenticatedUser);

        return new ResponseEntity<>(exercisesByUser, HttpStatus.OK);
    }

    @GetMapping("/not-in-training-plan/{trainingPlanId}")
    public ResponseEntity<List<ExerciseDTO>> getExercisesNotInTrainingPlan(@PathVariable Long trainingPlanId, Authentication authenticatedUser) {
        List<ExerciseDTO> exercises = exerciseService.getExercisesNotInTrainingPlan(trainingPlanId, authenticatedUser);

        return new ResponseEntity<>(exercises, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ExerciseDTO> createExercise(@Valid @RequestBody ExerciseRequest exerciseRequest, Authentication authenticatedUser) {
        ExerciseDTO createdExercise = exerciseService.createExercise(exerciseRequest, authenticatedUser);

        return new ResponseEntity<>(createdExercise, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExerciseById(@PathVariable Long id, Authentication authenticatedUser) {
        exerciseService.deleteExerciseById(id, authenticatedUser);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ExerciseDTO> changeExerciseName(@PathVariable Long id, @Valid @RequestBody ExerciseRequest exerciseRequest, Authentication authenticatedUser) {
        ExerciseDTO exercise = exerciseService.changeExerciseName(id, exerciseRequest, authenticatedUser);

        return new ResponseEntity<>(exercise, HttpStatus.OK);
    }
}
