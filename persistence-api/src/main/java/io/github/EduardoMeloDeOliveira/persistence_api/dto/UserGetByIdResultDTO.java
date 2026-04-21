package io.github.EduardoMeloDeOliveira.persistence_api.dto;

public record UserGetByIdResultDTO(
        String requestId,
        Long userId,
        boolean found,
        UserResponseDTO user,
        String errorMessage
) {
}

