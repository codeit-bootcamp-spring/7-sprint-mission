package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.dto.DeletedMessageDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.DeletedMessage;
import com.sprint.mission.discodeit.entity.Entity;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageRepository;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.BiConsumer;

public class FileMessageRepository implements MessageRepository {
    private final ArrayList<Message> messageRepo ;
    private final ArrayList<DeletedMessage> deletedMessageRepo ;
    private final User DEFAULT_SENDER = new User(UUID.randomUUID(), "DeletedUser", "DeletedUser", "codeit.org", true);

    public FileMessageRepository() {
        this.messageRepo = new ArrayList<>();
        this.deletedMessageRepo = new ArrayList<>();
        resetMessageRepository();
    }

    @Override
    public MessageDto getMessageById(UUID messageId) {
        return messageRepo.stream().filter(x->x.getId().equals(messageId)).map(this::messageToMessageDto).findFirst().orElse(null);
    }

    @Override
    public MessageDto getMessageByName(String messageName) {
        return messageRepo.stream().filter(x->x.getContent().equals(messageName)).map(this::messageToMessageDto).findFirst().orElse(null);
    }

    @Override
    public MessageDto getMessage(MessageDto messageDto) {
        return getMessageById(messageDto.getId());
    }

    @Override
    public void saveMessage(MessageDto messageDto) {

        messageRepo.add(messageDtoToMessage(messageDto));

    }

    @Override
    public void deleteMessage(MessageDto messageDto) {
        if(messageDto == null){
            return;
        }
        deletedMessageRepo.add(messageDtoToDeletedMessage(messageDto));
        messageRepo.remove(messageRepo.stream().filter(x->x.getId().equals(messageDto.getId())).findFirst().orElse(null));
        return;

    }

    @Override
    public <T> void updateMessage(MessageDto messageDto, Message.messageElement messageElement, T updatedContent) {
        Message updatedMessage = messageRepo.stream().filter(x->x.getId().equals(messageDto.getId())).findFirst().orElse(null);
        BiConsumer<Message, Object> editFunction = messageElement.setter;
        editFunction.accept(updatedMessage, updatedContent);
        updatedMessage.updateEntity();

    }

    @Override
    public MessageDto[] getUpdatedMessage() {
        return messageRepo.stream().filter(x->x.getUpdatedAt()!= Entity.DEFAULT_UPDATED_AT).map(this::messageToMessageDto).toArray(MessageDto[]::new);
    }

    @Override
    public DeletedMessageDto[] getDeletedMessage() {
        return deletedMessageRepo.stream().map(this::deletedMessageToDeletedMessageDto).toArray(DeletedMessageDto[]::new);
    }

    @Override
    public MessageDto[] getAllMessage() {
        return messageRepo.stream().map(this::messageToMessageDto).toArray(MessageDto[]::new);
    }

    @Override
    public void setDefaultSender(MessageDto messageDto) {
        Message targetMessage = messageRepo.stream().filter(x->x.getId().equals(messageDto.getId())).findFirst().orElse(null);
        targetMessage.setSender(DEFAULT_SENDER);

    }

    @Override
    public void resetMessageRepository() {
        messageRepo.clear();
        deletedMessageRepo.clear();

    }

    private Message messageDtoToMessage(MessageDto messageDto) {
        User sender = new User(messageDto.getSender().getId(), messageDto.getSender().getName(), messageDto.getSender().getNickname(), messageDto.getSender().getEmail(), messageDto.getSender().isOnline());
        return new Message(messageDto.getId(), messageDto.getContent(), sender, messageDto.isMarkDown());

    }

    private MessageDto messageToMessageDto(Message message) {
        UserDto tempSender = new UserDto(message.getSender().getId(), message.getSender().getName(), message.getSender().getNickname(), message.getSender().getEmail(), message.getSender().isOnline());
        return new MessageDto(message.getId(),message.getContent(), tempSender, message.isMarkDown());
    }

    private DeletedMessage messageDtoToDeletedMessage(MessageDto messageDto){
        return new DeletedMessage(messageDto.getSender().getName(),messageDto.getContent());
    }
    //    private DeletedMessage deletedMessageDtoToDeletedMessage(DeletedMessageDto deletedMessageDto){
//        return new DeletedMessage(deletedMessageDto.getName(),deletedMessageDto.getContent());
//    }
    private DeletedMessageDto deletedMessageToDeletedMessageDto(DeletedMessage deletedMessage){
        return new DeletedMessageDto(deletedMessage.getSenderName(),deletedMessage.getContent());
    }
}
