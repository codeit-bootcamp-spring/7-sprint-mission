package com.sprint.mssion.discodeit.service.jcf;

import com.sprint.mssion.discodeit.entity.Message;
import com.sprint.mssion.discodeit.service.MessageService;

import java.util.*;

import static com.sprint.mssion.discodeit.service.jcf.JCFChannelService.channelRepo;
import static com.sprint.mssion.discodeit.service.jcf.JCFUserservice.userRepo;

public class JCfMessageService implements MessageService {
    public final static Map<UUID, Message> msgRepo;

    static {
        msgRepo = new HashMap<>();
    }

    @Override
    public Message create(String message, UUID channelId, UUID userId) {
        System.out.println("메세지 생성");
        if (!userRepo.containsKey(userId)) {
            throw new NoSuchElementException("찾을 수 없는 유저 : " + userId);
        } else if (!channelRepo.containsKey(channelId)) {
            throw new NoSuchElementException("찾을 수 없는 채널 : " + channelId);
        }

        Message newMessage = new Message(message, channelId, userId);
        msgRepo.put(newMessage.getCommon().getId(), newMessage);

        // 채널 인스턴스에 유저와 메시지 리스트 원소 추가
        channelRepo.get(channelId).addJoiner(userId);
        channelRepo.get(channelId).addMessages(newMessage.getCommon().getId());

        // 유저 인스턴스에 참여채널 추가
        userRepo.get(userId).addChannel(channelId);

        return newMessage;
    }

    @Override
    public Message read(UUID messageId) {
        System.out.println("메시지 단건 검색");
        if (msgRepo.containsKey(messageId)) {
            return msgRepo.get(messageId);
        }
        throw new NoSuchElementException("찾을 수 없는 메시지: " + messageId);
    }

    @Override
    public List<Message> readAll() {
        System.out.println("메시지 전체 검색");
        List<Message> messages = new ArrayList<>();
        for (UUID key : msgRepo.keySet()) {
            Message msg = msgRepo.get(key);
            messages.add(msg);
        }
        return messages;
    }

    @Override
    public Message update(UUID messageId, String message) {
        System.out.println("메시지 업데이트");
        if (msgRepo.containsKey(messageId)) {
            Message msg = msgRepo.get(messageId);
            msg.setMessage(message);
            msg.getCommon().touch();
            System.out.println("변경");
            return msg;
        }
        throw new NoSuchElementException("찾을 수 없는 메시지" + messageId);
    }

    @Override
    public void delete(UUID messageId) {
        System.out.println("메시지 삭제");
        if (msgRepo.containsKey(messageId)) {
            msgRepo.remove(messageId);
            channelRepo.remove(messageId);
            System.out.println("삭제 완료");
            return;
        }
        throw new NoSuchElementException("찾을 수 없는 메시지" + messageId);
    }
}
