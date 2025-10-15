package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * JCFMessageService
 * -----------------
 * MessageService 인터페이스의 구현체로,
 * Repository를 주입받아 실제 메시지 처리 로직을 수행합니다.
 *
 * - 비즈니스 로직: 메시지 생성, 필터링, 최신 메시지 조회 등
 * - 데이터 저장/조회: MessageRepository에 위임
 */
public class JCFMessageService implements MessageService {
    private final MessageRepository messageRepository;

    /**
     * 생성자에서 Repository를 주입받습니다.
     * (의존성 주입 방식 - DI)
     */
    public JCFMessageService(MessageRepository JCFMessageRepository) {
        this.messageRepository = JCFMessageRepository;
    }

    /**
     * 새로운 메시지를 생성하여 Repository에 저장
     */
    @Override
    public <T> void createMessage(User user, T receiver, String content) {
        Message newMessage = null;

        // 수신자가 User인 경우
        if(receiver instanceof User user2){
            newMessage = new Message(user.getId(), user2.getId(), Message.ReceiveType.USER, content);
        // 수신자가 Channel인 경우
        } else if(receiver instanceof Channel channel){
            newMessage = new Message(user.getId(), channel.getId(), Message.ReceiveType.CHANNEL, content);
        } else return; // 그 외의 타입은 무시

        messageRepository.save(newMessage);
    }

    /**
     * 특정 유저/채널과의 최신 메시지를 조회
     */
    @Override
    public <T> Message getLastestMessage(User user, T receiver) {
        List<Message> allMessages = messageRepository.findAll();

        // 최신순(역순)으로 순회
        Stream<Message> reversed = IntStream.iterate(allMessages.size() - 1, i -> i - 1)
                .limit(allMessages.size())
                .mapToObj(allMessages::get);

        if (receiver instanceof User user2) {
            // 유저 간의 대화 중 가장 마지막 메시지
            return reversed.filter(m ->
                            (m.getSenderId() == user.getId() && m.getReceiverId() == user2.getId()) ||
                                    (m.getSenderId() == user2.getId() && m.getReceiverId() == user.getId()))
                    .findFirst().orElse(null);
        } else if (receiver instanceof Channel channel) {
            // 특정 채널에 보낸 마지막 메시지
            return reversed.filter(m ->
                            m.getSenderId() == user.getId() && m.getReceiverId() == channel.getId())
                    .findFirst().orElse(null);
        }

        return null;
    }

    /**
     * 두 유저 간의 모든 메시지 목록 반환
     */
    @Override
    public List<Message> getMessagesBetween(User user1, User user2) {
        return messageRepository.findAll().stream()
                .filter(m -> (m.getSenderId() == user1.getId() && m.getReceiverId() == user2.getId()) ||
                        (m.getSenderId() == user2.getId() && m.getReceiverId() == user1.getId()))
                .toList();
    }

    /**
     * 특정 유저가 보낸/받은 모든 메시지 조회
     */
    @Override
    public List<Message> getAllMessagesByUser(User user) {
        return messageRepository.findAll().stream()
                .filter(m -> m.getSenderId() == user.getId() || m.getReceiverId() == user.getId())
                .toList();
    }

    /**
     * 특정 채널에 포함된 모든 메시지 조회
     */
    @Override
    public List<Message> getAllByChannel(Channel channel) {
        return messageRepository.findAll().stream()
                .filter(m -> m.getReceiverId() == channel.getId())
                .toList();
    }

    /**
     * 메시지 내용(content) 수정
     */
    @Override
    public void updateMessage(UUID id, String content) {
        // 특정 id를 가진 메시지가 존재한다면 실행
        messageRepository.findById(id).ifPresent(message -> {
            message.setContents(content);
            messageRepository.update(message);
        });
    }

    /**
     * 특정 메시지(UUID 기반) 삭제
     */
    @Override
    public void deleteMessage(UUID id) {
        messageRepository.deleteById(id);
    }

    /**
     * 특정 유저가 보낸 모든 메시지 삭제
     */
    @Override
    public void deleteMessagesByUser(User user) {
        messageRepository.deleteByUser(user);
    }
}
