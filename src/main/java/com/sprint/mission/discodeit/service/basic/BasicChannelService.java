package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    // 채널 생성
    @Override
    public Channel create(ChannelType type, String name, String description) {
        // 입력값 검증 (null, 공백 여부 확인)
        if (type == null) {
            throw new IllegalArgumentException("ChannelType은 null일 수 없습니다.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("채널 이름은 비어 있을 수 없습니다.");
        }

        // 예외 처리 및 로깅
        try {
            Channel channel = new Channel(type, name, description);
            // 정상적으로 채널 저장
            return channelRepository.save(channel);
        } catch (Exception e) {
            // 예외 발생 시 로그 출력 후 재전달
            log.error("채널 생성 중 오류 발생: type={}, name={}", type, name, e);
            throw e;
        }
    }

    // 채널 단건 조회
    @Override
    public Channel find(UUID channelId) {
        // 입력값 검증
        if (channelId == null) {
            throw new IllegalArgumentException("channelId는 null일 수 없습니다.");
        }

        // 예외 처리
        try {
            return channelRepository.findById(channelId)
                    .orElseThrow(() -> new NoSuchElementException("ID가 " + channelId + "인 채널을 찾을 수 없습니다."));
        } catch (Exception e) {
            log.error("채널 조회 중 오류 발생: channelId={}", channelId, e);
            throw e;
        }
    }

    // 전체 채널 목록 조회
    @Override
    public List<Channel> findAll() {
        // 예외 처리
        try {
            return channelRepository.findAll();
        } catch (Exception e) {
            log.error("채널 목록 조회 중 오류 발생", e);
            throw e;
        }
    }

    // 채널 정보수정
    @Override
    public Channel update(UUID channelId, String newName, String newDescription) {
        // 입력값 검증
        if (channelId == null) {
            throw new IllegalArgumentException("channelId는 null일 수 없습니다.");
        }
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("새 채널 이름은 비어 있을 수 없습니다.");
        }

        // 예외 처리
        try {
            Channel channel = channelRepository.findById(channelId)
                    .orElseThrow(() -> new NoSuchElementException("ID가 " + channelId + "인 채널을 찾을 수 없습니다."));
            channel.update(newName, newDescription);
            return channelRepository.save(channel);
        } catch (Exception e) {
            log.error("채널 수정 중 오류 발생: channelId={}", channelId, e);
            throw e;
        }
    }

    // 채널 삭제
    @Override
    public void delete(UUID channelId) {
        // 입력값 검증
        if (channelId == null) {
            throw new IllegalArgumentException("channelId는 null일 수 없습니다.");
        }

        try {
            if (!channelRepository.existsById(channelId)) {
                throw new NoSuchElementException("ID가 " + channelId + "인 채널을 찾을 수 없습니다.");
            }
            channelRepository.deleteById(channelId);
            log.info("채널이 정상적으로 삭제되었습니다. id={}", channelId);
        } catch (Exception e) {
            log.error("채널 삭제 중 오류 발생: channelId={}", channelId, e);
            throw e;
        }
    }
}
