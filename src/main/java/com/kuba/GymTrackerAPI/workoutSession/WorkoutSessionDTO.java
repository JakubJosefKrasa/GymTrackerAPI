package com.kuba.GymTrackerAPI.workoutSession;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kuba.GymTrackerAPI.trainingPlan.TrainingPlanWorkoutSessionExercisesDTO;

import java.time.LocalDate;

public record WorkoutSessionDTO(
        Long id,
        @JsonFormat(pattern = "dd/MM/yyyy") LocalDate date,
        TrainingPlanWorkoutSessionExercisesDTO trainingPlan
) {
}
