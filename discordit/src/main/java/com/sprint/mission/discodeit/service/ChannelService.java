package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.request.*;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponse;
import com.sprint.mission.discodeit.dto.user.response.UserResponse;
import com.sprint.mission.discodeit.enums.ChannelType;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelResponse createPublicChannel(PublicChannelCreateRequest dto); // 초기 운영자 한명일때
    ChannelResponse createPrivateChannel(PrivateChannelCreateRequest dto); // 초기 운영자 한명일때

    ChannelResponse update(ChannelUpdateRequest dto);

    void delete(ChannelDeleteRequest dto);

    List<UserResponse> getAllMembers(UUID uuid);
    List<UserResponse> getAllModerators(UUID uuid);

    ChannelResponse getById(UUID uuid);
    List<ChannelResponse> getAll();

    List<ChannelResponse> getAllVisibleByUserId(GetVisibleChannelRequest dto);

    // 멤버 수정 (userId 기반)
    void addMember(ChannelMemberRequest dto);
    void addModerator(ChannelMemberRequest dto);
    void deleteMember(ChannelMemberRequest dto);
    void deleteModerator(ChannelMemberRequest dto);
}
