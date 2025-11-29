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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

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

        // 이름 중복 저장 불가
        if(existsByName(name)) {
            throw new CustomException(ErrorCode.CHANNEL_NAME_ALREADY_EXISTS);
        }

        Channel channel = new Channel(name, ChannelType.PUBLIC, description);
        channelRepository.save(channel);
        return channelMapper.toResponseDto(channel);
    }

    @Override
    @Transactional
    public ChannelDto create(CreatePrivateChannelRequestDto request) {
        // 비공개 채널 참가 유저 존재 여부 판단
        List<User> users = request.participantIds().stream()
                .map(userId -> userRepository.findById(userId)
                        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)))
                .toList();

        Channel channel = new Channel(null, ChannelType.PRIVATE, null);
        channelRepository.save(channel);

        users.forEach(user ->
                readStatusRepository.save(
                        new ReadStatus(user, channel, Instant.now())
                ));
        return channelMapper.toResponseDto(channel);
    }

    @Override
    @Transactional
    public ChannelDto update(UUID channelId, UpdatePublicChannelRequestDto request) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() ->  new CustomException(ErrorCode.CHANNEL_NOT_FOUND));

        // 비공개 채널의 경우 수정 불가
        if (channel.getChannelType() ==  ChannelType.PRIVATE) {
            throw new CustomException(ErrorCode.PRIVATE_CHANNEL_UPDATE_FORBIDDEN);
        }

        // 이름 중복 저장 불가
        if(existsByName(request.newName())) {
            throw new CustomException(ErrorCode.CHANNEL_NAME_ALREADY_EXISTS);
        }

        channel.update(request.newName(), request.newDescription());
        channelRepository.save(channel);
        return channelMapper.toResponseDto(channel);
    }

    @Override
    public ChannelDto find(UUID channelId){
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));
        return channelMapper.toResponseDto(channel);
    }

    // Public 채널 목록은 전체 조회 + Private 채널은 User가 참여한 채널만 조회
    @Override
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
    }

    private boolean existsByName(String name) {
        return channelRepository.findAll().stream()
                .filter(c -> c.getChannelName() != null)
                .anyMatch(c -> c.getChannelName().equals(name));
    }
}
