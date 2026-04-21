package io.github.EduardoMeloDeOliveira.persistence_api.dto;

public record UserRequestDTO(
        String name,
        String email,
        String password,
        String phoneNumber,
        String documentNumber

) {
}