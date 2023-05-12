package ru.skillbox.diplom.group35.microservice.dialog.api.dto.dialog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.skillbox.diplom.group35.library.core.dto.base.BaseSearchDto;

import java.util.UUID;

/**
 * DialogSearchDto
 *
 * @author Georgii Vinogradov
 */

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class DialogSearchDto extends BaseSearchDto {

    private UUID authorId;
    private UUID conversationPartner;

    public DialogSearchDto(UUID authorId) {
        this.authorId = authorId;
    }

}
