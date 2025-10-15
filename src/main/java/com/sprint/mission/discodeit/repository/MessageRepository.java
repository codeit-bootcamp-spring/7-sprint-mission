package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.util.UUID;

// Message 전용 저장소 인터페이스
public interface MessageRepository extends CrudRepository<UUID, Message> {

}
