package com.kuba.GymTrackerAPI.workoutsessionexerciseset;

public record SetDTO(
        Long id,
        int repetitions,
        float weight
) {
}
