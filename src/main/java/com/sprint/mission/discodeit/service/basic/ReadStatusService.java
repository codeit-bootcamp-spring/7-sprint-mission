package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.dto.ReadStatusCteateRequest;
import com.sprint.mission.discodeit.dto.Dto_ReadStatusUpdate;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.mapper.dto.ReadStatusDto;
import com.sprint.mission.discodeit.repository.jpa.ChannelsRepository;
import com.sprint.mission.discodeit.repository.jpa.ReadStatusesRepository;
import com.sprint.mission.discodeit.repository.jpa.UsersRepository;
import com.sprint.mission.discodeit.service.InterfaceReadStatusService;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional // 영속성 컨텍스트
@RequiredArgsConstructor
public class ReadStatusService implements InterfaceReadStatusService {
    private final UsersRepository userRepository;
    private final ChannelsRepository channelRepository;
    private final ReadStatusesRepository readStatusRepository;
    private final ReadStatusMapper readStatusMapper;

    public ReadStatusDto create(ReadStatusCteateRequest dtoReadStatus) {
        User user = userRepository.findById(dtoReadStatus.userId()).stream()
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("🚨User[" + dtoReadStatus.userId().toString() + "] 를 찾을 수 없음 "));

        Channel channel = channelRepository.findById(dtoReadStatus.channelId()).stream()
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("🚨Channel[" + dtoReadStatus.channelId().toString() + "] 를 찾을 수 없음 "));

        // 이미 있으면 오류가 맞는걸까? => 그냥 이게 의도한 오류 한줄 던지니 깔끔! 요걸로 채택!
        // 있는걸 되돌려줘도 되는걸까?  => 해봤더니 줄줄이 사탕 에러!
        Optional<ReadStatus> byUserAndChannelId = readStatusRepository.findReadStatusByUserIdAndChannelId(user.getId(), channel.getId())
            .stream()
            .findFirst();

        if (byUserAndChannelId.isPresent()) {
            throw new IllegalArgumentException("⚠️이미 있는 User + Channel 임!");
        }

        ReadStatus newReadStatus = new ReadStatus(user
                                                , channel
                                                , Instant.now());

        readStatusRepository.save(newReadStatus);
        log.info("✅ ReadStatusService.create = [" + newReadStatus + "]");

        return readStatusMapper.toDto(newReadStatus);
    }

    public ReadStatusDto find(UUID statusID) {
        //find
        //[ ] id로 조회합니다.
        ReadStatus readStatus = readStatusRepository.findById(statusID).stream()
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("🚨statusID = [" + statusID.toString() + "] 오류"));

        ReadStatusDto dto = readStatusMapper.toDto(readStatus);
        log.info("✅ ReadStatusService.find = [" + dto + "]");

        return dto;
    }

    public List<ReadStatusDto> findAllByUserId(UUID userID) {
        //findAllByUserId
        //[ ] userId를 조건으로 조회합니다.
        List<ReadStatus> readStatuses = readStatusRepository.findAll();
        readStatuses.forEach(resReadStatus -> log.info("❌ ReadStatusService.findAllByUserId = [" + resReadStatus.toString() + "]"));

        List<ReadStatusDto> dtoList = readStatuses.stream()
            .filter(readStatus -> readStatus.getUser().getId().equals(userID))
            .map(readStatusMapper::toDto)
            .peek(resReadStatus -> log.info("✅ ReadStatusService.findAllByUserId = [" + resReadStatus.toString() + "]"))
            .toList();

        return dtoList;
    }

    public ReadStatusDto update(UUID readStatusId, Dto_ReadStatusUpdate requestDto) {
        //update
        //[ ] DTO를 활용해 파라미터를 그룹화합니다.
        //수정 대상 객체의 readStatusID 파라미터, 수정할 값 파라미터
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
            .orElseThrow(() -> new NoSuchElementException("🚨Message[" + readStatusId.toString() + "] 읽음 상태를 찾을 수 없음"));

        readStatus.setLastReadAt(requestDto.newLastReadAt());
        readStatusRepository.save(readStatus);

        log.info("✅ readStatusRepository.update = [" + readStatus + "]");

        return readStatusMapper.toDto(readStatus);
    }

    public void delete(UUID statusID) {
        //delete
        //[ ] id로 삭제합니다.
        readStatusRepository.deleteById(statusID);
        log.info("✅ readStatusRepository.deleteById = [" + statusID.toString() + "] 삭제 완료");
    }
}
