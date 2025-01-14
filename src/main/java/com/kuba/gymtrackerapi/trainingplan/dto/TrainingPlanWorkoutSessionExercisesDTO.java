package com.kuba.gymtrackerapi.trainingplan.dto;

import com.kuba.gymtrackerapi.workoutsessionexercise.dto.WorkoutSessionExerciseDTO;
import java.util.Set;

public record TrainingPlanWorkoutSessionExercisesDTO(
        Long id,
        String trainingPlanName,
        Set<WorkoutSessionExerciseDTO> workoutSessionExercises
) {
}
