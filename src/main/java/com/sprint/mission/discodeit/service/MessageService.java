package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    Message createMessage(UUID senderId, UUID receiverId, String content); // 메시지 생성
    Message findMessage(UUID id); // 메시지 찾기
    List<Message> findAllMessages(); // 모든 메시지 찾기
    Message updateMessage(UUID id, String content); //메시지 수정(업데이트)
    void deleteMessage(UUID id); // 메시지 삭제
}
