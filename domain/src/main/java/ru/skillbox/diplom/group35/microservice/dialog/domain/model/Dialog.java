package ru.skillbox.diplom.group35.microservice.dialog.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.diplom.group35.library.core.model.base.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.Size;
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

    @Column(name = "conversation_partner_1", nullable = false)
    private UUID conversationPartner1;

    @Column(name = "conversation_partner_2", nullable = false)
    private UUID conversationPartner2;

    @OrderBy("time DESC")
    @Column(name = "last_message")
    @OneToMany(mappedBy = "dialog", fetch = FetchType.LAZY)
    private List<Message> lastMessage;

    @Override
    public String toString() {
        return "Dialog{" +
                "unreadCount=" + unreadCount +
                ", conversationPartner1=" + conversationPartner1 +
                ", conversationPartner2=" + conversationPartner2 +
                ", lastMessage=" + lastMessage +
                '}';
    }
}
