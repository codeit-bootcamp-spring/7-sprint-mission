package com.sprint.mission.discodeit.dto.converter;

import com.sprint.mission.discodeit.dto.binarycontent.Response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.message.response.MessageResponseDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.Message;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class MessageDtoConverter {

    public MessageResponseDto toResponseDto(Message message, UserResponseDto userResponseDto, List<BinaryContentResponseDto> binaryContentResponseDtos) {
        return new MessageResponseDto(
                message.getId(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                message.getContent(),
                message.getChannelId(),
                userResponseDto,
                binaryContentResponseDtos
        );
    }
}
