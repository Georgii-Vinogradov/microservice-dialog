package ru.skillbox.diplom.group35.microservice.dialog.impl.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * KafkaConstConfig
 *
 * @author Georgii Vinogradov
 */

@Data
@Configuration
public class KafkaConstConfig {
    @Value(value = "${kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value(value = "${kafka.consumer.group-id}")
    private String groupId;

    @Value(value = "${kafka.consumer.max.poll.interval.ms}")
    private Integer maxPollInterval;

    @Value(value = "${kafka.consumer.spring.json.trusted.packages}")
    private String trustPackages;

    @Value(value = "${kafka.consumer.concurrency}")
    private Integer concurrency;

    @Value(value = "${kafka.topic.request}")
    private String requestTopic;

    @Value(value = "${kafka.topic.reply}")
    private String replyTopic;

    @Value(value = "${kafka.topic.partition-count}")
    private Integer partitionCount;

    @Value(value = "${kafka.topic.replication-factor}")
    private Short replicationFactor;

    @Value(value = "${kafka.producer.retries}")
    private Integer retries;

}
