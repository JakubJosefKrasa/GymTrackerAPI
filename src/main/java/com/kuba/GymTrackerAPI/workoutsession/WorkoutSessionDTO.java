package com.kuba.GymTrackerAPI.workoutsession;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kuba.GymTrackerAPI.trainingplan.TrainingPlanWorkoutSessionExercisesDTO;

import java.time.LocalDate;

public record WorkoutSessionDTO(
        Long id,
        @JsonFormat(pattern = "dd/MM/yyyy") LocalDate date,
        TrainingPlanWorkoutSessionExercisesDTO trainingPlan
) {
}
