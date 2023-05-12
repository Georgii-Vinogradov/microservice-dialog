package ru.skillbox.diplom.group35.microservice.dialog.api.dto.dialog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.skillbox.diplom.group35.library.core.dto.base.BaseSearchDto;

import java.util.UUID;

/**
 * DialogSearchDto
 *
 * @author Georgii Vinogradov
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class DialogSearchDto extends BaseSearchDto {
    private UUID conversationPartner1;
    private UUID conversationPartner2;
}
