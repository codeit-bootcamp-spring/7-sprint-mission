package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.response.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.request.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.dto.request.CreatePublicChannelDto;
import com.sprint.mission.discodeit.dto.update.UpdateChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.enum_.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;


  @Override
  public Channel createPrivateChannel(CreatePrivateChannelDto request) {
    if (request.participantIds() == null || request.participantIds().isEmpty()) {
      throw new IllegalArgumentException("최소 1명은 있어야 합니다.");
    }

    Channel channel = new Channel(
        ChannelType.PRIVATE,
        null,
        null
    );

    Channel saved = channelRepository.save(channel);

    for (UUID participantId : request.participantIds()) {
      ReadStatus readStatus = new ReadStatus(
          participantId,
          saved.getId()
      );
      readStatusRepository.save(readStatus);
    }
    return saved;
  }

  @Override
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
        .map(ReadStatus::getChannelId)
        .toList();

    return channelRepository.findAll().stream()
        .filter(channel -> channel.getType() == ChannelType.PUBLIC ||
            channelIds.contains(channel.getId()))
        .map(channel -> {
          List<UUID> participantIds = readStatusRepository.findAllByChannelId(channel.getId())
              .stream()
              .map(ReadStatus::getUserId)
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

    return channelRepository.save(channel);
  }

  @Override
  public void deleteChannel(UUID channelId) {
    channelRepository.findById(channelId)
        .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다."));

    messageRepository.deleteByChannelId(channelId);
    readStatusRepository.deleteChannelById(channelId);
    channelRepository.delete(channelId);
  }
}