package io.github.EduardoMeloDeOliveira.persistence_api.dto;

public record UserUpdateRequestDTO(
        Long userId,
        UserRequestDTO user
) {
}

