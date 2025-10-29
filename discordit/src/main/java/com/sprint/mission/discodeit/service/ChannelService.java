package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.request.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channel.request.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.request.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.base.User;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    UUID createPublicChannel(PublicChannelCreateRequestDto dto); // 초기 운영자 한명일때
    UUID createPrivateChannel(PrivateChannelCreateRequestDto dto); // 초기 운영자 한명일때

    void update(ChannelUpdateRequestDto dto);

    void delete(UUID uuid);

    List<UserResponseDto> getAllMembers(UUID uuid);
    List<UserResponseDto> getAllModerators(UUID uuid);

    void addModerator(UUID uuid, User user);
    void addMember(UUID uuid, User user);

    void deleteModerator(UUID uuid, User user);
    void deleteMember(UUID uuid, User user);

    ChannelResponseDto getById(UUID uuid);
    List<ChannelResponseDto> getAll();
    ChannelResponseDto getNth(int index);

    List<ChannelResponseDto> getAllByUserId(String userId);
}
