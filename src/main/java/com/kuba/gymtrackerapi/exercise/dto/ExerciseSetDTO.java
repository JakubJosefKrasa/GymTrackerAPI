package com.kuba.gymtrackerapi.exercise.dto;

import com.kuba.gymtrackerapi.workoutsessionexerciseset.SetDTO;
import java.util.Set;

public record ExerciseSetDTO(
        Long id,
        String exerciseName,
        Set<SetDTO> sets
) {
}
