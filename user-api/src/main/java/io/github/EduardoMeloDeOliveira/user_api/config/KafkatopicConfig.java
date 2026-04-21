package io.github.EduardoMeloDeOliveira.user_api.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkatopicConfig {

    @Value("${spring.kafka.bootstrap.servers}")
    private String bootStrapServer;

    @Value("${kafka.topic.user-create-request}")
    private String createUserTopic;

    @Value("${kafka.topic.user-update-request}")
    private String updateUserTopic;

    @Value("${kafka.topic.user-delete-request}")
    private String deleteUserTopic;

    @Value("${kafka.topic.user-get-by-id-request}")
    private String getByIdRequestTopic;

    @Value("${kafka.topic.user-get-by-id-result}")
    private String getByIdResultTopic;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServer);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic createUserTopic() {
        return new NewTopic(createUserTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic updateUserTopic() {
        return new NewTopic(updateUserTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic deleteUserTopic() {
        return new NewTopic(deleteUserTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic getByIdRequestTopic() {
        return new NewTopic(getByIdRequestTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic getByIdResultTopic() {
        return new NewTopic(getByIdResultTopic, 1, (short) 1);
    }
}
