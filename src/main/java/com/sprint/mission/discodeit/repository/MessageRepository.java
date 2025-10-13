package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * MessageRepository
 * -----------------
 * 메시지 데이터의 저장/조회/삭제를 담당하는 저장소 계층 인터페이스입니다.
 * (실제 데이터 저장소는 메모리, DB 등 다양하게 구현될 수 있습니다.)
 */
public interface MessageRepository {
    /** 메시지를 저장 */
    void save(Message message);

    /** 모든 메시지를 반환 */
    List<Message> findAll();

    /** ID(UUID)로 메시지를 조회 */
    Optional<Message> findById(UUID id);

    /** 메시지를 수정 (내용 변경 등) */
    void update(Message message);

    /** ID(UUID)로 메시지를 삭제 */
    void deleteById(UUID id);

    /** 특정 유저가 보낸 메시지들을 모두 삭제 */
    void deleteByUser(User user);
}
