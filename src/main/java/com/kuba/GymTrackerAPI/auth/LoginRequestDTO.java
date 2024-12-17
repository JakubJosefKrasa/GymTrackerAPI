package com.kuba.GymTrackerAPI.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequestDTO(

        @NotNull(message = "Emailová adresa nesmí být prázdná!")
        @NotBlank(message = "Emailová adresa nesmí být prázdná!")
        String email,

        @NotNull(message = "Heslo nesmí být prázdné!")
        @NotBlank(message = "Heslo nesmí být prázdné!")
        String password
) {
}
