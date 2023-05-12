package ru.skillbox.diplom.group35.microservice.dialog.api.dto.message;

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
public class MessageShortDto extends BaseDto {
    private ZonedDateTime time;
    private UUID conversationPartner1;
    private UUID conversationPartner2;
    private String messageText;
}
