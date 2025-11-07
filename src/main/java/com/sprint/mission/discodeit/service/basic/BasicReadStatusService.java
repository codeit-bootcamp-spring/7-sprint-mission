package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatus.request.CreateReadStatusRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.request.UpdateReadStatusRequestDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.global.exception.custom.CustomException;
import com.sprint.mission.discodeit.global.exception.custom.ErrorCode;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public void create(CreateReadStatusRequestDto request) {
        // 유저가 존재하지 않으면 예외 발생
        userRepository.findById(request.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 채널이 존재하지 않으면 예외 발생
        channelRepository.findById(request.getChannelId())
                .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));

        if(!readStatusRepository.existsByUserIdAndChannelId(request.getUserId(),request.getChannelId())) {
            readStatusRepository.save(new ReadStatus(request.getUserId(),request.getChannelId()));
        } else {
            throw new CustomException(ErrorCode.CHANNEL_MEMBER_ALREADY_EXISTS);
        }
    }

    @Override
    public ReadStatus find(UUID readStatusId) {
        return readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new CustomException(ErrorCode.READSTATUS_NOT_FOUND));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAll().stream()
                .filter(r -> userId.equals(r.getUserId()))
                .collect(Collectors.toList());
    }

    @Override
    public void update(UpdateReadStatusRequestDto request) {
        ReadStatus rs = readStatusRepository.findByUserIdAndChannelId(request.getUserId(),request.getChannelId())
                .orElseThrow(() -> new CustomException(ErrorCode.READSTATUS_NOT_FOUND));
        rs.setUpdatedAt(); // 유저가 채널 메시지를 읽을 경우 읽은 시간 변경
        readStatusRepository.update(rs);
    }

    @Override
    public void delete(UUID readStatusId) {
        readStatusRepository.deleteById(readStatusId);
    }
}
