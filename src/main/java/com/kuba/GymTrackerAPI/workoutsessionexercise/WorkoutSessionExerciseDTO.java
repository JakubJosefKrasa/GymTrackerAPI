package com.kuba.GymTrackerAPI.workoutsessionexercise;

import com.kuba.GymTrackerAPI.exercise.ExerciseSetDTO;

public record WorkoutSessionExerciseDTO(
        Long id,
        ExerciseSetDTO exercise
) {
}
