package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.request.*;
import com.sprint.mission.discodeit.dto.channel.response.ChannelDto;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.global.exception.custom.CustomException;
import com.sprint.mission.discodeit.global.exception.custom.ErrorCode;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    private final ChannelMapper channelMapper;

    // API 스펙에 맞는 공개 채널 및 비공개 채널 생성 메서드 추가
    @Override
    @Transactional
    public ChannelDto create(CreatePublicChannelRequestDto request) {
        String name = request.name();
        String description = request.description();

        log.debug("공개 채널 생성 요청: channelName = {}", name);

        // 채널 이름 중복 검사
        validateChannelNameDuplicate(name);

        Channel channel = new Channel(name, ChannelType.PUBLIC, description);
        Channel saved = channelRepository.save(channel);

        log.info("공개 채널 생성 완료: channelId = {}, channelName = {}", saved.getId(), saved.getChannelName());
        return channelMapper.toResponseDto(channel);
    }

    @Override
    @Transactional
    public ChannelDto create(CreatePrivateChannelRequestDto request) {

        log.debug("비공개 채널 생성 요청");

        // 비공개 채널 참가 유저 존재 여부 판단
        List<User> users = request.participantIds().stream()
                .map(userId -> userRepository.findById(userId)
                        .orElseThrow(() -> {
                            log.warn("userId = {}인 사용자가 없습니다.", userId);
                            return new CustomException(ErrorCode.USER_NOT_FOUND);
                        })
                )
                .toList();

        Channel channel = new Channel(null, ChannelType.PRIVATE, null);
        Channel saved = channelRepository.save(channel);

        users.forEach(user ->
                readStatusRepository.save(
                        new ReadStatus(user, channel, Instant.now())
                ));

        log.info("비공개 채널 생성 완료: channelId = {}", saved.getId());
        return channelMapper.toResponseDto(channel);
    }

    @Override
    @Transactional
    public ChannelDto update(UUID channelId, UpdatePublicChannelRequestDto request) {

        log.debug("채널 수정 요청: channelId = {}", channelId);

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() ->  new CustomException(ErrorCode.CHANNEL_NOT_FOUND));

        // 비공개 채널의 경우 수정 불가
        if (channel.getChannelType() ==  ChannelType.PRIVATE) {
            log.warn("비공개 채널 수정 요청 감지: channelId = {}", channelId);
            throw new CustomException(ErrorCode.PRIVATE_CHANNEL_UPDATE_FORBIDDEN);
        }

        // 이름 중복 저장 불가
        validateChannelNameDuplicate(request.newName());

        channel.update(request.newName(), request.newDescription());
        channelRepository.save(channel);

        log.info("채널 수정 완료: channelId = {}", channelId);
        return channelMapper.toResponseDto(channel);
    }

    @Override
    @Transactional(readOnly = true)
    public ChannelDto find(UUID channelId){
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));
        return channelMapper.toResponseDto(channel);
    }

    // Public 채널 목록은 전체 조회 + Private 채널은 User가 참여한 채널만 조회
    @Override
    @Transactional(readOnly = true)
    public List<ChannelDto> findAllByUserId(UUID userId) {
        // 공개 채널 조회
        List<Channel> publicChannels = channelRepository.findAllByChannelType(ChannelType.PUBLIC);

        // 비공개 채널 조회
        // fetch join 적용
        List<Channel> privateChannels = readStatusRepository.findAllByUserIdWithChannel(userId).stream()
                .map(rc -> rc.getChannel())
                .filter(c -> c.getChannelType() == ChannelType.PRIVATE)
                .collect(Collectors.toList());

        List<Channel> channels = new ArrayList<>();
        channels.addAll(publicChannels);
        channels.addAll(privateChannels);

        return channels.stream()
                .map(c -> channelMapper.toResponseDto(c))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(UUID channelId) {

        log.debug("채널 삭제 요청: channelId = {}", channelId);

        channelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));

        // 채널 메시지 연관 파일 UUID 저장
        List<UUID> binaryContentIds = messageRepository.findAllByChannelId(channelId).stream()
                .flatMap(message -> message.getAttachments().stream())
                .map(attachment -> attachment.getId())
                .toList();

        readStatusRepository.deleteByChannelId(channelId); // 채널의 모든 read status 삭제
        messageRepository.deleteAllByChannelId(channelId); // 채널의 모든 메시지 삭제
        binaryContentRepository.deleteByIdIn(binaryContentIds); // 채널 메시지 연관 파일들 삭제
        channelRepository.deleteById(channelId); // 채널 삭제

        log.info("채널 삭제 완료: channelId = {}", channelId);
    }

    private void validateChannelNameDuplicate(String name) {
        boolean exists = channelRepository.findAll().stream()
                .filter(c -> c.getChannelName() != null)
                .anyMatch(c -> c.getChannelName().equals(name));

        if(exists) {
            log.warn("중복된 채널 이름: {}", name);
            throw new CustomException(ErrorCode.CHANNEL_NAME_ALREADY_EXISTS);
        }
    }
}
