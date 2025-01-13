package com.kuba.gymtrackerapi.trainingplan;

import com.kuba.gymtrackerapi.workoutsessionexercise.WorkoutSessionExerciseDTO;
import java.util.Set;

public record TrainingPlanWorkoutSessionExercisesDTO(
        Long id,
        String trainingPlanName,
        Set<WorkoutSessionExerciseDTO> workoutSessionExercises
) {
}
