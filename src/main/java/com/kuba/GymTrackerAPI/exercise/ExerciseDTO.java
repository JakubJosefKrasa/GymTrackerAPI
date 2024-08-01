package com.kuba.GymTrackerAPI.exercise;

import lombok.Builder;

@Builder
public record ExerciseDTO(
        Long id,
        String exerciseName
) {
}
