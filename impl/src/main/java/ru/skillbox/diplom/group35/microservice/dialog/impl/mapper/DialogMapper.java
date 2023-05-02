package ru.skillbox.diplom.group35.microservice.dialog.impl.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import ru.skillbox.diplom.group35.microservice.dialog.api.dto.dialog.DialogDto;
import ru.skillbox.diplom.group35.microservice.dialog.domain.model.Dialog;
import ru.skillbox.diplom.group35.microservice.dialog.domain.model.Message;

import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class DialogMapper {

    @Autowired
    private MessageMapper messageMapper;

    public DialogDto toDto(Dialog dialog, Message lastMessage) {
        DialogDto dialogDto = new DialogDto();
        dialogDto.setId(dialog.getId());
        dialogDto.setIsDeleted(dialog.getIsDeleted());
        dialogDto.setConversationPartner(dialog.getConversationPartner());
        dialogDto.setUnreadCount(dialog.getUnreadCount());
        dialogDto.setLastMessage(messageMapper.toMessageDto(lastMessage));
        return dialogDto;
    }

    public Dialog toDialog(DialogDto dialogDto, UUID authorId) {
        Dialog dialog = new Dialog();
        dialog.setId(dialogDto.getId());
        dialog.setIsDeleted(dialogDto.getIsDeleted());
        dialog.setAuthorId(authorId);
        dialog.setUnreadCount(dialogDto.getUnreadCount());
        dialog.setConversationPartner(dialogDto.getConversationPartner());
        return dialog;
    }
}
