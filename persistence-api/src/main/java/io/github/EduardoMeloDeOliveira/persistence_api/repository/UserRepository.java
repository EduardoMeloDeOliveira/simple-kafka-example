package io.github.EduardoMeloDeOliveira.persistence_api.repository;

import io.github.EduardoMeloDeOliveira.persistence_api.enity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}

