package ru.skillbox.diplom.group35.microservice.dialog.api.dto.kafka;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.UUID;

/**
 * StreamingMessageDto
 *
 * @author Georgii Vinogradov
 */

@Data
@Schema(description = "Dto обертки сообщения")
public class StreamingMessageDto<T> {

    @Schema(description = "Тип вложенного сообщения (\"message\"/\"notification\")")
    private String type;

    @Schema(description = "UUID собеседника")
    private UUID recipientId;

    @Schema(description = "Dto сообщения")
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
    private T data;
}
