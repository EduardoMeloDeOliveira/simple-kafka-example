package io.github.EduardoMeloDeOliveira.user_api.service;

import io.github.EduardoMeloDeOliveira.user_api.dto.UserRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final KafkaTemplate<String, UserRequestDTO> kafkaTemplate;

    @Value("${kafka-create-user-producer}")
    private  String createUserTopic;


    public void sendMessageCreateUser(UserRequestDTO userRequestDTO) {
        log.info("Sending message to topic create-user {}", userRequestDTO);
        kafkaTemplate.send(createUserTopic, userRequestDTO);
    }
}
