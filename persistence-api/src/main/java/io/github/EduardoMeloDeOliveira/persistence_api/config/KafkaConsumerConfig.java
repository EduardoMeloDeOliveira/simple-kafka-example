package io.github.EduardoMeloDeOliveira.persistence_api.config;

import io.github.EduardoMeloDeOliveira.persistence_api.dto.UserDeleteRequestDTO;
import io.github.EduardoMeloDeOliveira.persistence_api.dto.UserGetByIdRequestDTO;
import io.github.EduardoMeloDeOliveira.persistence_api.dto.UserRequestDTO;
import io.github.EduardoMeloDeOliveira.persistence_api.dto.UserUpdateRequestDTO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootStrapAddress;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    private <T> ConsumerFactory<String, T> buildConsumerFactory(Class<T> type) {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapAddress);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        properties.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        properties.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        properties.put(JsonDeserializer.VALUE_DEFAULT_TYPE, type.getName());
        return new DefaultKafkaConsumerFactory<>(properties);
    }

    private <T> ConcurrentKafkaListenerContainerFactory<String, T> buildListenerFactory(Class<T> type) {
        ConcurrentKafkaListenerContainerFactory<String, T> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(buildConsumerFactory(type));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserRequestDTO> createUserKafkaListenerContainerFactory() {
        return buildListenerFactory(UserRequestDTO.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserUpdateRequestDTO> updateUserKafkaListenerContainerFactory() {
        return buildListenerFactory(UserUpdateRequestDTO.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserDeleteRequestDTO> deleteUserKafkaListenerContainerFactory() {
        return buildListenerFactory(UserDeleteRequestDTO.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserGetByIdRequestDTO> getByIdKafkaListenerContainerFactory() {
        return buildListenerFactory(UserGetByIdRequestDTO.class);
    }
}
