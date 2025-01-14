package com.kuba.gymtrackerapi.workoutsessionexercise.dto;

import com.kuba.gymtrackerapi.exercise.dto.ExerciseSetDTO;

public record WorkoutSessionExerciseDTO(
        Long id,
        ExerciseSetDTO exercise
) {
}
