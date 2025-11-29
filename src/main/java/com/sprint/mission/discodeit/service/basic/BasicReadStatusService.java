package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatus.response.ReadStatusInfoRes;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;

  // ===== 🏗️ Domain Logic (Facade 용)  =====
  @Override
  public ReadStatus create(ReadStatus readStatus) {
    //유저id 랑 channel id 가 이미 있는 readStatus 가 있으면 에러
    if (readStatusRepository.existsByUser_idAndChannel_Id(
        readStatus.getUser().getId(), readStatus.getChannel().getId()
    )) {
      throw new CustomException(ErrorCode.READSTATUS_ALREADY_EXISTS);
    }

    return readStatusRepository.save(readStatus);
  }

  @Override
  @Transactional
  public ReadStatus update(UUID id) {
    ReadStatus readStatus = readStatusRepository.findById(id).orElseThrow(
        () -> new CustomException(ErrorCode.READSTATUS_NOT_FOUND)
    );
    readStatus.updateReadAt();
    return readStatus;
  }

  @Override
  public void delete(UUID id) {
    if (readStatusRepository.existsById(id)) {
      throw new CustomException(ErrorCode.READSTATUS_NOT_FOUND);
    }
    readStatusRepository.deleteById(id);
  }

  @Override
  public List<ReadStatus> findAllByChannelId(UUID channelId) {
    return readStatusRepository.findAllByChannel_id(channelId);
  }

  // ===== 🎯 Controller Direct (DTO 반환) =====
  @Override
  public ReadStatusInfoRes findById(UUID id) {
    ReadStatus readStatus = readStatusRepository.findById(id).orElseThrow(() ->
        new CustomException(ErrorCode.READSTATUS_NOT_FOUND));
    return ReadStatusInfoRes.from(readStatus);
  }
}
