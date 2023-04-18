package ru.skillbox.diplom.group35.microservice.dialog.api.dto.message;

import lombok.Getter;
import lombok.Setter;
import ru.skillbox.diplom.group35.library.core.dto.base.BaseDto;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * MessageShortDto
 *
 * @author Georgii Vinogradov
 */

@Getter
@Setter
public class MessageShortDto extends BaseDto {
    private ZonedDateTime time;
    private UUID authorId;
    private String messageText;
}
