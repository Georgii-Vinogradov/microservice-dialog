package ru.skillbox.diplom.group35.microservice.dialog.api.resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group35.microservice.dialog.api.dto.dialog.DialogDto;
import ru.skillbox.diplom.group35.microservice.dialog.api.dto.message.MessageDto;
import ru.skillbox.diplom.group35.microservice.dialog.api.dto.message.MessageShortDto;
import ru.skillbox.diplom.group35.microservice.dialog.api.dto.message.UnreadCountDto;

import java.util.UUID;

/**
 * DIalogController
 *
 * @author Georgii Vinogradov
 */

@RequestMapping("/api/v1/dialogs")
public interface DialogController {

    @PostMapping("/createDialog")
    ResponseEntity<DialogDto> createDialog(@RequestBody DialogDto dialogDto);

    @PostMapping("/createMessage")
    ResponseEntity<MessageDto> createMessage(@RequestBody MessageDto messageDto);

    @PutMapping("/{dialogId}")
    ResponseEntity updateReadStatus(@PathVariable(name = "dialogId") UUID dialogId);

    @GetMapping
    ResponseEntity<Page<DialogDto>> getDialogs(Pageable pageable);

    @GetMapping("/unread")
    ResponseEntity<UnreadCountDto> getUnread();

    @GetMapping("/messages")
    ResponseEntity<Page<MessageShortDto>> getMessages(@RequestParam UUID companionId, Pageable pageable);
}
