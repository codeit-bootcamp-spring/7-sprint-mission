package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.io.*;
import java.util.*;

public class FileMessageService implements MessageService {

    private static final String MSG_FILE_PATH = "messages.ser";
    private static final File FILE = new File(MSG_FILE_PATH);
    private final Map<UUID, Message> messageMap;

    public FileMessageService() {
        this.messageMap = loadData();
    }


    private Map<UUID, Message> loadData(){

        try (FileInputStream load = new FileInputStream(MSG_FILE_PATH);
             ObjectInputStream ois = new ObjectInputStream(load))
        {

            return (Map<UUID, Message>) ois.readObject();

        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
            return new HashMap();
        }
    }

    private void saveData(Map<UUID, Message> data){

        try (FileOutputStream fos = new FileOutputStream(MSG_FILE_PATH);
             ObjectOutputStream oos = new ObjectOutputStream(fos);
             ) {

            oos.writeObject(data);

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public Message createMessage(UUID senderId, UUID receiverId, String content) {
        Message message = new Message(senderId, receiverId, content);
        UUID messageId = message.getId();
        messageMap.put(messageId, message);
        saveData(messageMap);
        return message;
    }

    @Override
    public Message findMessage(UUID id) {
        return messageMap.get(id);
    }

    @Override
    public List<Message> findAllMessages() {
        return new ArrayList<>(messageMap.values());
    }

    @Override
    public Message updateMessage(UUID id, String content) {
        Message message = messageMap.get(id);
        if (message != null){
            message.setContent(content);
        }
        saveData(messageMap);
        return message;
    }

    @Override
    public void deleteMessage(UUID id) {
        messageMap.remove(id);
        saveData(messageMap);
    }
}
