package ru.skillbox.diplom.group35.microservice.dialog.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.diplom.group35.library.core.model.base.BaseEntity;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

/**
 * Dialog
 *
 * @author Georgii Vinogradov
 */

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "dialog")
public class Dialog extends BaseEntity {

    @Column(name = "unread_count")
    private Integer unreadCount;

    @Column(name = "author_id", nullable = false)
    private UUID authorId;

    @Column(name = "conversation_partner", nullable = false)
    private UUID conversationPartner;

    @OrderBy("time DESC")
    @Column(name = "last_message")
    @OneToMany(mappedBy = "dialog", fetch = FetchType.LAZY)
    private List<Message> lastMessage;
}
