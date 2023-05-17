package ru.skillbox.diplom.group35.microservice.dialog.api.dto.dialog;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.skillbox.diplom.group35.library.core.dto.base.BaseDto;
import ru.skillbox.diplom.group35.microservice.dialog.api.dto.message.MessageDto;

import java.util.List;
import java.util.UUID;

/**
 * DialogDto
 *
 * @author Georgii Vinogradov
 */

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class DialogDto extends BaseDto {
    private Integer unreadCount;
    private UUID conversationPartner1;
    private UUID conversationPartner2;
    private List<MessageDto> lastMessage;
}
