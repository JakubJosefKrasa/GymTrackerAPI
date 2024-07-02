package com.kuba.GymTrackerAPI.exercise;

public record ExerciseDTO(
        Long id,
        String exerciseName,
        Long userId
) {
}
