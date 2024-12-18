package com.kuba.gymtrackerapi.trainingplan;

import com.kuba.gymtrackerapi.exercise.ExerciseDTO;

import java.util.Set;

public record TrainingPlanExercisesDTO(
        Long id,
        String trainingPlanName,
        Set<ExerciseDTO> exercises
) {
}
