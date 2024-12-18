package com.kuba.gymtrackerapi.workoutsession;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kuba.gymtrackerapi.trainingplan.TrainingPlanWorkoutSessionExercisesDTO;

import java.time.LocalDate;

public record WorkoutSessionDTO(
        Long id,
        @JsonFormat(pattern = "dd/MM/yyyy") LocalDate date,
        TrainingPlanWorkoutSessionExercisesDTO trainingPlan
) {
}
