package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binarycontent.Response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.message.response.MessageResponseDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MessageMapper {
    private final BinaryContentMapper binaryContentMapper;
    private final UserMapper userMapper;

    public MessageResponseDto toResponseDto(Message message) {
        User author = message.getAuthor();
        List<BinaryContent> attachments = message.getAttachments();

        UserResponseDto userResponseDto = userMapper.toResponseDto(author);
        List<BinaryContentResponseDto> binaryContentResponseDtos = binaryContentMapper.toResponseDto(attachments);

        return new MessageResponseDto(
                message.getId(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                message.getContent(),
                message.getChannel().getId(),
                userResponseDto,
                binaryContentResponseDtos
        );
    }
}
