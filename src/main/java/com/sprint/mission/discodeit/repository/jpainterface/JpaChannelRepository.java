package com.sprint.mission.discodeit.repository.jpainterface;

import com.sprint.mission.discodeit.entity.ChannelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaChannelRepository extends JpaRepository<ChannelEntity, String> {

}
