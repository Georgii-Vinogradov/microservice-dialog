package ru.skillbox.diplom.group35.microservice.dialog.api.dto.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skillbox.diplom.group35.library.core.dto.base.BaseDto;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * MessageDto
 *
 * @author Georgii Vinogradov
 */

@Data
@Schema(description = "Dto сообщения")
public class MessageDto extends BaseDto {

    @Schema(description = "Дата и время отправки")
    private ZonedDateTime time;

    @Schema(description = "UUID первого собеседника")
    private UUID conversationPartner1;

    @Schema(description = "UUID второго собеседника")
    private UUID conversationPartner2;

    @Schema(description = "Текст сообщения")
    private String messageText;

    @Schema(description = "Статус прочтения: SENT, READ - отправлен, прочитан")
    private String readStatus;

    @Schema(description = "UUID диалога")
    private UUID dialogId;

    @JsonIgnore
    private Long kafkaTimestamp;
}
