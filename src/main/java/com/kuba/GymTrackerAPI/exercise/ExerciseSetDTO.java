package com.kuba.GymTrackerAPI.exercise;

import com.kuba.GymTrackerAPI.workoutSessionExerciseSet.SetDTO;

import java.util.List;

public record ExerciseSetDTO(
        Long id,
        String exerciseName,
        List<SetDTO> sets
) {
}
