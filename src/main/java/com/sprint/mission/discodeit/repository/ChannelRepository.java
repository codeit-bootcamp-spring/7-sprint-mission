package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import com.sprint.mission.discodeit.enum_.ChannelType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {


  Optional<Channel> findByName(String name, ChannelType type);

}
