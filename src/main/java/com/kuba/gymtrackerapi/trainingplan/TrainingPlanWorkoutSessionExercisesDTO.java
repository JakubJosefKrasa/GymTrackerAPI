package com.kuba.gymtrackerapi.trainingplan;

import com.kuba.gymtrackerapi.workoutsessionexercise.WorkoutSessionExerciseDTO;

import java.util.List;

public record TrainingPlanWorkoutSessionExercisesDTO(
        Long id,
        String trainingPlanName,
        List<WorkoutSessionExerciseDTO> workoutSessionExercises
) {
}
