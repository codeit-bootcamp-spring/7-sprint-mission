package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.response.user.UserDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ChannelMapper {

    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final UserMapper userMapper;
    public ChannelDto toDto(Channel channel){
        List<ReadStatus> readStatusList = readStatusRepository.findByChannel(channel);
        List<UserDto> participantsDto = readStatusList.stream().map(x->
                userMapper.toDto(x.getUser())).toList();
        return new ChannelDto(
                channel.getId(),
                channel.getType(),
                channel.getName(),
                channel.getDescription(),
                participantsDto,
                lastPostTime(channel)
        );
    }

    private Instant lastPostTime(Channel channel){
        List<Message> messageList = messageRepository.findAll();
        if(messageList.stream().noneMatch(x->x.getChannel().getId().equals(channel.getId()))) return Instant.MIN;

        return messageList.stream().
                filter(x -> x.getChannel().getId().equals(channel.getId()))
                .map(BaseUpdatableEntity::getUpdatedAt)
                .max(Comparator.naturalOrder()).orElseThrow(()->new IllegalArgumentException("Message not found"));
    }
}
