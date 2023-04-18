package ru.skillbox.diplom.group35.microservice.dialog.api.dto.dialog;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DialogsRs
 *
 * @author Georgii Vinogradov
 */

@Getter
@Setter
public class DialogsRs {
    private String error;
    private String errorDescription;
    private ZonedDateTime timestamp;
    private Integer total;
    private Integer offset;
    private Integer perPage;
    private UUID currentUserId;
    private List<DialogDto> data;
}
