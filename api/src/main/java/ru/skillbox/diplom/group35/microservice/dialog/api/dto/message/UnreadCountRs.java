package ru.skillbox.diplom.group35.microservice.dialog.api.dto.message;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * UnreadCountRs
 *
 * @author Georgii Vinogradov
 */

@Getter
@Setter
public class UnreadCountRs {
    private String error;
    private String errorDescription;
    private ZonedDateTime timestamp;
    private List<UnreadCountDto> data;
}
