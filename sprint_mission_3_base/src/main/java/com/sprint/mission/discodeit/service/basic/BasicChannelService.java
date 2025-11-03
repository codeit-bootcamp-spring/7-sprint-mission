package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelVisibility;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ChannelService 구현체
 * - PUBLIC 채널 생성/수정/삭제
 * - PRIVATE 채널 생성(참여자 ReadStatus 생성), 수정 금지
 * - 사용자 기준 채널 목록 조회(findAllByUserId)
 * - 채널 단건 조회(find)
 * - 삭제 시 Message/ReadStatus 연쇄 정리
 */
@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;

    private Instant latestMessageAt(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId).stream()
                .map(Message::getCreatedAt)
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder())
                .orElse(null);
    }

    /** 채널 참여자(userId) 목록 – PRIVATE 채널 판단/표시에 활용 */
    private List<UUID> participantUserIds(UUID channelId) {
        return readStatusRepository.findAllByChannelId(channelId).stream()
                .map(ReadStatus::getUserId)
                .distinct()
                .collect(Collectors.toList());
    }

    /** 엔티티 → DTO 매핑 */
    private ChannelDto toDto(Channel c) {
        Instant latest = latestMessageAt(c.getId());
        List<UUID> participants = (c.getVisibility() == Channel.Visibility.PRIVATE)
                ? participantUserIds(c.getId())
                : List.of();

        // ⚠️ 만약 여러분의 ChannelDto 생성자에서 visibility를 받지 않는다면
        // 아래 한 줄을 주석 처리하고, 바로 아래 주석 블럭을 사용하세요.
        return new ChannelDto(
                c.getId(),
                c.getName(),
                c.getDescription(),
                (c.getVisibility() == Channel.Visibility.PRIVATE) ? ChannelVisibility.PRIVATE : ChannelVisibility.PUBLIC,
                latest,
                participants
        );

        /*
        // 👉 ChannelDto가 (id, name, description, latest, participants) (5개 인자) 라면 이 버전으로 바꾸세요.
        return new ChannelDto(
                c.getId(),
                c.getName(),
                c.getDescription(),
                latest,
                participants
        );
        */
    }

    /* ---------- service methods ---------- */

    @Override
    public ChannelDto createPublic(CreatePublicChannelRequest request) {
        Channel c = Channel.createPublic(request.name(), request.description());
        channelRepository.save(c);
        return toDto(c);
    }

    @Override
    public ChannelDto createPrivate(CreatePrivateChannelRequest request) {
        // 참여자 검증
        for (UUID uid : request.participantUserIds()) {
            if (!userRepository.existsById(uid)) {
                throw new NoSuchElementException("User not found: " + uid);
            }
        }
        Channel c = Channel.createPrivate(request.participantUserIds());
        channelRepository.save(c);


        for (UUID uid : request.participantUserIds()) {
            readStatusRepository.save(new ReadStatus(uid, c.getId(), Instant.EPOCH));
        }
        return toDto(c);
    }



    @Override
    public ChannelDto find(UUID channelId) {
        Channel c = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel not found: " + channelId));
        return toDto(c);
    }

    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("User not found: " + userId);
        }

        // 채널 전체를 기준으로 접근 가능 여부 필터링
        return channelRepository.findAll().stream()
                .filter(c -> {
                    if (c.getVisibility() == Channel.Visibility.PUBLIC) return true;
                    // PRIVATE: 참여자에 포함된 경우만 허용
                    return readStatusRepository.existsByUserIdAndChannelId(userId, c.getId());
                })
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ChannelDto update(ChannelUpdateRequest request) {
        Channel ch = channelRepository.findById(request.id())
                .orElseThrow(() -> new NoSuchElementException("Channel not found: " + request.id()));

        if (ch.getVisibility() == Channel.Visibility.PRIVATE) {
            throw new IllegalStateException("PRIVATE 채널은 수정할 수 없습니다.");
        }
        ch.update(request.name(), request.description());
        channelRepository.save(ch);
        return toDto(ch);
    }


    @Override
    public void delete(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("Channel not found: " + channelId);
        }

        // 1) 메시지/읽음 먼저 삭제
        messageRepository.deleteAllByChannelId(channelId);
        readStatusRepository.deleteAllByChannelId(channelId);

        // 2) 채널 삭제
        channelRepository.deleteById(channelId);
    }
}
