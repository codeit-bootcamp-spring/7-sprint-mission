package com.sprint.mission.discodeit.application.dto;

import com.sprint.mission.discodeit.application.dto.response.ChannelResponseDto;
import com.sprint.mission.discodeit.domain.Channel;

//final로 상속 막고, private 생성자로 생성 막고, 오로지 정적 메서드만을 이용해서 사용할 수 있는 유틸 클래스
public final class ChannelDtoMapper {

    private ChannelDtoMapper() {}

    public static ChannelResponseDto channelToResponseDto(Channel channel){
        return new ChannelResponseDto(channel.getChannelName(), channel.getServerId(),channel.getId());
    }
}
