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

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServer);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic createUserTopic(){
        return new NewTopic("user-create-request",1,(short) 1);
    }

    @Bean NewTopic getUserTopic(){
        return new NewTopic("user-get-request",1,(short) 1);
    }
}
