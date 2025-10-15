package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.dto.DeletedMessageDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.DeletedMessage;
import com.sprint.mission.discodeit.entity.Entity;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.util.AppendableObjectOutputStream;

import java.io.*;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

public class FileMessageService implements MessageRepository {

    private final String MESSAGE_DATA_PATH = "C:\\Users\\황준영\\Java-codeit\\7-sprint-mission\\src\\main\\java\\com\\sprint\\mission\\discodeit\\repository\\data\\";
    private final String DELETED_MESSAGE_DATA_PATH = "C:/Users/황준영/Java-codeit/7-sprint-mission/src/main/resources/data/deletedMessageRepository.cer";
    private File messageRepositoryFile = new File(MESSAGE_DATA_PATH);
    private File deletedMessageRepositoryFile = new File(DELETED_MESSAGE_DATA_PATH);
    private final User DEFAULT_SENDER = new User(UUID.randomUUID(), "DeletedUser", "DeletedUser", "codeit.org", true);
    public FileMessageService() {
        repositoryCheck();
    }

    @Override
    public MessageDto getMessageById(UUID messageId) {
        try (
                ObjectInputStream ois = new ObjectInputStream(
                        new FileInputStream(MESSAGE_DATA_PATH)
                )
        ) {
            List<Message> messageDb = (List<Message>) ois.readObject();
            Message result = messageDb.stream().filter(x -> x.getId().equals(messageId)).findFirst().get();
            return messageToMessageDto(result);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public MessageDto getMessageByName(String messageName) {
        try (
                ObjectInputStream ois = new ObjectInputStream(
                        new FileInputStream(MESSAGE_DATA_PATH)
                )
        ) {
            List<Message> messageDb = (List<Message>) ois.readObject();
            Message result = messageDb.stream().filter(x -> x.getContent().equals(messageName)).findFirst().get();
            return messageToMessageDto(result);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public MessageDto getMessage(MessageDto messageDto) {

        return getMessageById(messageDto.getId());
    }

    @Override
    public void saveMessage(MessageDto messageDto) {
        try(
                ObjectOutputStream oos = new AppendableObjectOutputStream(new FileOutputStream(MESSAGE_DATA_PATH,true))
                )
        {
            oos.writeObject(messageDtoToMessage(messageDto));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return;

    }

    @Override
    public void deleteMessage(MessageDto messageDto) {
        try(
                ObjectOutputStream oos = new AppendableObjectOutputStream(new FileOutputStream(DELETED_MESSAGE_DATA_PATH,true));
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(MESSAGE_DATA_PATH));
                ObjectOutputStream oosMessage = new ObjectOutputStream(new FileOutputStream(MESSAGE_DATA_PATH));
                )
        {
            List<Message> messageDb = (List<Message>) ois.readObject();
            messageDb.remove(messageDtoToMessage(messageDto));
            oosMessage.writeObject(messageDb);
            oos.writeObject(messageDtoToMessage(messageDto));
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public <T> void updateMessage(MessageDto messageDto, Message.messageElement messageElement, T updatedContent) {
        try(
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(MESSAGE_DATA_PATH));
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(MESSAGE_DATA_PATH));
                )
        {
            Message updatedMessage = messageDtoToMessage(messageDto);
            BiConsumer<Message, Object> editFunction = messageElement.setter;
            editFunction.accept(updatedMessage, updatedContent);
            updatedMessage.updateEntity();
            List<Message> messageDb = (List<Message>) ois.readObject();
            messageDb.remove(messageDtoToMessage(messageDto));
            messageDb.add(updatedMessage);
            oos.writeObject(messageDb);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public MessageDto[] getUpdatedMessage() {
        try(
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(MESSAGE_DATA_PATH));
                )
        {
            List<Message> messageDb = (List<Message>) ois.readObject();
            return messageDb.stream().filter(x-> x.getUpdatedAt()!= Entity.DEFAULT_UPDATED_AT).map(this::messageToMessageDto).toArray(MessageDto[]::new);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public DeletedMessageDto[] getDeletedMessage() {
        try(
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DELETED_MESSAGE_DATA_PATH));
                ){
            List<DeletedMessage> deletedMessageDb = (List<DeletedMessage>) ois.readObject();
            return deletedMessageDb.stream().map(this::deletedMessageToDeletedMessageDto).toArray(DeletedMessageDto[]::new);


        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public MessageDto[] getAllMessage() {
        try(
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(MESSAGE_DATA_PATH));
                ){
            List<Message> messageDb = (List<Message>) ois.readObject();
            return messageDb.stream().map(this::messageToMessageDto).toArray(MessageDto[]::new);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setDefaultSender(MessageDto messageDto) {
        try( ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(MESSAGE_DATA_PATH));
             ObjectInputStream ois = new ObjectInputStream(new FileInputStream(MESSAGE_DATA_PATH));
        ){
           List<Message> messageDb = (List<Message>) ois.readObject();
           Message targetMessage = messageDb.stream().filter(x->x.getId().equals(messageDto.getId())).findFirst().get();
           targetMessage.setSender(DEFAULT_SENDER);
           oos.writeObject(messageDb);
        }

        catch (Exception e){
            e.printStackTrace();
        }

    }

    private void repositoryCheck() {
        if (!messageRepositoryFile.exists()) {
            try {
                messageRepositoryFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!deletedMessageRepositoryFile.exists()) {
            try {
                deletedMessageRepositoryFile.createNewFile();
            } catch (Exception e) {
            }
        }
    }

    private Message messageDtoToMessage(MessageDto messageDto) {
        User sender = new User(messageDto.getSender().getId(), messageDto.getSender().getName(), messageDto.getSender().getNickname(), messageDto.getSender().getEmail(), messageDto.getSender().isOnline());
        return new Message(messageDto.getId(), messageDto.getContent(), sender, messageDto.isMarkDown());

    }

    private MessageDto messageToMessageDto(Message message) {
        UserDto tempSender = new UserDto(message.getSender().getId(), message.getSender().getName(), message.getSender().getNickname(), message.getSender().getEmail(), message.getSender().isOnline());
        return new MessageDto(message.getContent(), tempSender, message.isMarkDown());
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
