package com.kuba.gymtrackerapi.trainingplan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TrainingPlanRequestDTO(

        @NotNull(message = "Název tréninkového plánu nesmí být prázdný!")
        @NotBlank(message = "Název tréninkového plánu nesmí být prázdný!")
        @Size(min = 3, max = 50, message = "Název tréninkového plánu musí být minimálně 3 znaky dlouhý a maximálně 50 znaků dlouhý!")
        String trainingPlanName
) {
}
