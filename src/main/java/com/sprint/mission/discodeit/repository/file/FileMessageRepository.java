package com.sprint.mission.discodeit.repository.file;


import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.util.StaticString;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

import static com.sprint.mission.discodeit.service.util.StaticString.*;


@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "file",
        matchIfMissing = false
)
public class FileMessageRepository implements MessageRepository {

    private final String MESSAGE_DATA_PATH ;
    private final File messageRepositoryFile ;


    public FileMessageRepository(Environment env) {
        MESSAGE_DATA_PATH = env.getProperty(DISCODEIT_DIRECTORY)+"messageRepository.ser";
        messageRepositoryFile = new File(MESSAGE_DATA_PATH);
        repositoryCheck();
        resetMessageRepository();
    }

    @Override
    public Optional<Message> getMessageById(UUID messageId) {
        return Optional.ofNullable(loadAllMessage().get(messageId));

    }

    @Override
    public Optional<Message> getMessageByName(String messageName) {
        return loadAllMessage().values().stream().filter(x->x.getContent().equals(messageName)).findFirst();
    }

    @Override
    public Optional<Message> getMessage(Message message) {

        return getMessageById(message.getId());
    }

    @Override
    public Message saveMessage(Message message) {
        Map<UUID,Message> messageDb = loadAllMessage();
        messageDb.put(message.getId(),message);
        saveAllMessage(messageDb);
        return message;


    }

    @Override
    public void deleteMessage(Message message) {
        Map<UUID,Message> messageDb = loadAllMessage();
        messageDb.remove(message.getId());
        saveAllMessage(messageDb);

    }

    @Override
    public <T> void updateMessage(Message message ) {
        Map<UUID,Message> messageDb = loadAllMessage();
        messageDb.remove(message.getId());
        messageDb.put(message.getId(),message);
        saveAllMessage(messageDb);


    }


    @Override
    public List<Message> getUpdatedMessage() {

        return loadAllMessage().values()
                .stream()
                .filter(x->x.getUpdatedAt()!= x.getCreatedAt())
                .toList();

    }



    @Override
    public List<Message> getAllMessage() {
        return loadAllMessage().values().stream().toList();
    }


    @Override
    public void resetMessageRepository() {

        saveAllMessage(new HashMap<>());

    }

    private void repositoryCheck() {
        if (!messageRepositoryFile.exists()) {
            try {
                messageRepositoryFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


    private void saveAllMessage(Map<UUID,Message>messageList){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(messageRepositoryFile,false));){
            oos.writeObject(messageList);
            oos.flush();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private Map<UUID,Message> loadAllMessage(){
        if (!messageRepositoryFile.exists() || messageRepositoryFile.length() == 0) {
            return new HashMap<>(); // 빈 파일이면 빈 리스트 반환
        }
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(messageRepositoryFile));){
            return (Map<UUID, Message>) ois.readObject();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new HashMap<>();
    }




}
