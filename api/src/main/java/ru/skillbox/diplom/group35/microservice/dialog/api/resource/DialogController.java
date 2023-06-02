package ru.skillbox.diplom.group35.microservice.dialog.api.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group35.library.core.dto.streaming.MessageDto;
import ru.skillbox.diplom.group35.microservice.dialog.api.dto.dialog.DialogDto;
import ru.skillbox.diplom.group35.microservice.dialog.api.dto.message.MessageShortDto;
import ru.skillbox.diplom.group35.microservice.dialog.api.dto.message.UnreadCountDto;

import java.util.UUID;


/**
 * DIalogController
 *
 * @author Georgii Vinogradov
 */


@Tag(name = "Dialog service", description = "Работа с диалогами")
@ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
@RequestMapping(value = "/api/v1/dialogs", produces = MediaType.APPLICATION_JSON_VALUE)
public interface DialogController {

    @PostMapping("/createDialog")
    @SecurityRequirement(name = "JWT")
    @Operation(description = "Создание диалога, для тестирования")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    ResponseEntity<DialogDto> createDialog(@RequestBody DialogDto dialogDto);

    @PostMapping("/createMessage")
    @SecurityRequirement(name = "JWT")
    @Operation(description = "Создание сообщения, для тестирования")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    ResponseEntity<MessageDto> createMessage(@RequestBody MessageDto messageDto);

    @PutMapping("/{dialogId}")
    @SecurityRequirement(name = "JWT")
    @Operation(description = "Обновление статуса сообщений")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content)
    ResponseEntity updateReadStatus(@PathVariable(name = "dialogId") UUID dialogId);

    @GetMapping
    @SecurityRequirement(name = "JWT")
    @Operation(description = "Получение списка диалогов")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    ResponseEntity<Page<DialogDto>> getDialogs(Pageable pageable);

    @GetMapping("/recipientId/{id}")
    @SecurityRequirement(name = "JWT")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @Operation(description = "Получение(создание) диалога между пользователями")
    ResponseEntity<DialogDto> getDialogByRecipientId(@PathVariable(name = "id") UUID recipientId);

    @GetMapping("/unread")
    @SecurityRequirement(name = "JWT")
    @Operation(description = "Получение количества непрочитанных сообщений")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    ResponseEntity<UnreadCountDto> getUnread();

    @GetMapping("/messages")
    @SecurityRequirement(name = "JWT")
    @Operation(description = "Получение сообщений сообщений диалога")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    ResponseEntity<Page<MessageShortDto>> getMessages(@RequestParam UUID recipientId, Pageable pageable);
}
