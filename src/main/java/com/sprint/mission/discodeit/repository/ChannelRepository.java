package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import java.util.UUID;

// Channel 전용 저장소 인터페이스
public interface ChannelRepository extends  CrudRepository<UUID, Channel> {
}
