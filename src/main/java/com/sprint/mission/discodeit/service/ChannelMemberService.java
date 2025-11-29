package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channelmember.response.ChannelMemberInfoRes;
import com.sprint.mission.discodeit.entity.ChannelMember;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;

public interface ChannelMemberService {

  ChannelMember create(ChannelMember channelMember);

  ChannelMember update(UUID id);

  void delete(UUID id);

  ChannelMember findManagerByChannelId(UUID channelId);

  List<ChannelMember> findMembersByChannelId(UUID channelId);

  List<ChannelMember> findAllByChannelId(UUID channelId);

  ChannelMemberInfoRes findById(UUID id);
}
