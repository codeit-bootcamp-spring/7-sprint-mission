package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.readStatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.readStatus.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entityElement.ReadStatusElement;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

import static java.time.Instant.*;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public void resetReadStatus() {

    }

    @Override
    public ReadStatus createReadStatus(ReadStatusCreateRequestDto readStatusCreateRequestDto) {
        UUID channelId = readStatusCreateRequestDto.getChannelId();
        UUID userId = readStatusCreateRequestDto.getUserId();
        if(!(channelRepository.isChannelExit(channelId)
                && userRepository.isUserExit(userId))
        )
        {
            throw new IllegalArgumentException("존재하지 않은 Channel 혹은 User 입니다.");
        }
        if(readStatusRepository.isReadStatusExist(userId,channelId)){
            throw new IllegalArgumentException("이미 존재하는 readStatus 입니다.");
        }
        ReadStatus readStatus = ReadStatus.builder()
                .readLastTime(now())
                .userId(readStatusCreateRequestDto.getUserId())
                .channelId(readStatusCreateRequestDto.getChannelId())
                .readLastTime(readStatusCreateRequestDto.getReadLastTime())
                .build();
        return readStatusRepository.createReadStatus(readStatus);
    }

    @Override
    public void deleteReadStatus(UUID readStatusID) {
        List<ReadStatus>readStatusList = readStatusRepository.readAllReadStatus();
        if(readStatusList.stream().noneMatch(x->x.getId().equals(readStatusID))){
            throw new IllegalArgumentException("존재하지 않는 readStatus 입니다.");
        }
        readStatusRepository.deleteReadStatus(readStatusID);

    }

    @Override
    public <T>void updateReadStatus(ReadStatusUpdateRequestDto<T> readStatusUpdateRequestDto) {

//        Channel channel = channelRepository.getChannelById(updateChannelRequestDto.getId()).
//                orElseThrow(()->new IllegalArgumentException("존재하지 않는 Channel 입니다."));
//
//        Channel.channelElement channelElement = updateChannelRequestDto.getType();
//
//        BiConsumer<Channel ,T> biConsumer = (BiConsumer<Channel, T>) channelElement.setter;
//        biConsumer.accept(channel,updateChannelRequestDto.getUpdatedValue());
//        channel.updateEntity();
//        channelRepository.updateChannel(channel);

        ReadStatus readStatus = readStatusRepository.readReadStatus(readStatusUpdateRequestDto.getReadStatusId()).orElseThrow(()->new IllegalArgumentException("존재하지 않는 readStatus 입니다."));
        Channel channel = channelRepository.getChannelById(readStatus.getChannelId()).orElseThrow(()->new IllegalArgumentException("존재하지 않는 Channel 입니다."));
//        if(!channel.isPublic() && readStatusUpdateRequestDto.getType() == ReadStatusElement.CHANNEL_ID){
//            throw new IllegalArgumentException("Private Channel은 수정할 수 없습니다");
//
//        }
        ReadStatusElement readStatusElement = readStatusUpdateRequestDto.getType();
        BiConsumer<ReadStatus ,T> biConsumer = (BiConsumer<ReadStatus, T>) readStatusElement.setter;
        biConsumer.accept(readStatus, readStatusUpdateRequestDto.getUpdateValue());
        readStatus.updateEntity();
        readStatusRepository.updateReadStatus(readStatus);
    }

    @Override
    public ReadStatus readReadStatus(UUID readStatusID) {
        return readStatusRepository.readReadStatus(readStatusID).orElseThrow(()->new IllegalArgumentException("존재하지 않는 readStatus 입니다.")) ;

    }

    @Override
    public List<ReadStatus> findAllyByUserId(UUID userID) {
        return readStatusRepository.readAllReadStatus().stream().filter(x->x.getUserId().equals(userID)).toList();
    }


}
