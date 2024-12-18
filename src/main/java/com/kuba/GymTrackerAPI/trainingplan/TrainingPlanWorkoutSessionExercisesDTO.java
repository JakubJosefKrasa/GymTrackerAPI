package com.kuba.GymTrackerAPI.trainingplan;

import com.kuba.GymTrackerAPI.workoutsessionexercise.WorkoutSessionExerciseDTO;

import java.util.List;

public record TrainingPlanWorkoutSessionExercisesDTO(
        Long id,
        String trainingPlanName,
        List<WorkoutSessionExerciseDTO> workoutSessionExercises
) {
}
