package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ChannelMember;
import com.sprint.mission.discodeit.entity.ChannelMemberRole;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelMemberRepository extends JpaRepository<ChannelMember, UUID> {

  boolean existsByChannel_IdAndUser_Id(UUID channelId, UUID userId);

  List<ChannelMember> findAllByChannel_id(UUID channelId);

  List<ChannelMember> findByChannel_IdAndRole(UUID channelId, ChannelMemberRole role);
}
