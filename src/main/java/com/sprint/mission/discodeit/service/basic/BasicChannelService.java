package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.request.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.dto.channel.request.CreatePublicChannelDto;
import com.sprint.mission.discodeit.dto.channel.request.UpdateChannelDto;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.enums.ChannelType;
import com.sprint.mission.discodeit.global.exception.CustomException;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;

  private final ChannelMapper channelMapper;

  @Override
  public ChannelResponseDto createChannel(CreatePublicChannelDto createPublicChannelDto) {
    Channel channel = new Channel(
        ChannelType.PUBLIC,
        createPublicChannelDto.name(),
        createPublicChannelDto.description()
    );
    channelRepository.save(channel);

    return channelMapper.toResponseDto(channel);
  }

  @Override
  public ChannelResponseDto createChannel(CreatePrivateChannelDto createPrivateChannelDto) {
    Channel channel = new Channel(ChannelType.PRIVATE, null, null);

    createPrivateChannelDto.participantIds().forEach(userId -> {
      User user = userRepository.findById(userId)
          .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

      ReadStatus readStatus = new ReadStatus(user, channel, channel.getCreatedAt());
      readStatusRepository.save(readStatus);
    });

    channelRepository.save(channel);

    return channelMapper.toResponseDto(channel);
  }

  @Override
  public ChannelResponseDto getChannel(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));

    return channelMapper.toResponseDto(channel);
  }

  @Override
  public List<ChannelResponseDto> getAllChannelByUserId(UUID userId) {
    List<UUID> participants = readStatusRepository.findAllByUserId(userId).stream()
        .map(readStatus -> readStatus.getChannel().getId())
        .toList();

    // 채널 타입이 퍼블릭이면 전체, private이면 userId로 필터
    return channelRepository.findAll().stream()
        .filter(channel -> channel.getType().equals(ChannelType.PUBLIC)
            || participants.contains(channel.getId()))
        .map(channelMapper::toResponseDto)
        .toList();
  }

  @Override
  public ChannelResponseDto updateChannel(UUID channelId, UpdateChannelDto updateChannelDto) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));

    if (channel.getType().equals(ChannelType.PRIVATE)) {
      throw new CustomException(ErrorCode.PRIVATE_CHANNEL_UPDATE_NOT_ALLOWED);
    }

    channel.updateChannel(updateChannelDto.newName(), updateChannelDto.newDescription());

    return channelMapper.toResponseDto(channel);
  }

  @Override
  public void deleteChannel(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));

    // 채널이 삭제되면, 연관 데이터 readStatus, message도 삭제
    channelRepository.deleteById(channelId);
    readStatusRepository.deleteAllByChannelId(channelId);
    messageRepository.deleteAllByChannelId(channelId);

  }
}
