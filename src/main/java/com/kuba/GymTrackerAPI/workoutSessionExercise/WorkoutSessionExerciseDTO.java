package com.kuba.GymTrackerAPI.workoutSessionExercise;

import com.kuba.GymTrackerAPI.exercise.ExerciseSetDTO;

public record WorkoutSessionExerciseDTO(
        Long id,
        ExerciseSetDTO exercise
) {
}
