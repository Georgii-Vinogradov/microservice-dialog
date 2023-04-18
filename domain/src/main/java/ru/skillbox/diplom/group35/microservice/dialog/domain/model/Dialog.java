package ru.skillbox.diplom.group35.microservice.dialog.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.diplom.group35.library.core.model.base.BaseEntity;

import javax.persistence.*;
import java.util.Set;
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

    @Column(name = "conversation_partner")
    private UUID conversationPartner;

    @OneToOne
    @JoinColumn(name = "last_message", referencedColumnName = "id")
    private Message lastMessage;

    @OneToMany
    private Set<Message> messages;
}
