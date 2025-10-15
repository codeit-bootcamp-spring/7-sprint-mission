package com.sprint.mission.discodeit.presentation;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }
    public Message createMessageController(MessageDto messageDto){
        Message message = messageInverter(messageDto);
        messageService.createMessage(message);
        return message;
    }
    public void readMessageController(MessageDto messageDto){
        Message message = messageInverter(messageDto);
        messageService.readMessage(message);
    }
    public void deleteMessageController(MessageDto messageDto){
        Message message = messageInverter(messageDto);
        messageService.deleteMessage(message);
    }
    public void readAllMessageController(){
        messageService.readAllMessage();
    }
    public void updateMessageController(MessageDto messageDto, Message.messageElement messageElement, Object updatedContent){
        Message message = messageInverter(messageDto);
        messageService.updateMessage(message, messageElement, updatedContent);
    }
    public void readUpdatedMessageController(){
        messageService.readUpdatedMessage();
    }
    public void readDeletedMessageController(){
        messageService.readDeletedMessage();
    }

    private Message messageInverter(MessageDto messageDto){

        User sender = new User(messageDto.getSender().getId(), messageDto.getSender().getName(), messageDto.getSender().getNickname(), messageDto.getSender().getEmail(), messageDto.getSender().isOnline());
        return new Message(messageDto.getId(), messageDto.getContent(), sender, messageDto.isMarkDown());
    }
}
