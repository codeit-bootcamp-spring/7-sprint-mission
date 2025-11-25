package com.sprint.mission.discodeit.repository.jpainterface;

import com.sprint.mission.discodeit.entity.ChannelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaChannelRepository extends JpaRepository<ChannelEntity, UUID> {

}
