package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channelDto.ChannelDto;
import com.sprint.mission.discodeit.dto.channelDto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDto.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.entityType.ChannelType;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChannelServiceImpl implements ChannelService {

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final ChannelMapper channelMapper;

    @Override
    @Transactional
    public ChannelDto createPublicChannel(PublicChannelCreateRequest requestDto) {

        log.debug("공개 채널 생성 요청 - name: {}, description: {}",
                requestDto.name(), requestDto.description());

        Channel newChannel = new Channel(
                requestDto.name(),
                requestDto.description(),
                ChannelType.PUBLIC
                );

        channelRepository.save(newChannel);
        log.info("공개 채널 생성 완료");
        return channelMapper.toDto(newChannel);
    }

    @Override
    @Transactional
    public ChannelDto createPrivateChannel(PrivateChannelCreateRequest requestDto) {

        log.info("프라이빗 채널 생성 요청");
        Channel newChannel = new Channel(ChannelType.PRIVATE);
        channelRepository.save(newChannel);

        if (requestDto.participantIds() != null && !requestDto.participantIds().isEmpty()) {

            List<User> users = userRepository.findAllById(requestDto.participantIds());
            List<ReadStatus> readStatuses = users.stream()
                    .map(user -> new ReadStatus(user, newChannel))
                    .collect(Collectors.toList());
            readStatusRepository.saveAll(readStatuses);
        }
        log.info("프라이빗 채널 생성 완료");
        return channelMapper.toDto(newChannel);
    }

    @Override
    @Transactional(readOnly = true)
    public ChannelDto findChannelById(UUID id) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new ChannelNotFoundException(id));

        return channelMapper.toDto(channel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChannelDto> findAllByUserId(UUID userId) {

        if(!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        List<Channel> allChannels = channelRepository.findAll();    // 모든 채널

        // 유저 수신 정보로부터 채널Id 가져오기
        Set<UUID> myChannelIds = readStatusRepository.findAllByUserId(userId)
                .stream().map(readStatus -> readStatus.getChannel().getId())
                .collect(Collectors.toSet());

        return allChannels.stream().filter(channel -> {
                    if (channel.getType() == ChannelType.PUBLIC) {
                        return true;
                    } else {
                        return myChannelIds.contains(channel.getId());
                    }
                })
                .map(channelMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ChannelDto updateChannel(UUID channelId, PublicChannelUpdateRequest updateDto) {

        log.debug("채널 수정 요청 - channelId: {}", channelId);
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> {
                    log.warn("채널 수정 실패 - 채널을 찾을 수 없음: {}",  channelId);
                    return new ChannelNotFoundException(channelId);
                });

        if (channel.getType() == ChannelType.PRIVATE) {
            log.warn("채널 수정 실패 - 프라이빗 채널 수정 불가: {}", channelId);
            throw new PrivateChannelUpdateException(channelId);
        }

        channel.updateChannelInfo(updateDto.newName(), updateDto.newDescription());

        channelRepository.save(channel);
        log.info("채널 수정 성공: {}", channelId);
        return channelMapper.toDto(channel);

    }

    @Override
    @Transactional
    public void deleteChannel(UUID id) {
        log.info("채널 삭제 요청: {}", id);
        channelRepository.findById(id)
                        .orElseThrow(() -> {
                            log.warn("채널 삭제 실패 - 채널을 찾을 수 없음: {}", id);
                            return new ChannelNotFoundException(id);
                        });
        log.info("채널 삭제 성공: {}", id);
        channelRepository.deleteById(id);

    }
}
