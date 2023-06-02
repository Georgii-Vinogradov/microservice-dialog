package ru.skillbox.diplom.group35.microservice.dialog.impl.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group35.library.core.dto.streaming.MessageDto;
import ru.skillbox.diplom.group35.library.core.dto.streaming.StreamingMessageDto;
import ru.skillbox.diplom.group35.microservice.dialog.impl.config.KafkaConstConfig;


/**
 * KafkaProducerService
 *
 * @author Georgii Vinogradov
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, StreamingMessageDto<MessageDto>> kafkaTemplate;
    private final KafkaConstConfig kafkaConstConfig;

    public void send(StreamingMessageDto<MessageDto> messageDto){
        log.info("sent message id: {} to topic {}", messageDto.getData().getId(), kafkaConstConfig.getReplyTopic());
        kafkaTemplate.send(kafkaConstConfig.getReplyTopic(), messageDto.getRecipientId().toString(), messageDto);
    }
}
