package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.enum_.ChannelType;

import io.swagger.v3.oas.models.security.SecurityScheme.In;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelResponseDto(

    UUID id,// 채널 ID
    ChannelType type, //채널 타입
    String name, //채널 이름
    String description, //채널 설명
    List<UserResponseDto> participants,
    Instant lastMessageAt // 메시지 시간 정보

) {

}
