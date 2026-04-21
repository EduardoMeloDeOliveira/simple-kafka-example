package io.github.EduardoMeloDeOliveira.persistence_api.dto;

public record UserResponseDTO(
        Long userId,
        String name,
        String email,
        String phoneNumber,
        String documentNumber
) {
}
