package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.enums.ChannelType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {
    // Todo: 설계 상 Public으로 고정
    // Private은 로직 상 조회할 필요가 없기 때문
    List<Channel> findAllByTypeOrIdIn(ChannelType channelType, List<UUID> channelIds);
}
