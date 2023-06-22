package ru.skillbox.diplom.group35.microservice.dialog.impl.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skillbox.diplom.group35.library.core.dto.streaming.MessageDto;
import ru.skillbox.diplom.group35.microservice.dialog.api.dto.dialog.DialogDto;
import ru.skillbox.diplom.group35.microservice.dialog.domain.model.Dialog;
import ru.skillbox.diplom.group35.microservice.dialog.domain.model.Message;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DialogMapperTest {

    @Mock
    private MessageMapper messageMapper;

    @InjectMocks
    private DialogMapperImpl dialogMapper;

    private Dialog dialog;
    private DialogDto dialogDto;

    private Message message;
    private MessageDto messageDto;


    @BeforeEach
    void setUp() {
        dialog = new Dialog();
        dialog.setId(UUID.fromString("eeff65ae-737d-4558-995b-475c99f27789"));
        dialog.setIsDeleted(false);
        dialog.setLastMessage(null);
        dialog.setUnreadCount(10);
        dialog.setConversationPartner1(UUID.fromString("653e7e10-c412-436d-8fbe-f07942648021"));
        dialog.setConversationPartner2(UUID.fromString("90eb3bd5-5ed9-49ab-a5c0-1fe962bb7ee9"));

        dialogDto = new DialogDto();
        dialogDto.setId(UUID.fromString("eeff65ae-737d-4558-995b-475c99f27789"));
        dialogDto.setIsDeleted(false);
        dialogDto.setConversationPartner1(UUID.fromString("653e7e10-c412-436d-8fbe-f07942648021"));
        dialogDto.setConversationPartner2(UUID.fromString("90eb3bd5-5ed9-49ab-a5c0-1fe962bb7ee9"));
        dialogDto.setUnreadCount(10);

        message = new Message();

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
    }

    @Test
    @DisplayName("Тест мапинга dialog в dialogDto")
    void toDto() {
        when(messageMapper.toMessageDto(any(Message.class))).thenReturn(messageDto);
        DialogDto dialogDto = dialogMapper.toDto(dialog, message);
        assertNotNull(dialogDto);
        assertEquals(dialogDto.getId(), UUID.fromString("eeff65ae-737d-4558-995b-475c99f27789"));
        assertEquals(dialogDto.getIsDeleted(), false);
        assertEquals(dialogDto.getUnreadCount(), 10);
        assertEquals(dialogDto.getConversationPartner1(), UUID.fromString("653e7e10-c412-436d-8fbe-f07942648021"));
        assertEquals(dialogDto.getConversationPartner2(), UUID.fromString("90eb3bd5-5ed9-49ab-a5c0-1fe962bb7ee9"));
        assertNotNull(dialogDto.getLastMessage().get(0));
        assertEquals(dialogDto.getLastMessage().get(0).getId(), UUID.fromString("eeff65ae-737d-4558-995b-475c99f27789"));
    }

    @Test
    @DisplayName("Тест мапинга dialogDto в dialog")
    void toDialog() {
        Dialog dialog = dialogMapper.toDialog(dialogDto);
        assertNotNull(dialog);
        assertEquals(dialog.getId(), UUID.fromString("eeff65ae-737d-4558-995b-475c99f27789"));
        assertEquals(dialog.getIsDeleted(), false);
        assertEquals(dialog.getUnreadCount(), 10);
        assertEquals(dialog.getConversationPartner1(), UUID.fromString("653e7e10-c412-436d-8fbe-f07942648021"));
        assertEquals(dialog.getConversationPartner2(), UUID.fromString("90eb3bd5-5ed9-49ab-a5c0-1fe962bb7ee9"));
    }
}