package ru.skillbox.diplom.group35.microservice.dialog.api.dto.message;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.skillbox.diplom.group35.library.core.dto.base.BaseSearchDto;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class MessageSearchDto extends BaseSearchDto {
    private UUID conversationPartner1;
    private UUID conversationPartner2;
    private String readStatus;
}
