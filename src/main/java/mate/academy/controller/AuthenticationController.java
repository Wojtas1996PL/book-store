package mate.academy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.user.UserRegistrationRequestDto;
import mate.academy.dto.user.UserResponseDto;
import mate.academy.exception.RegistrationException;
import mate.academy.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Authentication management")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;

    @Operation(summary = "Register user")
    @PostMapping("/api/auth/registration")
    public UserResponseDto register(@RequestBody UserRegistrationRequestDto request)
            throws RegistrationException {
        return userService.register(request);
    }
}
