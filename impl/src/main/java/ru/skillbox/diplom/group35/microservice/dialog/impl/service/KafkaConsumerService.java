package ru.skillbox.diplom.group35.microservice.dialog.impl.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AbstractConsumerSeekAware;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group35.library.core.dto.streaming.MessageDto;
import ru.skillbox.diplom.group35.library.core.dto.streaming.StreamingMessageDto;

import java.util.ArrayList;
import java.util.Map;

/**
 * KafkaConsumerService
 *
 * @author Georgii Vinogradov
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService extends AbstractConsumerSeekAware {

    private final DialogService dialogService;
    private final KafkaProducerService kafkaProducerService;

    @KafkaListener(topics = "${kafka.topic.request}")
    public void receiveMessage(@Payload StreamingMessageDto<MessageDto> streamingMessageDto,
                               @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp,
                               Acknowledgment acknowledgment) {
        log.info("received message, timestamp {} message: {}",
                timestamp, streamingMessageDto.getData().getMessageText());

        MessageDto messageDto = streamingMessageDto.getData();
        messageDto.setKafkaTimestamp(timestamp);
        messageDto = dialogService.createMessage(messageDto);
        streamingMessageDto.setData(messageDto);
        acknowledgment.acknowledge();
        kafkaProducerService.send(streamingMessageDto);
    }

    @Override
    public void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
        Long timestamp = dialogService.getLastTimestamp();
        if (timestamp == null) {
            return;
        }
        callback.seekToTimestamp(new ArrayList<>(assignments.keySet()), timestamp + 1);
    }

}
