package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.CreateReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.UpdateReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public ReadStatus createReadStatus(CreateReadStatusDto createReadStatusDto) {
        if (!userRepository.existsById(createReadStatusDto.getUserId())) {
            throw new NoSuchElementException("존재하지 않는 유저입니다. " + createReadStatusDto.getUserId());
        }

        if (!channelRepository.existsById(createReadStatusDto.getChannelId())) {
            throw new NoSuchElementException("존재하지 않는 채널입니다. " + createReadStatusDto.getChannelId());
        }

        if (readStatusRepository.findAllByUserId(createReadStatusDto.getUserId())
                .stream().anyMatch(readStatus -> readStatus.getChannelId().equals(createReadStatusDto.getChannelId()))) {
            throw new NoSuchElementException("존재하는 ReadStatus입니다. + " + createReadStatusDto.getUserId() + " " + createReadStatusDto.getChannelId());
        }

        ReadStatus readStatus = new ReadStatus(createReadStatusDto.getUserId(), createReadStatusDto.getChannelId(), createReadStatusDto.getLastReadAt());
        readStatusRepository.save(readStatus);

        return readStatus;
    }

    @Override
    public ReadStatus getReadStatus(UUID readStatusId) {
        return readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new NoSuchElementException("찾을 수 없는 ReadStatus입니다." + readStatusId));
    }

    @Override
    public List<ReadStatus> getAllReadStatusByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId);
    }

    @Override
    public void updateReadStatus(UpdateReadStatusDto updateReadStatusDto) {
        ReadStatus readStatus = readStatusRepository.findById(updateReadStatusDto.getReadStatusId())
                .orElseThrow(() -> new NoSuchElementException("찾을 수 없는 ReadStatus입니다." + updateReadStatusDto.getReadStatusId()));
        readStatus.update(updateReadStatusDto.getLastReadAt());
    }

    @Override
    public void deleteReadStatus(UUID readStatusId) {
        if(!userRepository.existsById(readStatusId)) {
            throw new NoSuchElementException("삭제할 readStatus를 찾을 수 없습니다: " + readStatusId);
        }
        readStatusRepository.deleteById(readStatusId);
    }
}
