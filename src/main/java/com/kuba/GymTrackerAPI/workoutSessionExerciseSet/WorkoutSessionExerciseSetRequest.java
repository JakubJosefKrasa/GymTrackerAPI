package com.kuba.GymTrackerAPI.workoutSessionExerciseSet;

import jakarta.validation.constraints.NotNull;

public record WorkoutSessionExerciseSetRequest(
        @NotNull(message = "Počet opakování nesmí být prázdný!")
        int repetitions,
        @NotNull(message = "Váha nesmí být prázdná!")
        float weight
) {
}
