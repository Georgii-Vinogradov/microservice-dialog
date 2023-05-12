package ru.skillbox.diplom.group35.microservice.dialog.api.dto.kafka;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import java.util.UUID;

/**
 * StreamingMessageDto
 *
 * @author Georgii Vinogradov
 */

@Data
public class StreamingMessageDto<T> {

    private String type;

    private UUID recipientId;

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
    private T data;
}
