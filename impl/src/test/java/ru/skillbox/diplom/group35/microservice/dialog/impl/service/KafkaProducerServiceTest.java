package ru.skillbox.diplom.group35.microservice.dialog.impl.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.skillbox.diplom.group35.library.core.dto.streaming.MessageDto;
import ru.skillbox.diplom.group35.library.core.dto.streaming.StreamingMessageDto;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext
@ActiveProfiles(profiles = "test")
@ExtendWith(SpringExtension.class)
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class KafkaProducerServiceTest {
    @Autowired
    private KafkaProducerService kafkaProducerService;
    @MockBean
    private KafkaTemplate<String, StreamingMessageDto<MessageDto>> kafkaTemplate;
    private StreamingMessageDto<MessageDto> streamingMessageDto;
    private MessageDto messageDto;

    @BeforeEach
    void setUp() {
        messageDto = new MessageDto();
        messageDto.setId(UUID.fromString("eeff65ae-737d-4558-995b-475c99f27789"));
        messageDto.setIsDeleted(false);
        messageDto.setMessageText("test message");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        ZonedDateTime dateTime = ZonedDateTime.parse("2023-03-12T12:18:53.735Z", dateTimeFormatter.withZone(ZoneOffset.UTC));
        messageDto.setTime(dateTime);
        messageDto.setKafkaTimestamp(123456789L);
        messageDto.setConversationPartner1(UUID.fromString("653e7e10-c412-436d-8fbe-f07942648021"));
        messageDto.setConversationPartner2(UUID.fromString("90eb3bd5-5ed9-49ab-a5c0-1fe962bb7ee9"));
        messageDto.setReadStatus("SENT");

        streamingMessageDto = new StreamingMessageDto<>();
        streamingMessageDto.setRecipientId(UUID.fromString("90eb3bd5-5ed9-49ab-a5c0-1fe962bb7ee9"));
        streamingMessageDto.setType("message");
        streamingMessageDto.setData(messageDto);
    }

    @Test
    public void testSend() {
        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<StreamingMessageDto<MessageDto>> messageCaptor = ArgumentCaptor.forClass(StreamingMessageDto.class);

        assertDoesNotThrow(() -> kafkaProducerService.send(streamingMessageDto));

        verify(kafkaTemplate, times(1)).send(topicCaptor.capture(), keyCaptor.capture(), messageCaptor.capture());
        String capturedTopic = topicCaptor.getValue();
        String capturedKey = keyCaptor.getValue();
        StreamingMessageDto<MessageDto> capturedMessage = messageCaptor.getValue();
        MessageDto result = capturedMessage.getData();

        assertNotNull(result);
        assertEquals(result.getId(), UUID.fromString("eeff65ae-737d-4558-995b-475c99f27789"));
        assertEquals(result.getIsDeleted(), false);
        assertEquals(result.getMessageText(), "test message");
        assertEquals(result.getTime().toString(), "2023-03-12T12:18:53.735Z");
        assertEquals(result.getKafkaTimestamp(), 123456789L);
        assertEquals(result.getConversationPartner1(),  UUID.fromString("653e7e10-c412-436d-8fbe-f07942648021"));
        assertEquals(result.getConversationPartner2(),  UUID.fromString("90eb3bd5-5ed9-49ab-a5c0-1fe962bb7ee9"));
        assertEquals(result.getReadStatus(), "SENT");
    }
}