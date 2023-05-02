package ru.skillbox.diplom.group35.microservice.dialog.impl.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group35.library.core.utils.SecurityUtil;
import ru.skillbox.diplom.group35.microservice.dialog.api.dto.dialog.DialogDto;
import ru.skillbox.diplom.group35.microservice.dialog.api.dto.dialog.DialogSearchDto;
import ru.skillbox.diplom.group35.microservice.dialog.api.dto.message.MessageDto;
import ru.skillbox.diplom.group35.microservice.dialog.api.dto.message.MessageSearchDto;
import ru.skillbox.diplom.group35.microservice.dialog.api.dto.message.MessageShortDto;
import ru.skillbox.diplom.group35.microservice.dialog.api.dto.message.UnreadCountDto;
import ru.skillbox.diplom.group35.microservice.dialog.domain.model.*;
import ru.skillbox.diplom.group35.microservice.dialog.impl.mapper.DialogMapper;
import ru.skillbox.diplom.group35.microservice.dialog.impl.mapper.MessageMapper;
import ru.skillbox.diplom.group35.microservice.dialog.impl.repository.DialogRepository;
import ru.skillbox.diplom.group35.microservice.dialog.impl.repository.MessageRepository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

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
    private final SecurityUtil securityUtil;
    private final DialogRepository dialogRepository;
    private final MessageRepository messageRepository;

    public DialogDto createDialog(DialogDto dialogDto, UUID authorId) {
        Dialog dialog = null;
        Optional<Dialog> dialogFound = dialogRepository
                .findOne(getDialogSpec(new DialogSearchDto(authorId, dialogDto.getConversationPartner())));
        if (dialogFound.isPresent()) {
            dialog =  dialogFound.get();
        } else {
            dialog = dialogRepository.save(dialogMapper.toDialog(dialogDto, authorId));
        }
        Optional<Message> lastMessage = messageRepository.findTopByDialogIdOrderByTimeDesc(dialog.getId());
        return dialogMapper.toDto(dialog, lastMessage.isPresent() ? lastMessage.get() : null);
    }

    public MessageDto createMessage(MessageDto messageDto) {
        messageDto.setIsDeleted(false);
        Dialog dialog = null;
        if (messageDto.getDialogId() != null) {
            dialog = dialogRepository.getById(messageDto.getDialogId());
        }
        dialog = dialog == null ? createDialog(messageDto.getAuthorId(), messageDto.getRecipientId()) : dialog;

        Message message = messageMapper.toMessage(messageDto);
        message.setDialog(dialog);
        message = messageRepository.save(message);
        return messageMapper.toMessageDto(message);
    }

    public void updateReadStatus(UUID dialogId) {
        messageRepository.updateReadStatusByDialogId("READ", dialogId);
    }

    public Page<DialogDto> getDialogs(Pageable pageable) {
        UUID authorId = securityUtil.getAccountDetails().getId();
        log.info("found current user id: {}", authorId);
        DialogSearchDto dialogSearchDto = new DialogSearchDto(authorId);
        Page<Dialog> dialogs = dialogRepository.findAll(getDialogSpec(dialogSearchDto), pageable);

        Page<DialogDto> dialogDtoPage = dialogs.map(dialog -> {
            Optional<Message> lastMessage = messageRepository.findTopByDialogIdOrderByTimeDesc(dialog.getId());
            DialogDto dto = dialogMapper.toDto(dialog, lastMessage.isPresent() ? lastMessage.get() : null);
            MessageSearchDto messageSearchDto = new MessageSearchDto(false, authorId, dialog.getConversationPartner(), "SENT");
            dto.setUnreadCount(getUnreadCount(messageSearchDto));
            return  dto;
        });
        return dialogDtoPage;
    }

    public UnreadCountDto getUnreadCountDto() {
        UUID recipientId = securityUtil.getAccountDetails().getId();
        log.info("found current user id: {}", recipientId);
        MessageSearchDto messageSearchDto = new MessageSearchDto(false, null, recipientId, "SENT");
        return new UnreadCountDto(getUnreadCount(messageSearchDto));
    }

    private Integer getUnreadCount(MessageSearchDto messageSearchDto) {
        return Math.toIntExact(messageRepository.count(getMessageSpec(messageSearchDto)));
    }

    public Page<MessageShortDto> getMessages(UUID companionId, Pageable pageable) {
        UUID authorId = securityUtil.getAccountDetails().getId();
        log.info("found current user id: {}", authorId);
        MessageSearchDto messageSearchDto = new MessageSearchDto(false, authorId, companionId, null);
        Page<Message> messages = messageRepository.findAll(getMessageSpec(messageSearchDto), pageable);
        if (messages.get().count() == 0) {
            dialogRepository.save(createDialog(authorId, companionId));
        }
        Page<MessageShortDto> messageShortDtoPage = messages.map(messageMapper::toShortMessageDto);
        return messageShortDtoPage;
    }

    private Dialog createDialog(UUID authorId, UUID companionId) {
        log.info("create dialog w authorId: {}, companionId: {}", authorId, companionId);
        Optional<Dialog> dialogFound = dialogRepository
                .findOne(getDialogSpec(new DialogSearchDto(authorId, companionId)));
        if (dialogFound.isPresent()) {
            return dialogFound.get();
        }
        Dialog dialog = new Dialog();
        dialog.setAuthorId(authorId);
        dialog.setConversationPartner(companionId);
        dialog.setIsDeleted(false);
        return dialogRepository.save(dialog);
    }

    public static Specification<Message> getMessageSpec(MessageSearchDto messageSearchDto) {
        return getBaseSpecification(messageSearchDto)
                .and(equal(Message_.authorId, messageSearchDto.getAuthorId(), true))
                .and(equal(Message_.recipientId, messageSearchDto.getRecipientId(), true))
                .and(equal(Message_.readStatus, messageSearchDto.getReadStatus(), true))
                .and(equal(Message_.isDeleted, messageSearchDto.getIsDeleted(), true));
    }
    
    public static Specification<Dialog> getDialogSpec(DialogSearchDto dialogSearchDto) {
        return getBaseSpecification(dialogSearchDto)
                .and(equal(Dialog_.authorId, dialogSearchDto.getAuthorId(), true))
                .and(equal(Dialog_.conversationPartner, dialogSearchDto.getConversationPartner(), true))
                .and(equal(Dialog_.isDeleted, dialogSearchDto.getIsDeleted(), true));
    }

}
