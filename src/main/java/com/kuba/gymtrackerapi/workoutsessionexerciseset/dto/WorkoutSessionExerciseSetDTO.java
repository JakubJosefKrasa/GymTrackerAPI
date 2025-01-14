package com.kuba.gymtrackerapi.workoutsessionexerciseset.dto;

public record WorkoutSessionExerciseSetDTO(
        Long id,
        int repetitions,
        float weight
) {
}
