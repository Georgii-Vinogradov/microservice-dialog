package ru.skillbox.diplom.group35.microservice.dialog.impl.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.skillbox.diplom.group35.library.core.dto.streaming.MessageDto;
import ru.skillbox.diplom.group35.library.core.utils.AccountDetails;
import ru.skillbox.diplom.group35.library.core.utils.SecurityUtil;
import ru.skillbox.diplom.group35.microservice.dialog.api.dto.dialog.DialogDto;
import ru.skillbox.diplom.group35.microservice.dialog.api.dto.message.MessageShortDto;
import ru.skillbox.diplom.group35.microservice.dialog.api.dto.message.UnreadCountDto;
import ru.skillbox.diplom.group35.microservice.dialog.domain.model.Dialog;
import ru.skillbox.diplom.group35.microservice.dialog.domain.model.Message;
import ru.skillbox.diplom.group35.microservice.dialog.impl.mapper.DialogMapperImpl;
import ru.skillbox.diplom.group35.microservice.dialog.impl.mapper.MessageMapper;
import ru.skillbox.diplom.group35.microservice.dialog.impl.repository.DialogRepository;
import ru.skillbox.diplom.group35.microservice.dialog.impl.repository.MessageRepository;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DialogServiceTest {
    @Mock
    private MessageMapper messageMapper;
    @Mock
    private DialogMapperImpl dialogMapper;
    @Mock
    private DialogRepository dialogRepository;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private SecurityUtil securityUtil;
    @InjectMocks
    private DialogService dialogService;

    private DialogDto dialogDto;
    private Dialog dialog;
    private Message message;
    private MessageDto messageDto;
    private MessageShortDto messageShortDto;
    private AccountDetails accountDetails;
    private UUID recipientId;

    @BeforeEach
    void setUp() {
        accountDetails = new AccountDetails();
        accountDetails.setId(UUID.fromString("653e7e10-c412-436d-8fbe-f07942648021"));

        recipientId = UUID.fromString("90eb3bd5-5ed9-49ab-a5c0-1fe962bb7ee9");

        dialogDto = new DialogDto();
        dialogDto.setId(UUID.fromString("eeff65ae-737d-4558-995b-475c99f27789"));
        dialogDto.setIsDeleted(false);
        dialogDto.setConversationPartner1(UUID.fromString("653e7e10-c412-436d-8fbe-f07942648021"));
        dialogDto.setConversationPartner2(UUID.fromString("90eb3bd5-5ed9-49ab-a5c0-1fe962bb7ee9"));
        dialogDto.setUnreadCount(10);
        dialogDto.setLastMessage(Collections.emptyList());

        dialog = new Dialog();
        dialog.setId(UUID.fromString("eeff65ae-737d-4558-995b-475c99f27789"));
        dialog.setIsDeleted(false);
        dialog.setLastMessage(null);
        dialog.setUnreadCount(10);
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

        messageShortDto = new MessageShortDto();
        messageShortDto.setId(UUID.fromString("eeff65ae-737d-4558-995b-475c99f27789"));
        messageShortDto.setIsDeleted(false);
        messageShortDto.setMessageText("test message");
        messageShortDto.setTime(dateTime);
        messageShortDto.setConversationPartner1(UUID.fromString("653e7e10-c412-436d-8fbe-f07942648021"));
        messageShortDto.setConversationPartner2(UUID.fromString("90eb3bd5-5ed9-49ab-a5c0-1fe962bb7ee9"));
    }

    @Test
    @DisplayName("Тест создания диалога")
    void createDialog() {
        when(messageRepository.findTopByDialogIdOrderByTimeDesc(any(UUID.class))).thenReturn(Optional.empty());
        when(dialogMapper.toDto(dialog, null)).thenReturn(dialogDto);
        when(dialogRepository.findOne(any(Specification.class))).thenReturn(Optional.of(dialog));
        lenient().when(dialogRepository.save(any(Dialog.class))).thenReturn(dialog);

        DialogDto result = dialogService.createDialog(dialogDto);

        assertNotNull(result);
        assertEquals(result.getId().toString(), "eeff65ae-737d-4558-995b-475c99f27789");
        assertEquals(result.getIsDeleted(), false);
        assertTrue(result.getLastMessage().isEmpty());
        assertEquals(result.getUnreadCount(), 10);
        assertEquals(result.getConversationPartner1().toString(), "653e7e10-c412-436d-8fbe-f07942648021");
        assertEquals(result.getConversationPartner2().toString(), "90eb3bd5-5ed9-49ab-a5c0-1fe962bb7ee9");
    }

    @Test
    @DisplayName("Тест получения диалога с пользователем")
    void getDialogByRecipientId() {
        when(securityUtil.getAccountDetails()).thenReturn(accountDetails);
        when(messageRepository.findTopByDialogIdOrderByTimeDesc(any(UUID.class))).thenReturn(Optional.empty());
        when(dialogMapper.toDialog(any(DialogDto.class))).thenReturn(dialog);
        when(dialogMapper.toDto(dialog, null)).thenReturn(dialogDto);
        when(dialogRepository.findOne(any(Specification.class))).thenReturn(Optional.empty());
        when(dialogRepository.save(any(Dialog.class))).thenReturn(dialog);

        DialogDto result = dialogService.getDialogByRecipientId(recipientId);

        assertNotNull(result);
        assertEquals(result.getId().toString(), "eeff65ae-737d-4558-995b-475c99f27789");
        assertEquals(result.getIsDeleted(), false);
        assertTrue(result.getLastMessage().isEmpty());
        assertEquals(result.getUnreadCount(), 10);
        assertEquals(result.getConversationPartner1().toString(), "653e7e10-c412-436d-8fbe-f07942648021");
        assertEquals(result.getConversationPartner2().toString(), "90eb3bd5-5ed9-49ab-a5c0-1fe962bb7ee9");
    }

    @Test
    @DisplayName("Тест создания сообщения")
    void createMessage() {
        when(messageRepository.save(any(Message.class))).thenReturn(message);
        when(messageMapper.toMessage(any(MessageDto.class))).thenReturn(message);
        when(messageMapper.toMessageDto(any(Message.class))).thenReturn(messageDto);
        when(dialogMapper.toDialog(any(DialogDto.class))).thenReturn(dialog);
        when(dialogRepository.findOne(any(Specification.class))).thenReturn(Optional.empty());
        when(dialogRepository.save(any(Dialog.class))).thenReturn(dialog);

        MessageDto result = dialogService.createMessage(messageDto);

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

    @Test
    @DisplayName("Тест обновления статуса сообщений")
    void updateReadStatus() {
        assertDoesNotThrow(() -> {
            dialogService.updateReadStatus(recipientId);
        });
    }

    @Test
    @DisplayName("Тест получения диалогов")
    void getDialogs() {
        Pageable pageable = PageRequest.of(0, 10);
        when(securityUtil.getAccountDetails()).thenReturn(accountDetails);
        when(dialogMapper.toDto(dialog, null)).thenReturn(dialogDto);
        Page<Dialog> expectedPage = new PageImpl<>(Arrays.asList(dialog), pageable, 1);
        when(dialogRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(messageRepository.findTopByDialogIdOrderByTimeDesc(any(UUID.class))).thenReturn(Optional.empty());
        when(messageRepository.count(any(Specification.class))).thenReturn(10L);

        Page<DialogDto> result = dialogService.getDialogs(pageable);

        assertNotNull(result);
        assertEquals(result.getTotalElements(), 1);
        assertEquals(result.getTotalPages(), 1);
        DialogDto resultDialogDto = result.getContent().get(0);
        assertEquals(resultDialogDto.getId().toString(), "eeff65ae-737d-4558-995b-475c99f27789");
        assertEquals(resultDialogDto.getIsDeleted(), false);
        assertTrue(resultDialogDto.getLastMessage().isEmpty());
        assertEquals(resultDialogDto.getUnreadCount(), 10);
        assertEquals(resultDialogDto.getConversationPartner1().toString(), "653e7e10-c412-436d-8fbe-f07942648021");
        assertEquals(resultDialogDto.getConversationPartner2().toString(), "90eb3bd5-5ed9-49ab-a5c0-1fe962bb7ee9");
    }

    @Test
    @DisplayName("Тест получения количества непрочитанных сообщений")
    void getUnreadCountDto() {
        when(securityUtil.getAccountDetails()).thenReturn(accountDetails);
        when(messageRepository.count(any(Specification.class))).thenReturn(10L);
        UnreadCountDto result = dialogService.getUnreadCountDto();

        assertNotNull(result);
        assertEquals(result.getCount(), 10);
    }

    @Test
    @DisplayName("Тест получения сообщений с собеседником")
    void getMessages() {
        Pageable pageable = PageRequest.of(0, 10);
        when(securityUtil.getAccountDetails()).thenReturn(accountDetails);
        Page<Message> expectedPage = new PageImpl<>(Arrays.asList(message), pageable, 1);
        when(messageRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(messageMapper.toShortMessageDto(any(Message.class))).thenReturn(messageShortDto);

        Page<MessageShortDto> result = dialogService.getMessages(recipientId, pageable);

        assertNotNull(result);
        assertEquals(result.getTotalElements(), 1);
        assertEquals(result.getTotalPages(), 1);
        MessageShortDto resultMessageShortDto = result.getContent().get(0);
        assertEquals(resultMessageShortDto.getId().toString(), "eeff65ae-737d-4558-995b-475c99f27789");
        assertEquals(resultMessageShortDto.getIsDeleted(), false);
        assertEquals(resultMessageShortDto.getMessageText(), "test message");
        assertEquals(resultMessageShortDto.getTime().toString(), "2023-03-12T12:18:53.735Z");
        assertEquals(resultMessageShortDto.getConversationPartner1().toString(), "653e7e10-c412-436d-8fbe-f07942648021");
        assertEquals(resultMessageShortDto.getConversationPartner2().toString(), "90eb3bd5-5ed9-49ab-a5c0-1fe962bb7ee9");
    }

    @Test
    @DisplayName("Тест получения метки времени kafka последнего сообщения")
    void getLastTimestamp() {
        when(messageRepository.findLastKafkaTimestamp()).thenReturn(0L);
        Long result = dialogService.getLastTimestamp();

        assertEquals(result, 0L);
    }
}