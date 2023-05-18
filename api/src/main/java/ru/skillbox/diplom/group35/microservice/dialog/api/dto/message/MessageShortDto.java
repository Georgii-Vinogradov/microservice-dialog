package ru.skillbox.diplom.group35.microservice.dialog.api.dto.message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skillbox.diplom.group35.library.core.dto.base.BaseDto;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * MessageShortDto
 *
 * @author Georgii Vinogradov
 */

@Data
@Schema(description = "Dto сообщения краткое")
public class MessageShortDto extends BaseDto {

    @Schema(description = "Дата и время отправки")
    private ZonedDateTime time;

    @Schema(description = "UUID первого собеседника")
    private UUID conversationPartner1;

    @Schema(description = "UUID второго собеседника")
    private UUID conversationPartner2;

    @Schema(description = "Текст сообщения")
    private String messageText;
}
