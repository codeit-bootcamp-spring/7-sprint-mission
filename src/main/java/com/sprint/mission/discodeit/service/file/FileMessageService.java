package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.io.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * FileMessageService
 * -----------------
 * MessageService 인터페이스의 파일 기반 구현체로,
 * FileMessageRepository를 주입받아 메시지 처리 로직을 수행합니다.
 *
 * - 기능은 JCFMessageService와 동일하나,
 *   데이터 저장 및 조회가 파일(.sav) 단위로 이루어집니다.
 */
public class FileMessageService implements MessageService {
    private final MessageRepository messageRepository;

    public FileMessageService(MessageRepository fileMessageRepository) {
        this.messageRepository = fileMessageRepository;
    }

    @Override
    public <T> void createMessage(User user, T receiver, String content) {
        Message newMessage;

        if(receiver instanceof User user2){
            newMessage = new Message(user.getId(), user2.getId(), Message.ReceiveType.USER, content);
        } else if(receiver instanceof Channel channel){
            newMessage = new Message(user.getId(), channel.getId(), Message.ReceiveType.CHANNEL, content);
        } else return;

        messageRepository.save(newMessage);
    }

    @Override
    public <T> Message getLastestMessage(User user, T receiver) {
        List<Message> allMessages = messageRepository.findAll();

        // 최신순(역순)으로 순회
        Stream<Message> reversed = IntStream.iterate(allMessages.size() - 1, i -> i - 1)
                .limit(allMessages.size())
                .mapToObj(allMessages::get);

        if(receiver instanceof User user2){
            return reversed.filter(m ->
                            (user.getId().equals(m.getSenderId()) && user2.getId().equals(m.getReceiverId())) ||
                                    (user2.getId().equals(m.getSenderId()) && user.getId().equals(m.getReceiverId())))
                    .findFirst().orElse(null);
        } else if(receiver instanceof Channel channel){
            return reversed.filter(m ->
                            user.getId().equals(m.getSenderId()) && channel.getId().equals(m.getReceiverId()))
                    .findFirst().orElse(null);
        } else {
            return null;
        }
    }

    @Override
    public List<Message> getMessagesBetween(User user1, User user2) {
        return messageRepository.findAll().stream()
                .filter(m -> (user1.getId().equals(m.getSenderId()) && user2.getId().equals(m.getReceiverId())) ||
                        (user2.getId().equals(m.getSenderId()) && user1.getId().equals(m.getReceiverId())))
                .toList();
    }

    @Override
    public List<Message> getAllMessagesByUser(User user) {
        return messageRepository.findAll().stream()
                .filter(m -> user.getId().equals(m.getSenderId()) || user.getId().equals(m.getReceiverId()))
                .toList();
    }

    @Override
    public List<Message> getAllByChannel(Channel channel) {
        return messageRepository.findAll().stream()
                .filter(m -> channel.getId().equals(m.getReceiverId()))
                .toList();
    }

    @Override
    public void updateMessage(UUID id, String content) {
        Message message = messageRepository.findById(id);
        message.setContents(content);
        messageRepository.update(message);
    }

    @Override
    public void deleteMessage(UUID id) {
        messageRepository.deleteById(id);
    }

    @Override
    public void deleteMessagesByUser(User user) {
        messageRepository.deleteByUser(user);
    }
}
