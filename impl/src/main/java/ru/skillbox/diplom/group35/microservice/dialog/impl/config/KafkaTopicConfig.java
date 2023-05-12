package ru.skillbox.diplom.group35.microservice.dialog.impl.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

/**
 * KafkaTopicConfig
 *
 * @author Georgii Vinogradov
 */

@Configuration
@RequiredArgsConstructor
public class KafkaTopicConfig {

    private final KafkaConstConfig kafkaConstConfig;

    @Bean
    public Map<String, Object> adminConfig() {
        Map<String, Object> configuration = new HashMap<>();
        configuration.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConstConfig.getBootstrapAddress());
        return configuration;
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        return new KafkaAdmin(adminConfig());
    }

    @Bean
    public NewTopic replyTopic() {
        return new NewTopic(kafkaConstConfig.getReplyTopic(),
                kafkaConstConfig.getPartitionCount(),
                kafkaConstConfig.getReplicationFactor());
    }

    @Bean
    public NewTopic requestTopic() {
        return new NewTopic(kafkaConstConfig.getRequestTopic(),
                kafkaConstConfig.getPartitionCount(),
                kafkaConstConfig.getReplicationFactor());
    }

}
