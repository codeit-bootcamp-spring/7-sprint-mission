package com.sprint.mission.discodeit.facade.mapper;

import com.sprint.mission.discodeit.dto.channel.response.ChannelInfoRes;
import com.sprint.mission.discodeit.dto.channel.response.ChannelPrivateInfoRes;
import com.sprint.mission.discodeit.dto.channel.response.ChannelPublicInfoRes;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

// 채널의 공개 여부에 따라 ResDTO를 맞게 변환해주는 클래스
@Component
@RequiredArgsConstructor
public class ChannelMapper {
    private final MessageService messageService;

    public ChannelInfoRes toInfoRes(@NonNull Channel channel) {
        Message lastMessage = messageService.findLastMessageByChannelId(channel.getId());
        Instant lastMessageTime = lastMessage == null ? null : lastMessage.getCreatedAt();

        return switch (channel.getPublicType()) {
            case PUBLIC -> ChannelPublicInfoRes.from(channel, lastMessageTime);
            case PRIVATE -> ChannelPrivateInfoRes.from(channel, lastMessageTime);
            default -> throw new CustomException(ErrorCode.INVALID_CHANNEL_TYPE);
        };
    }
}
