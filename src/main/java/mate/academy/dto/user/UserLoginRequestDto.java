package mate.academy.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record UserLoginRequestDto(
        @Email
        @NotNull
        String email,
        @NotNull
        @Length(min = 8, max = 20)
        String password) {
}
