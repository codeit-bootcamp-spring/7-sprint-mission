package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;
import java.util.stream.IntStream;

public class JCFMessageService implements MessageService {
    private final List<Message> messageStore = new ArrayList<>();

    @Override
    public void createMessage(User sender, User receiver, String content) {
        Message newMessage = new Message(sender.getId(), receiver.getId(), content);
        messageStore.add(newMessage);
    }


    @Override
    public Message getLastestMessage(User user1, User user2) {
//        Message lastestMessage = null;
//
//        for(int i = messageStore.size()-1; i >= 0; i--) {
//            //서로 주고 받은 메시지라면
//            if ((messageStore.get(i).getSenderId() == user1.getId() && messageStore.get(i).getReceiverId() == user2.getId())
//                    || (messageStore.get(i).getSenderId() == user2.getId() && messageStore.get(i).getReceiverId() == user1.getId())) {
//                lastestMessage = messageStore.get(i);
//                break;
//            }
//        }

        Message lastestMessage = IntStream.iterate(messageStore.size() - 1, i -> i - 1)
                .limit(messageStore.size())
                .mapToObj(i -> messageStore.get(i))
                .filter(m -> (m.getSenderId() ==  user1.getId() && m.getReceiverId() == user2.getId()) ||
                        (m.getSenderId() == user2.getId() && m.getReceiverId() == user1.getId()))
                .findFirst()
                .orElse(null);

        return lastestMessage;
    }

    @Override
    public List<Message> getMessagesBetween(User user1, User user2) {
//        List<Message> messages = new ArrayList<>();
//
//        for(int i = 0; i < messageStore.size(); i++){
//            //서로 주고 받은 메시지라면
//            if((messageStore.get(i).getSenderId() == user1.getId() && messageStore.get(i).getReceiverId() == user2.getId())
//                    || (messageStore.get(i).getSenderId() == user2.getId() && messageStore.get(i).getReceiverId() == user1.getId())){
//                messages.add(messageStore.get(i));
//            }
//        }

        List<Message> messages = messageStore.stream()
                .filter(m -> (m.getSenderId() == user1.getId() && m.getReceiverId() == user2.getId()) ||
                        (m.getSenderId() == user2.getId() && m.getReceiverId() == user1.getId()))
                .toList();

        return messages;
    }

    @Override
    public List<Message> getAllMessagesByUser(User user) {
//        List<Message> messages = new ArrayList<>();
//        for(int i = 0; i < messageStore.size(); i++){
//            if(messageStore.get(i).getSenderId() == user.getId() || messageStore.get(i).getReceiverId() == user.getId()){
//                messages.add(messageStore.get(i));
//            }
//        }

        List<Message> messages = messageStore.stream()
                .filter(m -> m.getSenderId() == user.getId() || m.getReceiverId() == user.getId())
                .toList();

        return messages;
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
