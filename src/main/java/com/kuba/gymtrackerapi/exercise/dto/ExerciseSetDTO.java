package com.kuba.gymtrackerapi.exercise.dto;

import com.kuba.gymtrackerapi.workoutsessionexerciseset.dto.WorkoutSessionExerciseSetDTO;
import java.util.Set;

public record ExerciseSetDTO(
        Long id,
        String exerciseName,
        Set<WorkoutSessionExerciseSetDTO> sets
) {
}
