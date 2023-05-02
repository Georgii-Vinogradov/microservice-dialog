package ru.skillbox.diplom.group35.microservice.dialog.api.dto.dialog;

import lombok.Data;
import ru.skillbox.diplom.group35.library.core.dto.base.BaseDto;
import ru.skillbox.diplom.group35.microservice.dialog.api.dto.message.MessageDto;

import java.util.UUID;

/**
 * DialogDto
 *
 * @author Georgii Vinogradov
 */

@Data
public class DialogDto extends BaseDto {
    private Integer unreadCount;
    private UUID conversationPartner;
    private MessageDto lastMessage;
}
