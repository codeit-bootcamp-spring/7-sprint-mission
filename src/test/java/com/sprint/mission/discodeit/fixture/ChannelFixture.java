package com.sprint.mission.discodeit.fixture;

import com.sprint.mission.discodeit.dto.channelDto.ChannelDto;
import com.sprint.mission.discodeit.dto.channelDto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDto.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.userDto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.enums.ChannelType;
import com.sprint.mission.discodeit.entity.enums.Role;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChannelFixture {

    // Request ========================================================
    public static PublicChannelCreateRequest getPublicChannelRequest(int index) {
        return new PublicChannelCreateRequest("Public Channel" + index, "test" + index);
    }

    public static PrivateChannelCreateRequest getPrivateChannelRequest(List<UUID> userIds) {
        return new PrivateChannelCreateRequest(userIds);
    }

    // entity ========================================================
    public static Channel getPublicChannel(int index) {
        return toPublicChannel(getPublicChannelRequest(index));
    }

    public static Channel getPrivateChannel() {
        Channel channel =  new Channel(ChannelType.PRIVATE);
        ReflectionTestUtils.setField(channel, "id", UUID.randomUUID());
        return channel;
    }

    private static Channel toPublicChannel(PublicChannelCreateRequest request) {
        Channel channel =  new Channel(
                request.name(), request.description(), ChannelType.PUBLIC
        );
        ReflectionTestUtils.setField(channel, "id", UUID.randomUUID());
        return channel;
    }

    // Response ========================================================
    public static ChannelDto getPublicChannelDto(int index) {
        return toDto(getPublicChannel(index));
    }
    public static ChannelDto getPrivateChannelDto(List<UUID> userIds) {
        return toDto(getPrivateChannel(), userIds);
    }

    private static ChannelDto toDto(Channel channel) {
        return ChannelDto.from(channel, null, null);
    }
    private static ChannelDto toDto(Channel channel, List<UUID> userIds) {
        if (userIds == null) return toDto(channel);

        int userNumber = 1;
        List<UserDto> participants = new ArrayList<>();
        for (UUID userId : userIds) {
            UserDto userDto = UserDto.builder()
                    .id(userId).
                    username("user" + userNumber)
                    .username("user" + userNumber)
                    .email("user" + userNumber++ + "@discodeit.com")
                    .role(Role.USER)
                    .build();
            participants.add(userDto);
        }
        return ChannelDto.from(channel, participants, null);
    }

    // UpdateResponse ========================================================
    public static ChannelDto getUpdatedChannelDto(PublicChannelUpdateRequest request, UUID channelId) {
        return ChannelDto.builder()
                .id(channelId)
                .type(ChannelType.PUBLIC)
                .name(request.newName())
                .description(request.newDescription())
                .build();
    }
}
