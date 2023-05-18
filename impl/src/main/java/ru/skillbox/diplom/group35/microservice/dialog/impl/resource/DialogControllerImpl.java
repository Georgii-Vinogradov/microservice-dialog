package ru.skillbox.diplom.group35.microservice.dialog.impl.resource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.group35.library.core.annotation.EnableExceptionHandler;
import ru.skillbox.diplom.group35.library.core.annotation.EnableSwagger;
import ru.skillbox.diplom.group35.microservice.dialog.api.dto.dialog.DialogDto;
import ru.skillbox.diplom.group35.microservice.dialog.api.dto.message.MessageDto;
import ru.skillbox.diplom.group35.microservice.dialog.api.dto.message.MessageShortDto;
import ru.skillbox.diplom.group35.microservice.dialog.api.dto.message.UnreadCountDto;
import ru.skillbox.diplom.group35.microservice.dialog.api.resource.DialogController;
import ru.skillbox.diplom.group35.microservice.dialog.impl.service.DialogService;

import java.util.UUID;

/**
 * DialogControllerImpl
 *
 * @author Georgii Vinogradov
 */

@Slf4j
@EnableSwagger
@RestController
@EnableExceptionHandler
@RequiredArgsConstructor
public class DialogControllerImpl implements DialogController {

    private final DialogService dialogService;

    @Override
    public ResponseEntity<DialogDto> createDialog(DialogDto dialogDto) {
        log.info("create dialog with conversationPartner1: {}, conversationPartner2: {}",
                dialogDto.getConversationPartner1(), dialogDto.getConversationPartner2());
        return ResponseEntity.ok(dialogService.createDialog(dialogDto));
    }

    @Override
    public ResponseEntity<MessageDto> createMessage(MessageDto messageDto) {
        log.info("create message with conversationPartner1: {}, conversationPartner2: {}",
                messageDto.getConversationPartner1(), messageDto.getConversationPartner2());
        return ResponseEntity.ok(dialogService.createMessage(messageDto));
    }

    @Override
    public ResponseEntity<DialogDto> getDialogByRecipientId(UUID recipientId) {
        log.info("get dialog by recipientId {}", recipientId);
        return ResponseEntity.ok(dialogService.getDialogByRecipientId(recipientId));
    }

    @Override
    public ResponseEntity updateReadStatus(UUID dialogId) {
        log.info("set all messages read for dialog id: {}", dialogId);
        dialogService.updateReadStatus(dialogId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Page<DialogDto>> getDialogs(Pageable pageable) {
        log.info("get dialogs");
        return ResponseEntity.ok(dialogService.getDialogs(pageable));
    }

    @Override
    public ResponseEntity<UnreadCountDto> getUnread() {
        log.info("get unread count");
        return ResponseEntity.ok(dialogService.getUnreadCountDto());
    }

    @Override
    public ResponseEntity<Page<MessageShortDto>> getMessages(UUID recipientId, Pageable pageable) {
        log.info("get messages with recipientId {}", recipientId);
        return ResponseEntity.ok(dialogService.getMessages(recipientId, pageable));
    }

}
