package com.kuba.gymtrackerapi.workoutsessionexercise;

import com.kuba.gymtrackerapi.exercise.dto.ExerciseSetDTO;

public record WorkoutSessionExerciseDTO(
        Long id,
        ExerciseSetDTO exercise
) {
}
