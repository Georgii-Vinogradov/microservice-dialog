package ru.skillbox.diplom.group35.microservice.dialog.api.dto.message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * UnreadCountDto
 *
 * @author Georgii Vinogradov
 */

@Data
@AllArgsConstructor
@Schema(description = "Dto количества непрочитанных сообщений")
public class UnreadCountDto {

    @Schema(description = "Количество непрочитанных сообщений в диалоге")
    private Integer count;
}
