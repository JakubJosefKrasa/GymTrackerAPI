package com.kuba.GymTrackerAPI.exercise;

import com.kuba.GymTrackerAPI.pagination.PaginationDTO;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/exercises")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;

    @GetMapping
    public ResponseEntity<PaginationDTO<ExerciseDTO>> getExercisesByUser(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize
    ) {
        PaginationDTO<ExerciseDTO> exercisesByUser = exerciseService.getExercisesByUser(pageNumber, pageSize);

        return new ResponseEntity<>(exercisesByUser, HttpStatus.OK);
    }

    @GetMapping("/not-in-training-plan/{trainingPlanId}")
    public ResponseEntity<List<ExerciseDTO>> getExercisesNotInTrainingPlan(@PathVariable Long trainingPlanId) {
        List<ExerciseDTO> exercises = exerciseService.getExercisesNotInTrainingPlan(trainingPlanId);

        return new ResponseEntity<>(exercises, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ExerciseDTO> createExercise(@Valid @RequestBody ExerciseRequestDTO exerciseRequest) {
        ExerciseDTO createdExercise = exerciseService.createExercise(exerciseRequest);

        return new ResponseEntity<>(createdExercise, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExerciseById(@PathVariable Long id) {
        exerciseService.deleteExerciseById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ExerciseDTO> changeExerciseName(
            @PathVariable Long id,
            @Valid @RequestBody ExerciseRequestDTO exerciseRequest
    ) {
        ExerciseDTO exercise = exerciseService.changeExerciseName(id, exerciseRequest);

        return new ResponseEntity<>(exercise, HttpStatus.OK);
    }
}
