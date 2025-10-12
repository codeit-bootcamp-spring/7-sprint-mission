package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;
import java.util.stream.IntStream;

public class JCFMessageService<T> implements MessageService<T> {
    private final List<Message> messageStore = new ArrayList<>();

    @Override
    public void createMessage(User user, T receiver, String content) {
        Message newMessage = null;

        if(receiver instanceof User user2){
            newMessage = new Message(user.getId(), user2.getId(), Message.ReceiveType.USER, content);
        } else if(receiver instanceof Channel channel){
            newMessage = new Message(user.getId(), channel.getId(), Message.ReceiveType.CHANNEL, content);
        } else return;

        messageStore.add(newMessage);
    }

    @Override
    public Message getLastestMessage(User user, T receiver) {
        Message lastestMessage;
        if(receiver instanceof User user2){
            lastestMessage = IntStream.iterate(messageStore.size() - 1, i -> i - 1)
                    .limit(messageStore.size())
                    .mapToObj(i -> messageStore.get(i))
                    .filter(m -> (m.getSenderId() ==  user.getId() && m.getReceiverId() == user2.getId()) ||
                            (m.getSenderId() == user2.getId() && m.getReceiverId() == user.getId()))
                    .findFirst()
                    .orElse(null);
        } else if(receiver instanceof Channel channel){
            lastestMessage = IntStream.iterate(messageStore.size() - 1, i -> i - 1)
                    .limit(messageStore.size())
                    .mapToObj(i -> messageStore.get(i))
                    .filter(m -> m.getSenderId() ==  user.getId() && m.getReceiverId() == channel.getId())
                    .findFirst()
                    .orElse(null);
        } else {
            return null;
        }

        return lastestMessage;
    }

    @Override
    public List<Message> getMessagesBetween(User user1, User user2) {
        List<Message> messages = messageStore.stream()
                .filter(m -> (m.getSenderId() == user1.getId() && m.getReceiverId() == user2.getId()) ||
                        (m.getSenderId() == user2.getId() && m.getReceiverId() == user1.getId()))
                .toList();

        return messages;
    }

    @Override
    public List<Message> getAllMessagesByUser(User user) {
        List<Message> messages = messageStore.stream()
                .filter(m -> m.getSenderId() == user.getId() || m.getReceiverId() == user.getId())
                .toList();

        return messages;
    }

    @Override
    public List<Message> getAllByChannel(Channel channel) {
        return messageStore.stream()
                .filter(m -> m.getReceiverId() == channel.getId())
                .toList();
    }

    @Override
    public void updateMessage(UUID id, String content) {
        for(int i = 0; i < messageStore.size(); i++){
            if(messageStore.get(i).getId() == id){
                Message message = messageStore.get(i);
                message.setContents(content);
                messageStore.set(i, message);
                break;
            }
        }
    }

    @Override
    public void deleteMessage(UUID id) {
        for(int  i = 0; i < messageStore.size(); i++){
            if(messageStore.get(i).getId() == id){
                messageStore.remove(i);
                break;
            }
        }
    }

    @Override
    public void delMessageByUser(User user) {
        for(int i = 0; i < messageStore.size(); i++){
            if(messageStore.get(i).getSenderId() == user.getId()){
                messageStore.remove(i);
            }
        }
    }
}
