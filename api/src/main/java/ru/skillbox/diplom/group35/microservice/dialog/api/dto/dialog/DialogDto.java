package ru.skillbox.diplom.group35.microservice.dialog.api.dto.dialog;

import lombok.Getter;
import lombok.Setter;
import ru.skillbox.diplom.group35.library.core.dto.base.BaseDto;
import ru.skillbox.diplom.group35.microservice.account.api.dto.AccountDto;
import ru.skillbox.diplom.group35.microservice.dialog.api.dto.message.MessageDto;

/**
 * DialogDto
 *
 * @author Georgii Vinogradov
 */

@Getter
@Setter
public class DialogDto extends BaseDto {
    private Integer unreadCount;
    private AccountDto conversationPartner;
    private MessageDto lastMessage;
}
