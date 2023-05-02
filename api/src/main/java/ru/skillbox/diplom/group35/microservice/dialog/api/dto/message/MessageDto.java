package ru.skillbox.diplom.group35.microservice.dialog.api.dto.message;

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
public class MessageDto extends BaseDto {
    private ZonedDateTime time;
    private UUID authorId;
    private UUID recipientId;
    private String messageText;
    private String readStatus;
    private UUID dialogId;
}
