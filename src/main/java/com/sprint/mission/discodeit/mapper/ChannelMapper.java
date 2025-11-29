package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channel.response.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.enums.ChannelType;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

// Repository나 다른 Mapper, 추가 연산이 필요한 DTO 변환이라면 interface로는 불가능하며 abstract class를 사용해야 한다.
// Mapstruct에서 Mapper는 uses로 DI가 가능
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public abstract class ChannelMapper {

  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private ReadStatusRepository readStatusRepository;

  @Autowired
  private UserMapper userMapper;

  @Mapping(target = "participants", source = ".", qualifiedByName = "getParticipants")
  @Mapping(target = "lastMessageAt", source = ".", qualifiedByName = "getLastMessageAt")
  public abstract ChannelResponseDto toResponseDto(Channel channel);

  @Named("getParticipants")
  protected List<UserResponseDto> getParticipants(Channel channel) {
    if (channel.getType() != ChannelType.PRIVATE) {
      return List.of();
    }

    return readStatusRepository.findAllByChannel(channel).stream()
        .map(ReadStatus::getUser)
        .map(userMapper::toResponseDto)
        .toList();
  }

  @Named("getLastMessageAt")
  protected Instant getLastMessageAt(Channel channel) {
    return messageRepository.findAllByChannelId(channel.getId()).stream()
        .max(Comparator.comparing(Message::getCreatedAt))
        .map(Message::getCreatedAt)
        .orElse(Instant.MIN);
  }
}


