package ru.skillbox.diplom.group35.microservice.dialog.api.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * UnreadCountDto
 *
 * @author Georgii Vinogradov
 */

@Data
@AllArgsConstructor
public class UnreadCountDto {
    private Integer count;
}
