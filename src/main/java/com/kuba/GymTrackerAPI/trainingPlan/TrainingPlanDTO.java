package com.kuba.GymTrackerAPI.trainingPlan;

import com.kuba.GymTrackerAPI.exercise.ExerciseDTO;

import java.util.Set;

public record TrainingPlanDTO(
        Long id,
        String trainingPlanName,
        Long userId,
        Set<ExerciseDTO> exercises
) {
}
