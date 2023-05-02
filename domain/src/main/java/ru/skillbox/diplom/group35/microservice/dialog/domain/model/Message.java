package ru.skillbox.diplom.group35.microservice.dialog.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.diplom.group35.library.core.model.base.BaseEntity;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Message
 *
 * @author Georgii Vinogradov
 */

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "message")
public class Message extends BaseEntity {

    @Column(name = "time", nullable = false)
    private ZonedDateTime time;

    @Column(name = "author_id", nullable = false)
    private UUID authorId;

    @Column(name = "recipient_id", nullable = false)
    private UUID recipientId;

    @Column(name = "message_text", columnDefinition = "TEXT")
    private String messageText;

    @Column(name = "read_status", columnDefinition = "VARCHAR(255)", nullable = false)
    private String readStatus;

    @ManyToOne
    @JoinColumn(name = "dialog_id", nullable = false)
    private Dialog dialog;
}
