package com.kuba.gymtrackerapi.workoutsession.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kuba.gymtrackerapi.trainingplan.dto.TrainingPlanDTO;
import java.time.LocalDate;

public record WorkoutSessionDTO(
        Long id,
        @JsonFormat(pattern = "dd/MM/yyyy") LocalDate date,
        TrainingPlanDTO trainingPlan

) {

}
