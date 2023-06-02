package ru.skillbox.diplom.group35.microservice.dialog.api.dto.dialog;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.skillbox.diplom.group35.library.core.dto.base.BaseDto;
import ru.skillbox.diplom.group35.library.core.dto.streaming.MessageDto;

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
@Schema(description = "Dto диалога")
public class DialogDto extends BaseDto {

    @Schema(description = "Количество непрочитанных сообщений диалога")
    private Integer unreadCount;

    @Schema(description = "UUID первого собеседника")
    private UUID conversationPartner1;

    @Schema(description = "UUID второго собеседника")
    private UUID conversationPartner2;

    @Schema(description = "Dto последнего сообщения")
    private List<MessageDto> lastMessage;
}
