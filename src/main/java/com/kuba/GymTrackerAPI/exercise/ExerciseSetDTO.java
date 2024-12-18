package com.kuba.GymTrackerAPI.exercise;

import com.kuba.GymTrackerAPI.workoutsessionexerciseset.SetDTO;

import java.util.List;

public record ExerciseSetDTO(
        Long id,
        String exerciseName,
        List<SetDTO> sets
) {
}
