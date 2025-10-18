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
                .mapToObj(index -> allMessages.get(index));

        if(receiver instanceof User user2){
            return reversed.filter(m ->
                            (m.getSenderId().equals(user.getId()) && m.getReceiverId().equals(user2.getId())) ||
                                    (m.getSenderId().equals(user2.getId()) && m.getReceiverId().equals(user.getId())))
                    .findFirst().orElse(null);
        } else if(receiver instanceof Channel channel){
            return reversed.filter(m ->
                            m.getSenderId().equals(user.getId()) && m.getReceiverId().equals(channel.getId()))
                    .findFirst().orElse(null);
        } else {
            return null;
        }
    }

    @Override
    public List<Message> getMessagesBetween(User user1, User user2) {
        return messageRepository.findAll().stream()
                .filter(m -> (m.getSenderId().equals(user1.getId()) && m.getReceiverId().equals(user2.getId())) ||
                        (m.getSenderId().equals(user2.getId()) && m.getReceiverId().equals(user1.getId())))
                .toList();
    }

    @Override
    public List<Message> getAllMessagesByUser(User user) {
        return messageRepository.findAll().stream()
                .filter(m -> m.getSenderId().equals(user.getId()) || m.getReceiverId().equals(user.getId()))
                .toList();
    }

    @Override
    public List<Message> getAllByChannel(Channel channel) {
        return messageRepository.findAll().stream()
                .filter(m -> m.getReceiverId().equals(channel.getId()))
                .toList();
    }

    @Override
    public void updateMessage(UUID id, String content) {
        messageRepository.findById(id).ifPresent(message -> {
            message.setContents(content);
            messageRepository.update(message);
        });
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
