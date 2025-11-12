package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository extends Repository<Channel, UUID> {

  // 공통 CRUD 메서드는 Repository에서 상속받음


  Optional<Channel> findByName(String channelName);

}
