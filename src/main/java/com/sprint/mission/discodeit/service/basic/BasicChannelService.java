package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.response.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.request.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.dto.request.CreatePublicChannelDto;
import com.sprint.mission.discodeit.dto.update.UpdateChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.enum_.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;


  @Override
  @Transactional
  public Channel createPrivateChannel(CreatePrivateChannelDto request) {
    if (request.participantIds() == null || request.participantIds().isEmpty()) {
      throw new IllegalArgumentException("최소 1명은 있어야 합니다.");
    }

    Channel channel = new Channel(
        ChannelType.PRIVATE,
        null,
        null
    );

    Channel savedChannel = channelRepository.save(channel);

    for (UUID participantId : request.participantIds()) {
      User user = userRepository.findById(participantId)
          .orElseThrow(() -> new IllegalArgumentException("유저가 없습니다"));
      ReadStatus readStatus = new ReadStatus(
          user,
          savedChannel
      );
      readStatusRepository.save(readStatus);
    }
    return savedChannel;
  }

  @Override
  @Transactional
  public Channel createPublicChannel(CreatePublicChannelDto request) {
    if (request.name() == null || request.name().isBlank()) {
      throw new IllegalArgumentException("채널 이름은 필수입니다.");
    }

    if (channelRepository.findByName(request.name(), ChannelType.PUBLIC).isPresent()) {
      throw new IllegalArgumentException("채널이 이미 존재합니다.");
    }

    Channel channel = new Channel(
        ChannelType.PUBLIC,
        request.name(),
        request.description()
    );

    return channelRepository.save(channel);
  }

  @Override
  public List<ChannelResponseDto> findAllByUserId(UUID userId) {
    List<UUID> channelIds = readStatusRepository.findAllByUserId(userId).stream()
        .map(readStatus -> readStatus.getChannel().getId())
        .toList();

    return channelRepository.findAll().stream()
        .filter(channel -> channel.getType() == ChannelType.PUBLIC ||
            channelIds.contains(channel.getId()))
        .map(channel -> {
          List<UUID> participantIds = readStatusRepository.findAllByChannelId(channel.getId())
              .stream()
              .map(readStatus -> readStatus.getUser().getId())
              .toList();

          Instant lastMessageAt = messageRepository.findByChannelId(channel.getId())
              .stream()
              .map(message -> message.getCreatedAt())
              .max(Instant::compareTo)
              .orElse(null);
          return ChannelResponseDto.from(channel, participantIds, lastMessageAt);
        })
        .toList();
  }

  @Override
  @Transactional
  public Channel updateChannel(UUID id, UpdateChannelDto updateChannelDto) {
    Channel channel = channelRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다."));

    if (channel.getType() == ChannelType.PRIVATE) {
      throw new IllegalArgumentException("Private 채널은 수정할 수 없습니다.");
    }

    channel.updateInfo(
        channel.getType(),
        updateChannelDto.newName(),
        updateChannelDto.newDescription()
    );

    return channel;
  }

  @Override
  @Transactional
  public void deleteChannel(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다."));
    channelRepository.delete(channel);
  }
}