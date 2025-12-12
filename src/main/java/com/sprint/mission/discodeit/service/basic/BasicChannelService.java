package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.response.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.request.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.dto.request.CreatePublicChannelDto;
import com.sprint.mission.discodeit.dto.update.UpdateChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.enum_.ChannelType;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ChannelMapper channelMapper;


  @Override
  @Transactional
  public ChannelResponseDto createPrivateChannel(CreatePrivateChannelDto request) {
    log.debug("비공개 채널 생성 시작 - 참여자 수: {}", request.participantIds().size());
    if (request.participantIds() == null || request.participantIds().isEmpty()) {
      log.warn("채널 생성 실패 - 참여자 없음");
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
    log.info("비공개 채널 생성 완료 - 비공개 채널 id: {}, 참여자 수: {}",
        savedChannel.getId(), request.participantIds().size());
    return channelMapper.toDto(savedChannel);
  }

  @Override
  @Transactional
  public ChannelResponseDto createPublicChannel(CreatePublicChannelDto request) {
    log.debug("공개 채널 생성 시작 - 채널명: {}", request.name());
    if (request.name() == null || request.name().isBlank()) {
      log.warn("채널 생성 실패 - 채널명 없음");
      throw new IllegalArgumentException("채널 이름은 필수입니다.");
    }

    if (channelRepository.findByNameAndType(request.name(), ChannelType.PUBLIC).isPresent()) {
      log.warn("채널 생성 실패 - 중복된 채널명: {}", request.name());
      throw new IllegalArgumentException("채널이 이미 존재합니다.");
    }

    Channel channel = new Channel(
        ChannelType.PUBLIC,
        request.name(),
        request.description()
    );

    Channel saved = channelRepository.save(channel);
    log.info("공개 채널 생성 완료 - 공개 채널 id: {}, 채널명: {}",
        saved.getId(), saved.getName());
    return channelMapper.toDto(saved);
  }

  @Override
  public List<ChannelResponseDto> findAllByUserId(UUID userId) {
    List<UUID> channelIds = readStatusRepository.findAllByUserId(userId).stream()
        .map(readStatus -> readStatus.getChannel().getId())
        .toList();

    return channelRepository.findAll().stream()
        .filter(channel -> channel.getType() == ChannelType.PUBLIC ||
            channelIds.contains(channel.getId()))
        .map(channel -> channelMapper.toDto(channel))
        .toList();
  }

  @Override
  @Transactional
  public ChannelResponseDto updateChannel(UUID id, UpdateChannelDto updateChannelDto) {
    log.debug("채널 수정 시작 - 채널 id: {}", id);

    Channel channel = channelRepository.findById(id)
        .orElseThrow(() -> {
          log.warn("채널 수정 실패 - 존재하지 않은 채널 id: {}", id);
          return new IllegalArgumentException("채널을 찾을 수 없습니다.");
        });

    if (channel.getType() == ChannelType.PRIVATE) {
      log.warn("채널 수정 실패 - private 채널은 수정 불가, 채널 id: {}", id);
      throw new IllegalArgumentException("private 채널은 수정할 수 없습니다.");
    }

    channel.updateInfo(
        channel.getType(),
        updateChannelDto.newName(),
        updateChannelDto.newDescription()
    );

    log.info("채널 수정 완료 - 채널 id: {}, 채널명: {}", id, channel.getName());
    return channelMapper.toDto(channel);
  }

  @Override
  @Transactional
  public void deleteChannel(UUID channelId) {
    log.debug("채널 삭제 시작 - 채널 id: {}", channelId);
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> {
          log.warn("채널 삭제 실패 - 존재하지 않은 채널 id: {}", channelId);
          return new IllegalArgumentException("채널을 찾을 수 없습니다.");
        });
    log.info("채널 삭제 완료 - 채널 id: {}, 채널명: {}, 타입: {}",
        channelId, channel.getName(), channel.getType());
    channelRepository.delete(channel);
  }
}