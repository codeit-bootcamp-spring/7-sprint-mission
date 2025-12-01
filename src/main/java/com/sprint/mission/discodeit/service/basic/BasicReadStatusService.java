package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatus.response.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readstatus.requset.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.requset.ReadStatusUpdateReuqest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicReadStatusService implements ReadStatusService {
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;
    private final ChannelRepository channelRepository;
    private final ReadStatusMapper readStatusMapper;


    @Override
    @Transactional
    public ReadStatusDto create(ReadStatusCreateRequest request) {

        //request에 받은 Channel이나 User의 uuid가 존재하냐

        boolean channelMatch = channelRepository.existsById(request.channelId());
        boolean userMatch = userRepository.existsById(request.userId());

        //같은 Channel과 User와 관련된 객체 존재하냐
        boolean existReadStatus = readStatusRepository.existsByUserIdAndChannelId(request.userId(), request.channelId());
        // Channel이나 User가 존재하지 않으면 예외
        if (!channelMatch || !userMatch) {
            throw new RuntimeException("맞는uuid가 존재하지않아요\n"
                    + "채널아이디 : " + request.channelId() + "\n"
                    + "유저아이디 : " + request.userId());
        } else if (existReadStatus) {
            throw new IllegalStateException("이미 존재하는 ReadStatus다");
        }

        User user = userRepository.getReferenceById(request.userId());
        Channel channel = channelRepository.getReferenceById(request.channelId());

        ReadStatus createReadStatus = new ReadStatus(user, channel, request.lastReadAt());

        readStatusRepository.save(createReadStatus);
        return readStatusMapper.toDto(createReadStatus);
    }

    @Transactional(readOnly = true)
    @Override
    public ReadStatusDto find(UUID readStatusId) {

        return readStatusRepository.findById(readStatusId)
                .map(readStatusMapper::toDto)
                .orElseThrow(() -> new NoSuchElementException("readStatusId로 찾을수없어"));

    }

    @Override
    @Transactional(readOnly = true)
    public List<ReadStatusDto> findAllByUserId(UUID userId) {
        return readStatusRepository.findAll()
                .stream()
                .map(readStatusMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ReadStatusDto update(UUID readStatusId, ReadStatusUpdateReuqest request) {
        Instant newLastReadAt = request.newLastReadAt();
        ReadStatus readStatus = readStatusRepository
                .findById(readStatusId).orElseThrow(() -> new NoSuchElementException("맞는 readstatus가없어요"));
        //업데이트자체가 시간만 있는게아닌가 시간초기화
        readStatus.update(newLastReadAt);
        return readStatusMapper.toDto(readStatus);

    }

    @Override
    public void delete(UUID readStatusId) {
        readStatusRepository.deleteById(readStatusId);
    }
}
