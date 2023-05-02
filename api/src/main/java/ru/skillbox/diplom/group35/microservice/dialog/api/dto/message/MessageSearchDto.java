package ru.skillbox.diplom.group35.microservice.dialog.api.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skillbox.diplom.group35.library.core.dto.base.BaseSearchDto;

import java.util.UUID;

@Data
@AllArgsConstructor
public class MessageSearchDto extends BaseSearchDto {
    private Boolean isDeleted;
    private UUID authorId;
    private UUID recipientId;
    private String readStatus;
}
