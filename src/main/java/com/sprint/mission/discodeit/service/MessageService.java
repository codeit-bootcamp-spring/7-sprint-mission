package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

/**
 * ✅ Message 도메인에 대한 CRUD 규칙(인터페이스)
 * - Message는 누가(senderId) 어느 채널(channelId)에 어떤 내용(content)을 남겼는지를 표현한다.
 * - id, createdAt, updatedAt은 공통 부모(DefEntity)에서 관리된다.
 */
public interface MessageService {

    // C: 메시지 생성 (필수값: senderId, channelId, content)
    Message create(UUID senderId, UUID channelId, String content);

    // R: 단건 조회
    Message read(UUID id);

    // R: 전체 조회
    List<Message> readAll();

    // U: 내용 수정
    Message updateContent(UUID id, String newContent);

    // D: 삭제
    boolean delete(UUID id);
}