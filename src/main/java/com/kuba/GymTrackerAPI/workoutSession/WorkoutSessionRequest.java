package com.kuba.GymTrackerAPI.workoutSession;

import java.time.LocalDate;

public record WorkoutSessionRequest(
        LocalDate date,
        Long trainingPlanId
) {
}