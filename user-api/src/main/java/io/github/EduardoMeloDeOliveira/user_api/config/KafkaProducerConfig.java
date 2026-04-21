package io.github.EduardoMeloDeOliveira.user_api.config;

import io.github.EduardoMeloDeOliveira.user_api.dto.UserRequestDTO;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {


    private final String bootStrapServer = "localhost:9092";

    @Bean
    public ProducerFactory<String, UserRequestDTO> producerFactory() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(JsonSerializer.ADD_TYPE_INFO_HEADERS,false);
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootStrapServer);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(properties);
    }

    @Bean
    public KafkaTemplate<String, UserRequestDTO> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
