package com.kuba.GymTrackerAPI.trainingplan;

import com.kuba.GymTrackerAPI.exercise.ExerciseDTO;

import java.util.Set;

public record TrainingPlanExercisesDTO(
        Long id,
        String trainingPlanName,
        Set<ExerciseDTO> exercises
) {
}
