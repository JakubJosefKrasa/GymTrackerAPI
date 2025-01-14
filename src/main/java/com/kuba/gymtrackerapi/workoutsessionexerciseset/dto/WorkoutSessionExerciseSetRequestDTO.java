package com.kuba.gymtrackerapi.workoutsessionexerciseset.dto;

import jakarta.validation.constraints.NotNull;

public record WorkoutSessionExerciseSetRequestDTO(

        @NotNull(message = "Počet opakování nesmí být prázdný!")
        int repetitions,
        @NotNull(message = "Váha nesmí být prázdná!")
        float weight
) {

}
