package ru.skillbox.diplom.group35.microservice.dialog.api.dto.message;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * MessagesRs
 *
 * @author Georgii Vinogradov
 */

@Getter
@Setter
public class MessagesRs {
    private String error;
    private String errorDescription;
    private ZonedDateTime timestamp;
    private Integer total;
    private Integer offset;
    private Integer perPage;
    private List<MessageShortDto> data;
}
