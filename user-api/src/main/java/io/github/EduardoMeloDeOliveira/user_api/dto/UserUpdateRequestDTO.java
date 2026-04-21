package io.github.EduardoMeloDeOliveira.user_api.dto;

public record UserUpdateRequestDTO(
        Long userId,
        UserRequestDTO user
) {
}

