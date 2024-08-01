package com.kuba.GymTrackerAPI.workoutSessionExerciseSet;

public record SetDTO(
        Long id,
        int repetitions,
        float weight
) {
}
