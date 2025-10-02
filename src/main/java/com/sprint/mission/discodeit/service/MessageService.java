package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    Message create(Message message);               // 생성

    Message read(UUID id);                   // 단건 조회
    List<Message> readAll();                 // 전체 조회

    Message update(UUID id, Message message);      // 수정

    void delete(UUID id);              // 삭제
}
