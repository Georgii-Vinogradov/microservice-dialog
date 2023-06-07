package ru.skillbox.diplom.group35.microservice.dialog.impl.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group35.library.core.dto.streaming.MessageDto;
import ru.skillbox.diplom.group35.library.core.utils.SecurityUtil;
import ru.skillbox.diplom.group35.microservice.dialog.api.dto.dialog.DialogDto;
import ru.skillbox.diplom.group35.microservice.dialog.api.dto.dialog.DialogSearchDto;
import ru.skillbox.diplom.group35.microservice.dialog.api.dto.message.MessageSearchDto;
import ru.skillbox.diplom.group35.microservice.dialog.api.dto.message.MessageShortDto;
import ru.skillbox.diplom.group35.microservice.dialog.api.dto.message.UnreadCountDto;
import ru.skillbox.diplom.group35.microservice.dialog.domain.model.*;
import ru.skillbox.diplom.group35.microservice.dialog.impl.mapper.DialogMapper;
import ru.skillbox.diplom.group35.microservice.dialog.impl.mapper.MessageMapper;
import ru.skillbox.diplom.group35.microservice.dialog.impl.repository.DialogRepository;
import ru.skillbox.diplom.group35.microservice.dialog.impl.repository.MessageRepository;

import javax.transaction.Transactional;
import java.util.*;

import static ru.skillbox.diplom.group35.library.core.utils.SpecificationUtil.*;

/**
 * DialogService
 *
 * @author Georgii Vinogradov
 */

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DialogService {

    private final DialogMapper dialogMapper;
    private final MessageMapper messageMapper;
    private final DialogRepository dialogRepository;
    private final MessageRepository messageRepository;
    private final SecurityUtil securityUtil;

    private DialogDto createDialogDto(UUID conversationPartner1, UUID conversationPartner2) {
        DialogDto dialogDto = new DialogDto();
        dialogDto.setIsDeleted(false);
        dialogDto.setConversationPartner1(conversationPartner1);
        dialogDto.setConversationPartner2(conversationPartner2);
        return dialogDto;
    }

    private Dialog createDialogIfAbsent(DialogDto dialogDto) {
        log.info("call find or create new dialog");
        Dialog dialog = null;
        DialogSearchDto searchDto = new DialogSearchDto(dialogDto.getConversationPartner1(), dialogDto.getConversationPartner2());
        Optional<Dialog> dialogFound = dialogRepository.findOne(getDialogSpecIn(searchDto));
        if (dialogFound.isPresent()) {
            dialog =  dialogFound.get();
            log.debug("found exist dialog: {}", dialog);
        } else {
            dialog = dialogRepository.save(dialogMapper.toDialog(dialogDto));
            log.debug("create new dialog: {}", dialog);
        }
        return dialog;
    }

    public DialogDto createDialog(DialogDto dialogDto) {
        log.debug("DialogDto: {}", dialogDto);
        Dialog dialog = createDialogIfAbsent(dialogDto);
        Optional<Message> lastMessage = messageRepository.findTopByDialogIdOrderByTimeDesc(dialog.getId());
        log.info("return dialogId {}", dialog.getId());
        return dialogMapper.toDto(dialog, lastMessage.isPresent() ? lastMessage.get() :null);
    }

    public DialogDto getDialogByRecipientId(UUID recipientId) {
        UUID currentUserId = securityUtil.getAccountDetails().getId();
        log.info("found current user id: {}", currentUserId);
        DialogDto dialogDto = createDialogDto(currentUserId, recipientId);
        Dialog dialog = createDialogIfAbsent(dialogDto);
        log.info("return dialogId {}", dialog.getId());
        Optional<Message> lastMessage = messageRepository.findTopByDialogIdOrderByTimeDesc(dialog.getId());
        DialogDto result = dialogMapper.toDto(dialog, lastMessage.isPresent() ? lastMessage.get() :null);
        log.debug("return DialogDto: {}", result);
        return result;
    }

    public MessageDto createMessage(MessageDto messageDto) {
        messageDto.setIsDeleted(false);
        Dialog dialog = null;
        if (messageDto.getDialogId() != null) {
            dialog = dialogRepository.getById(messageDto.getDialogId());
        } else {
            DialogDto dialogDto = createDialogDto(messageDto.getConversationPartner1(), messageDto.getConversationPartner2());
            dialog = createDialogIfAbsent(dialogDto);
        }
        Message message = messageMapper.toMessage(messageDto);
        message.setDialog(dialog);
        message = messageRepository.save(message);
        log.info("return messageId {}", message.getId());
        return messageMapper.toMessageDto(message);
    }

    public void updateReadStatus(UUID dialogId) {
        messageRepository.updateReadStatusByDialogId("READ", dialogId);
    }

    public Page<DialogDto> getDialogs(Pageable pageable) {
        UUID currentUserId = securityUtil.getAccountDetails().getId();
        log.info("found current user id: {}", currentUserId);
        DialogSearchDto dialogSearchDto = new DialogSearchDto().setConversationPartner1(currentUserId);
        DialogSearchDto reverseDialogSearchDto = new DialogSearchDto().setConversationPartner2(currentUserId);
        Page<Dialog> dialogs = dialogRepository.findAll(getDialogSpec(dialogSearchDto)
                .or(getDialogSpec(reverseDialogSearchDto)), pageable);
        log.debug("found {} dialogs", dialogs.getNumberOfElements());
        Page<DialogDto> dialogDtoPage = dialogs.map(dialog -> {
            Optional<Message> lastMessage = messageRepository.findTopByDialogIdOrderByTimeDesc(dialog.getId());
            DialogDto dto = dialogMapper.toDto(dialog, lastMessage.isPresent() ? lastMessage.get() : null);
            MessageSearchDto messageSearchDto = new MessageSearchDto()
                    .setConversationPartner1(dto.getConversationPartner1())
                    .setConversationPartner2(dto.getConversationPartner1())
                    .setReadStatus("SENT");
            dto.setUnreadCount(getUnreadCount(messageSearchDto));
            return  dto;
        });
        log.info("return dialog page number {} of {}", dialogDtoPage.getNumber(), dialogDtoPage.getTotalPages());
        return dialogDtoPage;
    }

    public UnreadCountDto getUnreadCountDto() {
        UUID currentUserId = securityUtil.getAccountDetails().getId();
        log.info("found current user id: {}", currentUserId);
        MessageSearchDto messageSearchDto = new MessageSearchDto()
                .setConversationPartner2(currentUserId)
                .setReadStatus("SENT");
        return new UnreadCountDto(getUnreadCount(messageSearchDto));
    }

    private Integer getUnreadCount(MessageSearchDto messageSearchDto) {
        return Math.toIntExact(messageRepository.count(getMessageSpec(messageSearchDto)));
    }

    public Page<MessageShortDto> getMessages(UUID companionId, Pageable pageable) {
        UUID currentUserId = securityUtil.getAccountDetails().getId();
        log.info("found current user id: {}", currentUserId);
        MessageSearchDto messageSearchDto = new MessageSearchDto()
                .setConversationPartner1(currentUserId)
                .setConversationPartner2(companionId);
        MessageSearchDto reverseMessageSearchDto = new MessageSearchDto()
                .setConversationPartner1(companionId)
                .setConversationPartner2(currentUserId);
        Page<Message> messages = messageRepository.findAll(getMessageSpec(messageSearchDto)
                .or(getMessageSpec(reverseMessageSearchDto)), pageable);
        Page<MessageShortDto> messageShortDtoPage = messages.map(messageMapper::toShortMessageDto);
        log.info("return messages page number {} of {}", messageShortDtoPage.getNumber(), messageShortDtoPage.getTotalPages());
        return messageShortDtoPage;
    }

    public Long getLastTimestamp() {
        return messageRepository.findLastKafkaTimestamp();
    }

    public static Specification<Message> getMessageSpec(MessageSearchDto messageSearchDto) {
        return getBaseSpecification(messageSearchDto)
                .and(equal(Message_.conversationPartner1, messageSearchDto.getConversationPartner1(), true))
                .and(equal(Message_.conversationPartner2, messageSearchDto.getConversationPartner2(), true))
                .and(equal(Message_.readStatus, messageSearchDto.getReadStatus(), true))
                .and(equal(Message_.isDeleted, messageSearchDto.getIsDeleted(), true));
    }
    
    public static Specification<Dialog> getDialogSpecIn(DialogSearchDto dialogSearchDto) {
        List<UUID> anyConversationPartner = List.of(dialogSearchDto.getConversationPartner1(), dialogSearchDto.getConversationPartner2());
        return getBaseSpecification(dialogSearchDto)
                .and(in(Dialog_.conversationPartner1, anyConversationPartner, true))
                .and(in(Dialog_.conversationPartner2, anyConversationPartner, true))
                .and(equal(Dialog_.isDeleted, dialogSearchDto.getIsDeleted(), true));
    }

    public static Specification<Dialog> getDialogSpec(DialogSearchDto dialogSearchDto) {
        return getBaseSpecification(dialogSearchDto)
                .and(equal(Dialog_.conversationPartner1, dialogSearchDto.getConversationPartner1(), true))
                .and(equal(Dialog_.conversationPartner2, dialogSearchDto.getConversationPartner2(), true))
                .and(equal(Dialog_.isDeleted, dialogSearchDto.getIsDeleted(), true));
    }

}
