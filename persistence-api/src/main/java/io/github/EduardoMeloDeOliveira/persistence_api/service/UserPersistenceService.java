package io.github.EduardoMeloDeOliveira.persistence_api.service;

import io.github.EduardoMeloDeOliveira.persistence_api.dto.UserRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserPersistenceService {

    @KafkaListener(topics = "user-create-request", groupId = "user-persistence-group")
    public void createUserListner(UserRequestDTO userRequestDTO) {
        log.info("Received user creation request: {}", userRequestDTO);
    }
}
