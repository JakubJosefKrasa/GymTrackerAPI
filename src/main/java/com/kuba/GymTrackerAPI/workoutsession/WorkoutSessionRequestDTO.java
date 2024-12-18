package com.kuba.GymTrackerAPI.workoutsession;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record WorkoutSessionRequestDTO(

        @NotNull(message = "Datum nesmí být prázdný!")
        LocalDate date,

        @NotNull(message = "Tréninkový plán nesmí být prázdný!")
        Long trainingPlanId
) {
}