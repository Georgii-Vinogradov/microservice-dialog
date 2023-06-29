package ru.skillbox.diplom.group35.microservice.dialog.impl.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.skillbox.diplom.group35.library.core.dto.streaming.MessageDto;
import ru.skillbox.diplom.group35.microservice.dialog.domain.model.Dialog;
import ru.skillbox.diplom.group35.microservice.dialog.domain.model.Message;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MessageMapperTest {

    private MessageMapper messageMapper = Mappers.getMapper(MessageMapper.class);

    private Dialog dialog;
    private Message message;
    private MessageDto messageDto;

    @BeforeEach
    void setUp() {
        dialog = new Dialog();
        dialog.setId(UUID.fromString("eeff65ae-737d-4558-995b-475c99f27789"));
        dialog.setIsDeleted(false);
        dialog.setLastMessage(null);
        dialog.setUnreadCount(0);
        dialog.setConversationPartner1(UUID.fromString("653e7e10-c412-436d-8fbe-f07942648021"));
        dialog.setConversationPartner2(UUID.fromString("90eb3bd5-5ed9-49ab-a5c0-1fe962bb7ee9"));

        message = new Message();
        message.setId(UUID.fromString("7a0113cf-4282-4a7c-8949-ca30b621c595"));
        message.setIsDeleted(false);
        message.setMessageText("test message");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        ZonedDateTime dateTime = ZonedDateTime.parse("2023-03-12T12:18:53.735Z", dateTimeFormatter.withZone(ZoneOffset.UTC));
        message.setTime(dateTime);
        message.setConversationPartner1(UUID.fromString("653e7e10-c412-436d-8fbe-f07942648021"));
        message.setConversationPartner2(UUID.fromString("90eb3bd5-5ed9-49ab-a5c0-1fe962bb7ee9"));
        message.setKafkaTimestamp(123456789L);
        message.setReadStatus("SENT");
        message.setDialog(dialog);

        messageDto = new MessageDto();
        messageDto.setId(UUID.fromString("eeff65ae-737d-4558-995b-475c99f27789"));
        messageDto.setIsDeleted(false);
        messageDto.setMessageText("test message");
        messageDto.setTime(dateTime);
        messageDto.setKafkaTimestamp(123456789L);
        messageDto.setConversationPartner1(UUID.fromString("653e7e10-c412-436d-8fbe-f07942648021"));
        messageDto.setConversationPartner2(UUID.fromString("90eb3bd5-5ed9-49ab-a5c0-1fe962bb7ee9"));
        messageDto.setReadStatus("SENT");
    }

    @Test
    @DisplayName("Тест мапинга Message в MessageDto")
    void toMessageDto() {
        MessageDto messageDto = messageMapper.toMessageDto(message);

        assertNotNull(messageDto);
        assertEquals(messageDto.getId(), UUID.fromString("7a0113cf-4282-4a7c-8949-ca30b621c595"));
        assertEquals(messageDto.getIsDeleted(), false);
        assertEquals(messageDto.getMessageText(), "test message");
        assertEquals(messageDto.getConversationPartner1(), UUID.fromString("653e7e10-c412-436d-8fbe-f07942648021"));
        assertEquals(messageDto.getConversationPartner2(), UUID.fromString("90eb3bd5-5ed9-49ab-a5c0-1fe962bb7ee9"));
        assertEquals(messageDto.getReadStatus(), "SENT");
        assertEquals(messageDto.getDialogId(), UUID.fromString("eeff65ae-737d-4558-995b-475c99f27789"));
        assertEquals(messageDto.getKafkaTimestamp(), 123456789L);
        assertEquals(messageDto.getTime().toString(), "2023-03-12T12:18:53.735Z");
    }

    @Test
    @DisplayName("Тест мапинга MessageDto в Message")
    void toMessage() {
        Message message = messageMapper.toMessage(messageDto);

        assertNotNull(message);
        assertEquals(message.getId(), UUID.fromString("eeff65ae-737d-4558-995b-475c99f27789"));
        assertEquals(message.getIsDeleted(), false);
        assertEquals(message.getMessageText(), "test message");
        assertEquals(message.getTime().toString(), "2023-03-12T12:18:53.735Z");
        assertEquals(message.getKafkaTimestamp(), 123456789L);
        assertEquals(message.getConversationPartner1(),  UUID.fromString("653e7e10-c412-436d-8fbe-f07942648021"));
        assertEquals(message.getConversationPartner2(),  UUID.fromString("90eb3bd5-5ed9-49ab-a5c0-1fe962bb7ee9"));
        assertEquals(message.getReadStatus(), "SENT");
    }
}