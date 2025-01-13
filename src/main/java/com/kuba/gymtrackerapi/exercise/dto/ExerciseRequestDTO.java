package com.kuba.gymtrackerapi.exercise.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ExerciseRequestDTO(

        @NotNull(message = "Název cviku nesmí být prázdný!")
        @NotBlank(message = "Název cviku nesmí být prázdný!")
        @Size(min = 3, max = 50, message = "Název cviku musí být minimálně 3 znaky dlouhý a maximálně 50 znaků dlouhý!")
        String exerciseName
) {
}
