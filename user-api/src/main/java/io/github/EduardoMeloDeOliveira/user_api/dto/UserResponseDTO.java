package io.github.EduardoMeloDeOliveira.user_api.dto;

public record UserResponseDTO(
        Long userId,
        String name,
        String email,
        String phoneNumber,
        String documentNumber
) {
}
