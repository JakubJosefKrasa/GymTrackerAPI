package com.kuba.GymTrackerAPI.workoutSession;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record WorkoutSessionRequest(
        @NotNull(message = "Datum nesmí být prázdný!")
        LocalDate date,
        @NotNull(message = "Tréninkový plán nesmí být prázdný!")
        Long trainingPlanId
) {
}