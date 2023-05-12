package ru.skillbox.diplom.group35.microservice.dialog.api.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.skillbox.diplom.group35.library.core.dto.base.BaseSearchDto;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class MessageSearchDto extends BaseSearchDto {
    private UUID authorId;
    private UUID recipientId;
    private String readStatus;
    private Long kafkaTimestamp;
}
