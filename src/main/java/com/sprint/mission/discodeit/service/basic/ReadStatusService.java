package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.dto.Dto_ReadStatus;
import com.sprint.mission.discodeit.entity.dto.Dto_ReadStatusUpdate;
import com.sprint.mission.discodeit.entity.dto.Res_ReadStatus;
import com.sprint.mission.discodeit.repository.InterfaceChannelRepository;
import com.sprint.mission.discodeit.repository.InterfaceReadStatusRepository;
import com.sprint.mission.discodeit.repository.InterfaceUserRepository;
import com.sprint.mission.discodeit.service.InterfaceReadStatusService;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReadStatusService implements InterfaceReadStatusService {
    private final InterfaceReadStatusRepository readStatusRepository;
    private final InterfaceUserRepository userRepository;
    private final InterfaceChannelRepository channelRepository;

    public Res_ReadStatus create(Dto_ReadStatus dtoReadStatus) {
        User user = userRepository.findById(dtoReadStatus.userId()).stream().findFirst()
            .orElseThrow(() -> new NoSuchElementException("🚨User[" + dtoReadStatus.userId().toString() + "] 를 찾을 수 없음 "));

        Channel channel = channelRepository.findById(dtoReadStatus.channelId()).stream().findFirst()
            .orElseThrow(() -> new NoSuchElementException("🚨Channel[" + dtoReadStatus.channelId().toString() + "] 를 찾을 수 없음 "));

        ReadStatus readStatus = readStatusRepository.findByUserAndChannelId(user.getId(),
            channel.getId()).orElse(null);
        if (readStatus != null) {
            throw new IllegalArgumentException("🚨userID + channelID = ReadStatus[" + readStatus + "] 이미 존재");
        }

        ReadStatus newReadStatus = new ReadStatus(user.getId(), channel.getId());
        readStatusRepository.save(newReadStatus);
        log.info("✅ 🍎🍎ReadStatusService.create = [" + newReadStatus + "]");

        return Res_ReadStatus.from(newReadStatus);
    }

    public Res_ReadStatus find(UUID statusID) {
        //find
        //[ ] id로 조회합니다.
        ReadStatus readStatus = readStatusRepository.findById(statusID).stream().findFirst().orElseThrow(() -> new IllegalArgumentException("🚨statusID = [" + statusID.toString() + "] 오류"));
        Res_ReadStatus dto = Res_ReadStatus.from(readStatus);
        log.info("✅ ReadStatusService.find = [" + dto + "]");
        return dto;
    }

    public List<Res_ReadStatus> findAllByUserId(UUID userID) {
        //findAllByUserId
        //[ ] userId를 조건으로 조회합니다.
        List<ReadStatus> readStatuses = readStatusRepository.findAll();
        List<ReadStatus> list = readStatuses.stream().filter(readStatus -> readStatus.getUserId().equals(userID)).toList();

        List<Res_ReadStatus> dtoList = new ArrayList<Res_ReadStatus>();
        for (ReadStatus readStatus : list) {
            dtoList.add(Res_ReadStatus.from(readStatus));
            log.info("✅ ReadStatusService.findAllByUserId = [" + readStatus + "]");
        }
        return dtoList;
    }

    public Res_ReadStatus update(UUID readStatusId, Dto_ReadStatusUpdate requestDto) {
        //update
        //[ ] DTO를 활용해 파라미터를 그룹화합니다.
        //수정 대상 객체의 readStatusID 파라미터, 수정할 값 파라미터
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
            .orElseThrow(() -> new NoSuchElementException("🚨Message[" + readStatusId.toString() + "] 읽음 상태를 찾을 수 없음"));

        readStatus.updateLastReadAt(requestDto);
        readStatusRepository.save(readStatus);
        log.info("✅ readStatusRepository.update = [" + readStatus + "]");

        return Res_ReadStatus.from(readStatus);
    }

    public void delete(UUID statusID) {
        //delete
        //[ ] id로 삭제합니다.
        readStatusRepository.deleteById(statusID);
        log.info("✅ readStatusRepository.deleteById = [" + statusID + "] 삭제 완료");
    }
}
