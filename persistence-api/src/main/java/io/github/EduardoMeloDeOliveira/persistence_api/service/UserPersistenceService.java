package io.github.EduardoMeloDeOliveira.persistence_api.service;

import io.github.EduardoMeloDeOliveira.persistence_api.dto.UserDeleteRequestDTO;
import io.github.EduardoMeloDeOliveira.persistence_api.dto.UserGetByIdRequestDTO;
import io.github.EduardoMeloDeOliveira.persistence_api.dto.UserGetByIdResultDTO;
import io.github.EduardoMeloDeOliveira.persistence_api.dto.UserRequestDTO;
import io.github.EduardoMeloDeOliveira.persistence_api.dto.UserResponseDTO;
import io.github.EduardoMeloDeOliveira.persistence_api.dto.UserUpdateRequestDTO;
import io.github.EduardoMeloDeOliveira.persistence_api.enity.UserEntity;
import io.github.EduardoMeloDeOliveira.persistence_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserPersistenceService {

    private final UserRepository userRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic.user-get-by-id-result}")
    private String getByIdResultTopic;

    @KafkaListener(
            topics = "${kafka.topic.user-create-request}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "createUserKafkaListenerContainerFactory"
    )
    public void createUserListener(UserRequestDTO userRequestDTO) {
        UserEntity entity = new UserEntity(
                null,
                userRequestDTO.name(),
                userRequestDTO.email(),
                userRequestDTO.password(),
                userRequestDTO.phoneNumber(),
                userRequestDTO.documentNumber()
        );
        UserEntity savedUser = userRepository.save(entity);
        log.info("Created user with id {}", savedUser.getId());
    }

    @KafkaListener(
            topics = "${kafka.topic.user-update-request}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "updateUserKafkaListenerContainerFactory"
    )
    public void updateUserListener(UserUpdateRequestDTO updateRequestDTO) {
        userRepository.findById(updateRequestDTO.userId()).ifPresent(userEntity -> {
            UserRequestDTO user = updateRequestDTO.user();
            userEntity.setName(user.name());
            userEntity.setEmail(user.email());
            userEntity.setPassword(user.password());
            userEntity.setPhoneNumber(user.phoneNumber());
            userEntity.setDocumentNumber(user.documentNumber());
            userRepository.save(userEntity);
            log.info("Updated user with id {}", userEntity.getId());
        });
    }

    @KafkaListener(
            topics = "${kafka.topic.user-delete-request}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "deleteUserKafkaListenerContainerFactory"
    )
    public void deleteUserListener(UserDeleteRequestDTO deleteRequestDTO) {
        if (userRepository.existsById(deleteRequestDTO.userId())) {
            userRepository.deleteById(deleteRequestDTO.userId());
            log.info("Deleted user with id {}", deleteRequestDTO.userId());
        }
    }

    @KafkaListener(
            topics = "${kafka.topic.user-get-by-id-request}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "getByIdKafkaListenerContainerFactory"
    )
    public void getUserByIdListener(UserGetByIdRequestDTO getByIdRequestDTO) {
        UserGetByIdResultDTO result = userRepository.findById(getByIdRequestDTO.userId())
                .map(this::toResponseDTO)
                .map(user -> new UserGetByIdResultDTO(getByIdRequestDTO.requestId(), getByIdRequestDTO.userId(), true, user, null))
                .orElseGet(() -> new UserGetByIdResultDTO(
                        getByIdRequestDTO.requestId(),
                        getByIdRequestDTO.userId(),
                        false,
                        null,
                        "User not found"
                ));

        kafkaTemplate.send(getByIdResultTopic, String.valueOf(getByIdRequestDTO.userId()), result);
    }

    private UserResponseDTO toResponseDTO(UserEntity userEntity) {
        return new UserResponseDTO(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getEmail(),
                userEntity.getPhoneNumber(),
                userEntity.getDocumentNumber()
        );
    }
}
