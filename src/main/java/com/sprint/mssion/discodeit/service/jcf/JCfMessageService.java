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
        if(!userRepo.containsKey(userId) ) {
            System.out.println("찾을 수 없는 유저입니다.");
            System.out.println("메세지 생성 실패");
            return null;
        }else if(!channelRepo.containsKey(channelId)) {
            System.out.println("찾을 수 없는 채널입니다.");
            System.out.println("메세지 생성 실패");
            return null;
        }

        Message newMessage =  new Message(message, channelId, userId);
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
        Message msg;
        if(msgRepo.containsKey(messageId)){
            msg = msgRepo.get(messageId);
            System.out.println(msg.toString());
            return msg;
        }
        System.out.println("찾을 수 없음.");
        return null;
    }

    @Override
    public List<Message> readAll() {
        System.out.println("메시지 전체 검색");
        List<Message> messages = new ArrayList<>();
        for(UUID key : msgRepo.keySet()){
            Message msg = msgRepo.get(key);
            messages.add(msg);
        }
        return messages;
    }

    @Override
    public Message update(UUID messageId, String message, UUID userId) {
        System.out.println("메시지 업데이트");
        if(msgRepo.containsKey(messageId)) {
            Message msg = msgRepo.get(messageId);
            msg.setMessage(message);
            msg.setUserId(userId);
            msg.getCommon().touch();
            System.out.println("변경");
            return msg;
        }
        System.out.println("찾을 수 없음.");
        return null;
    }

    @Override
    public void delete(UUID messageId) {
        System.out.println("메시지 삭제");
        if(!msgRepo.containsKey(messageId)) {
            System.out.println(" 찾을 수 없음.");
            return;
        }
        msgRepo.remove(messageId);
        channelRepo.remove(messageId);
        System.out.println("삭제 완료");


    }
}
