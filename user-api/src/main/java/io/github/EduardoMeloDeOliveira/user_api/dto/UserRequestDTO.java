package io.github.EduardoMeloDeOliveira.user_api.dto;

public record UserRequestDTO(
        String name,
        String email,
        String password,
        String phoneNumber,
        String documentNumber

) {
}
