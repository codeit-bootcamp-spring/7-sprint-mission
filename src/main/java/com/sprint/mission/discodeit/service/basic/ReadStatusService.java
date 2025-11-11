package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.Util;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReadStatusService implements InterfaceReadStatusService {
    private final InterfaceReadStatusRepository readStatusRepository;
    private final InterfaceUserRepository userRepository;
    private final InterfaceChannelRepository channelRepository;

    public Res_ReadStatus create(Dto_ReadStatus dtoReadStatus) {
        //create
        //[ ] DTO를 활용해 파라미터를 그룹화합니다.
        //[ ] 관련된 Channel이나 User가 존재하지 않으면 예외를 발생시킵니다.
        //[ ] 같은 Channel과 User와 관련된 객체가 이미 존재하면 예외를 발생시킵니다.
        User user = userRepository.findById(dtoReadStatus.userId()).stream().findFirst().orElseThrow(() -> new IllegalArgumentException("🚨userID = [" + dtoReadStatus.userId().toString() + "] 오류"));
        Channel channel = channelRepository.findById(dtoReadStatus.channelId()).stream().findFirst().orElseThrow(() -> new IllegalArgumentException("🚨channelId = [" + dtoReadStatus.channelId().toString() + "] 오류"));

        ReadStatus readStatus = new ReadStatus(user.getId(), channel.getId());
        readStatusRepository.save(readStatus);
        Util.okMessage("ReadStatusService.create = [" + readStatus + "]");

        return Res_ReadStatus.from(readStatus);
    }

    public Res_ReadStatus find(UUID statusID) {
        //find
        //[ ] id로 조회합니다.
        ReadStatus readStatus = readStatusRepository.findById(statusID).stream().findFirst().orElseThrow(() -> new IllegalArgumentException("🚨statusID = [" + statusID.toString() + "] 오류"));
        Res_ReadStatus dto = Res_ReadStatus.from(readStatus);
        Util.okMessage("ReadStatusService.find = [" + dto + "]");
        return dto;
    }

    public List<Res_ReadStatus> findAllByUserId(UUID userID) {
        //findAllByUserId
        //[ ] userId를 조건으로 조회합니다.
        List<ReadStatus> readStatuses = readStatusRepository.findAll().orElseThrow(() -> new IllegalArgumentException("🚨statusID = [" + userID.toString() + "] 오류"));
        List<ReadStatus> list = readStatuses.stream().filter(readStatus -> readStatus.getUserId().equals(userID)).toList();

        List<Res_ReadStatus> dtoList = new ArrayList<Res_ReadStatus>();
        for (ReadStatus readStatus : list) {
            dtoList.add(Res_ReadStatus.from(readStatus));
            Util.okMessage("ReadStatusService.findAllByUserId = [" + readStatus + "]");
        }
        return dtoList;
    }

    public void update(Dto_ReadStatusUpdate requestDto) {
        //update
        //[ ] DTO를 활용해 파라미터를 그룹화합니다.
        //수정 대상 객체의 readStatusID 파라미터, 수정할 값 파라미터
        ReadStatus readStatus = readStatusRepository.findById(requestDto.readStatusID())
                .orElseThrow(() -> new IllegalArgumentException("🚨readStatusRepository.update.requestDto = [" + requestDto.toString() + "] 오류"));
        readStatus.updateLastReadAt();
        readStatusRepository.save(readStatus);
        Util.okMessage("readStatusRepository.update = [" + readStatus + "]");
    }

    public void delete(UUID statusID) {
        //delete
        //[ ] id로 삭제합니다.
        readStatusRepository.deleteById(statusID);
        Util.okMessage("readStatusRepository.deleteById = [" + statusID + "] 삭제 완료");
    }
}
