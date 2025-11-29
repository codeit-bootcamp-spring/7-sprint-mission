package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channelmember.response.ReadStatusInfoRes;
import com.sprint.mission.discodeit.entity.ChannelMember;

import java.util.List;
import java.util.UUID;

public interface ChannerlMemberService {

  ChannelMember create(ChannelMember channelMember);

  ChannelMember update(UUID id);

  void delete(UUID id);

  List<ChannelMember> findAllByChannelId(UUID channelId);

  ReadStatusInfoRes findById(UUID id);
}
