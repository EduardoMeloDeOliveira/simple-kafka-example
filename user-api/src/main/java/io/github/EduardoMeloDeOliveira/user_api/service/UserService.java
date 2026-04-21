package io.github.EduardoMeloDeOliveira.user_api.service;

import io.github.EduardoMeloDeOliveira.user_api.dto.UserDeleteRequestDTO;
import io.github.EduardoMeloDeOliveira.user_api.dto.UserGetByIdRequestDTO;
import io.github.EduardoMeloDeOliveira.user_api.dto.UserGetByIdResultDTO;
import io.github.EduardoMeloDeOliveira.user_api.dto.UserRequestDTO;
import io.github.EduardoMeloDeOliveira.user_api.dto.UserResponseDTO;
import io.github.EduardoMeloDeOliveira.user_api.dto.UserUpdateRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private static final long GET_BY_ID_TIMEOUT_SECONDS = 5;

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ConcurrentMap<String, CompletableFuture<UserGetByIdResultDTO>> pendingGetById = new ConcurrentHashMap<>();

    @Value("${kafka.topic.user-create-request}")
    private String createUserTopic;

    @Value("${kafka.topic.user-update-request}")
    private String updateUserTopic;

    @Value("${kafka.topic.user-delete-request}")
    private String deleteUserTopic;

    @Value("${kafka.topic.user-get-by-id-request}")
    private String getByIdRequestTopic;

    @KafkaListener(
            topics = "${kafka.topic.user-get-by-id-result}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "getByIdResultKafkaListenerContainerFactory"
    )
    public void consumeGetByIdResult(UserGetByIdResultDTO result) {
        CompletableFuture<UserGetByIdResultDTO> future = pendingGetById.remove(result.requestId());
        if (future != null) {
            future.complete(result);
        }
    }

    public void sendCreateUser(UserRequestDTO userRequestDTO) {
        kafkaTemplate.send(createUserTopic, userRequestDTO.documentNumber(), userRequestDTO);
    }

    public void sendUpdateUser(Long userId, UserRequestDTO userRequestDTO) {
        kafkaTemplate.send(updateUserTopic, String.valueOf(userId), new UserUpdateRequestDTO(userId, userRequestDTO));
    }

    public void sendDeleteUser(Long userId) {
        kafkaTemplate.send(deleteUserTopic, String.valueOf(userId), new UserDeleteRequestDTO(userId));
    }

    public Optional<UserResponseDTO> getUserById(Long userId) {
        String requestId = UUID.randomUUID().toString();
        CompletableFuture<UserGetByIdResultDTO> future = new CompletableFuture<>();
        pendingGetById.put(requestId, future);
        kafkaTemplate.send(getByIdRequestTopic, String.valueOf(userId), new UserGetByIdRequestDTO(requestId, userId));

        try {
            UserGetByIdResultDTO result = future.get(GET_BY_ID_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            return result.found() ? Optional.ofNullable(result.user()) : Optional.empty();
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, "Timed out waiting user lookup response", ex);
        } finally {
            pendingGetById.remove(requestId);
        }
    }
}
