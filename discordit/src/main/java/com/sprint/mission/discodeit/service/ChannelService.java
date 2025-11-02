package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponse;
import com.sprint.mission.discodeit.dto.user.response.UserResponse;
import com.sprint.mission.discodeit.enums.ChannelType;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    UUID createPublicChannel(PublicChannelCreateRequest dto); // 초기 운영자 한명일때
    UUID createPrivateChannel(PrivateChannelCreateRequest dto); // 초기 운영자 한명일때

    void update(ChannelUpdateRequest dto);

    void delete(UUID uuid);

    List<UserResponse> getAllMembers(UUID uuid);
    List<UserResponse> getAllModerators(UUID uuid);

    ChannelResponse getById(UUID uuid);
    List<ChannelResponse> getAll();
    ChannelResponse getNth(int index);

    List<ChannelResponse> getAllByUserId(String userId);

    // 콘솔 편의용 API (식별/조회 간소화)
    List<UUID> getAllIds();
    List<UUID> getAllIdsByUserId(String userId);
    String getDisplayName(UUID uuid);
    ChannelType getType(UUID uuid);

    // 멤버 수정 (userId 기반)
    void addMember(UUID uuid, String userId);
    void addModerator(UUID uuid, String userId);
    void deleteMember(UUID uuid, String userId);
    void deleteModerator(UUID uuid, String userId);
}
