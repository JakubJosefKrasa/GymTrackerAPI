package com.kuba.GymTrackerAPI.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RegisterRequestDTO(
        @NotNull(message = "Emailová adresa nesmí být prázdná!")
        @NotBlank(message = "Emailová adresa nesmí být prázdná!")
        @Email(
                message = "Neplatný email!", regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
                flags = Pattern.Flag.CASE_INSENSITIVE
        )
        String email,
        @NotNull(message = "Heslo nesmí být prázdné!")
        @NotBlank(message = "Heslo nesmí být prázdné!")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{7,255}$",
                message = "Heslo musí obsahovat alespoň jedno velké písmeno, číslici, speciální znak a být dlouhé minimálně 7 znaků a maximálně dlouhé 255 znaků!"
        )
        String password,
        @NotNull(message = "Potvrzovací heslo nesmí být prázdné!")
        @NotBlank(message = "Potvrzovací heslo nesmí být prázdné!")
        String confirmPassword
) {
}
