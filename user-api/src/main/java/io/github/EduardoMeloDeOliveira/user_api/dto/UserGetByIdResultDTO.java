package io.github.EduardoMeloDeOliveira.user_api.dto;

public record UserGetByIdResultDTO(
        String requestId,
        Long userId,
        boolean found,
        UserResponseDTO user,
        String errorMessage
) {
}

