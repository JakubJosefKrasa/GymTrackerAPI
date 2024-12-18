package com.kuba.GymTrackerAPI.trainingplan;

import com.kuba.GymTrackerAPI.workoutSessionExercise.WorkoutSessionExerciseDTO;

import java.util.List;

public record TrainingPlanWorkoutSessionExercisesDTO(
        Long id,
        String trainingPlanName,
        List<WorkoutSessionExerciseDTO> workoutSessionExercises
) {
}
