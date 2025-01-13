package com.kuba.gymtrackerapi.trainingplan;

import com.kuba.gymtrackerapi.exercise.ExerciseDTO;
import java.util.List;

public record TrainingPlanExercisesDTO(
        Long id,
        String trainingPlanName,
        List<ExerciseDTO> exercises
) {
}
