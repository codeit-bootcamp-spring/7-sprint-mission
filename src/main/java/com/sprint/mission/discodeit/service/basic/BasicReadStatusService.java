package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.request.CreateReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.request.UpdateReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.response.ReadStatusResponseDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.global.exception.CustomException;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  @Override
  public ReadStatusResponseDto createReadStatus(CreateReadStatusDto createReadStatusDto) {
    if (!userRepository.existsById(createReadStatusDto.userId())) {
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    }

    if (!channelRepository.existsById(createReadStatusDto.channelId())) {
      throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
    }

    if (readStatusRepository.findAllByUserId(createReadStatusDto.userId())
        .stream().anyMatch(
            readStatus -> readStatus.getChannelId().equals(createReadStatusDto.channelId()))) {
      throw new CustomException(ErrorCode.READ_STATUS_ALREADY_EXIST);
    }

    ReadStatus readStatus = new ReadStatus(createReadStatusDto.userId(),
        createReadStatusDto.channelId(), createReadStatusDto.lastReadAt());
    readStatusRepository.save(readStatus);

    return ReadStatusResponseDto.from(readStatus);
  }

  @Override
  public ReadStatusResponseDto getReadStatus(UUID readStatusId) {
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new CustomException(ErrorCode.READ_STATUS_NOT_FOUND));

    return ReadStatusResponseDto.from(readStatus);
  }

  @Override
  public List<ReadStatusResponseDto> getAllReadStatusByUserId(UUID userId) {
    return readStatusRepository.findAllByUserId(userId).stream()
        .map(ReadStatusResponseDto::from)
        .toList();
  }

  @Override
  public ReadStatusResponseDto updateReadStatus(UUID readStatusId,
      UpdateReadStatusDto updateReadStatusDto) {
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new CustomException(ErrorCode.READ_STATUS_NOT_FOUND));
    readStatus.update(updateReadStatusDto.newLastReadAt());

    return ReadStatusResponseDto.from(readStatus);
  }

  @Override
  public void deleteReadStatus(UUID readStatusId) {
    if (!userRepository.existsById(readStatusId)) {
      throw new CustomException(ErrorCode.READ_STATUS_NOT_FOUND);
    }
    readStatusRepository.deleteById(readStatusId);
  }
}
