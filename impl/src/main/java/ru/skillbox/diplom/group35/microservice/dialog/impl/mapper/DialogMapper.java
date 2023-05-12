package ru.skillbox.diplom.group35.microservice.dialog.impl.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import ru.skillbox.diplom.group35.microservice.dialog.api.dto.dialog.DialogDto;
import ru.skillbox.diplom.group35.microservice.dialog.domain.model.Dialog;
import ru.skillbox.diplom.group35.microservice.dialog.domain.model.Message;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class DialogMapper {

    @Autowired
    private MessageMapper messageMapper;

    public DialogDto toDto(Dialog dialog, Message lastMessage) {
        DialogDto dialogDto = new DialogDto();
        dialogDto.setId(dialog.getId());
        dialogDto.setIsDeleted(dialog.getIsDeleted());
        dialogDto.setConversationPartner1(dialog.getConversationPartner1());
        dialogDto.setConversationPartner2(dialog.getConversationPartner2());
        dialogDto.setUnreadCount(dialog.getUnreadCount());
        if (lastMessage != null) {
            dialogDto.setLastMessage(List.of(messageMapper.toMessageDto(lastMessage)));
        }
        return dialogDto;
    }

    public Dialog toDialog(DialogDto dialogDto) {
        Dialog dialog = new Dialog();
        dialog.setId(dialogDto.getId());
        dialog.setIsDeleted(dialogDto.getIsDeleted());
        dialog.setUnreadCount(dialogDto.getUnreadCount());
        dialog.setConversationPartner1(dialogDto.getConversationPartner1());
        dialog.setConversationPartner2(dialogDto.getConversationPartner2());
        return dialog;
    }
}
