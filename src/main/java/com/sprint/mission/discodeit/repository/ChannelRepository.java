package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * ChannelRepository
 * -----------------
 * 채널 데이터의 저장/조회/삭제를 담당하는 저장소 계층 인터페이스입니다.
 * (실제 데이터 저장소는 메모리, DB 등 다양하게 구현될 수 있습니다.)
 */
public interface ChannelRepository extends JpaRepository<Channel, UUID> {
    List<Channel> findAllByChannelType(ChannelType channelType);
}
