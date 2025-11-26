package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.response.MessageResponseDto;
import com.sprint.mission.discodeit.dto.response.ReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.update.UpdateReadStatusDto;
import com.sprint.mission.discodeit.dto.request.CreateReadStatusRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final ReadStatusMapper readStatusMapper;


  @Override
  @Transactional
  public ReadStatusResponseDto createReadStatus(CreateReadStatusRequestDto request) {
    Optional<ReadStatus> exist = readStatusRepository.findByUserIdAndChannelId(
        request.userId(),
        request.channelId()
    );

    if (exist.isPresent()) {
      throw new IllegalArgumentException("이미 존재합니다.");
    }

    Channel channel = channelRepository.findById(request.channelId())
        .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다."));

    User user = userRepository.findById(request.userId())
        .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

    ReadStatus readStatus = new ReadStatus(
        user,
        channel
    );

    ReadStatus saved = readStatusRepository.save(readStatus);
    return readStatusMapper.toDto(saved);
  }

  @Override
  public ReadStatusResponseDto find(UUID userId) {
    ReadStatus readStatus = getReadStatus(userId);
    return readStatusMapper.toDto(readStatus);
  }

  @Override
  public List<ReadStatusResponseDto> findAllByUserId(UUID userId) {
    return readStatusRepository.findAllByUserId(userId).stream()
        .map(readStatusMapper::toDto)
        .toList();
  }

  @Override
  @Transactional
  public ReadStatusResponseDto updateReadStatus(UUID readStatusId,
      UpdateReadStatusDto request) {
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new IllegalArgumentException("ReadStatus를 찾을 수 없습니다."));

    readStatus.updateReadTime(request.newLastReadAt());

    return readStatusMapper.toDto(readStatus);
  }

  @Override
  @Transactional
  public void deleteReadStatus(UUID readStatusId) {
    ReadStatus readStatus = getReadStatus(readStatusId);
    readStatusRepository.delete(readStatus);
  }

  // 중복 메서드 만들기
  private ReadStatus getReadStatus(UUID readStatusId) {
    return readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new IllegalArgumentException("ReadStatus를 찾을 수 없습니다."));
  }
}
