package com.kuba.gymtrackerapi.exercise;

import com.kuba.gymtrackerapi.workoutsessionexerciseset.SetDTO;

import java.util.List;

public record ExerciseSetDTO(
        Long id,
        String exerciseName,
        List<SetDTO> sets
) {
}
