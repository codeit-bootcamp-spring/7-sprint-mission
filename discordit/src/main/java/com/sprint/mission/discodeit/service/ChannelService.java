package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.request.*;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponseV2;
import com.sprint.mission.discodeit.dto.channel.response.DetailedChannelResponse;
import com.sprint.mission.discodeit.dto.user.response.UserResponse;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelResponseV2 createPublicChannel(PublicChannelCreateRequest dto); // 초기 운영자 한명일때
    ChannelResponseV2 createPrivateChannel(PrivateChannelCreateRequest dto); // 초기 운영자 한명일때

    ChannelResponseV2 update(UUID channelId, ChannelUpdateRequest dto);

    void delete(UUID channelID);

    List<UserResponse> getAllMembers(UUID uuid);
    List<UserResponse> getAllModerators(UUID uuid);

    ChannelResponseV2 getById(UUID uuid);
    List<ChannelResponseV2> getAll();

    List<DetailedChannelResponse> getAllVisibleByUser(UUID userUuid);

    void addMember(ChannelMemberRequest dto);
    void addModerator(ChannelMemberRequest dto);
    void deleteMember(ChannelMemberRequest dto);
    void deleteModerator(ChannelMemberRequest dto);
}
