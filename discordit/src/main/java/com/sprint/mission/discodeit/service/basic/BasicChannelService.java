package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.enums.ChannelScope;
import com.sprint.mission.discodeit.common.exceptions.channel.ChannelModificationNotAllowedException;
import com.sprint.mission.discodeit.common.exceptions.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.common.exceptions.user.UserNotFoundException;
import com.sprint.mission.discodeit.dto.entity.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.entity.channel.request.ChannelMemberRequest;
import com.sprint.mission.discodeit.dto.entity.channel.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.entity.channel.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.entity.channel.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.entity.user.UserDto;
import com.sprint.mission.discodeit.dto.mapper.ChannelMapper;
import com.sprint.mission.discodeit.dto.mapper.UserMapper;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
@Primary
@Slf4j
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final UserMapper userMapper;
    private final ChannelMapper channelMapper;

    @Override
    public ChannelDto createPublicChannel(PublicChannelCreateRequest dto) {
        log.info("공개 채널 생성 요청 들어옴. - {}", dto.name());
        Channel channel = Channel.createPublicChannel(
                dto.name(),
                dto.description()
        );
        Channel saved = channelRepository.save(channel);
        log.info("공개 채널 생성 완료. id - {}", saved.getId());
        return channelMapper.toDto(saved, getLastMassageAt(saved));
    }

    @Override
    public ChannelDto createPrivateChannel(PrivateChannelCreateRequest dto) {
        log.info("비공개 채널 생성 요청 들어옴.");
        Channel channel = Channel.createPrivateChannel(
                dto.participantIds().stream()
                        .map(u -> userRepository.findById(u)
                                .orElseThrow(() -> new UserNotFoundException(u)))
                        .collect(Collectors.toSet())
        );
        Channel saved = channelRepository.save(channel);
        log.info("비공개 채널 생성 완료. id - {}", saved.getId());
        return channelMapper.toDto(saved, getLastMassageAt(saved));
    }


    @Override
    public ChannelDto update(UUID id, ChannelUpdateRequest dto) {
        log.info("채널 정보 수정 요청 들어옴. id - {}", id);
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new ChannelNotFoundException(id));
        if (channel.getType() == ChannelScope.PRIVATE) {
            throw new ChannelModificationNotAllowedException(id);
        }

        if (dto.newName() != null) {
            log.info("채널 이름 {} -> {}", channel.getName(), dto.newName());
            channel.setName(dto.newName());
        }
        if (dto.newDescription() != null) {
            log.info("채널 설명 \n{} \n\t-> {}", channel.getDescription(), dto.newDescription());
            channel.setDescription(dto.newDescription());
        }

        channelRepository.save(channel);
        log.info("채널 수정 완료");
        return channelMapper.toDto(channel, getLastMassageAt(channel));
    }

    @Override
    public void delete(UUID id) {
        log.warn("채널 삭제 요청 들어옴. id - {}", id);
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new ChannelNotFoundException(id));
        channelRepository.deleteById(id);
        log.warn("채널 삭제 완료");
        messageRepository.deleteAllByChannel(channel);
        log.warn("채널 메세지 삭제 완료");
        readStatusRepository.deleteAllByChannel(channel);
        log.warn("채널 읽음 상태 삭제 완료");
    }

    @Override
    public List<ChannelDto> getAll() {
        log.info("모든 채널 조회 요청 들어옴.");
        return channelRepository.findAll().stream()
                .map(channel -> channelMapper.toDto(channel, getLastMassageAt(channel)))
                .toList();
    }

    @Override
    public List<ChannelDto> getAllVisibleByUser(UUID userId) {
        log.info("유저 조회 가능 채널 조회 요청 들어옴. - {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return Stream.concat(channelRepository.findAllPublic().stream(), channelRepository.findAllPrivateByUser(user).stream())
                .map(c -> {
                    Optional<Message> last = messageRepository.findLastByChannel(c);
                    Instant lastMessageAt = null;
                    if (last.isPresent()) {
                        lastMessageAt = last.get().getCreatedAt();
                    }
                    return channelMapper.toDto(c, lastMessageAt);
                })
                .toList();
    }

    @Override
    public ChannelDto get(UUID id) {
        log.info("채널 조회 요청 들어옴 - {}", id);
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new ChannelNotFoundException(id));
        return channelMapper.toDto(channel
        , getLastMassageAt(channel));
    }

    @Override
    public List<UserDto> getAllParticipants(UUID id) {
        log.info("채널의 모든 참가자 조회 요청 들어옴. - {}", id);
        return channelRepository.findById(id)
                .orElseThrow(() -> new ChannelNotFoundException(id)).getParticipants().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public void addParticipant(ChannelMemberRequest dto) {
        log.info("채널 참가자 {} 명 추가 요청 들어옴. - {}", dto.userUuids().size(), dto.channelId());
        List<User> users = userRepository.findAllById(dto.userUuids());
        Channel channel = channelRepository.findById(dto.channelId())
                .orElseThrow(() -> new ChannelNotFoundException(dto.channelId()));
        log.trace("추가 전 참가자 - {} 명", channel.getParticipants().size());
        channel.addParticipant(new HashSet<>(users));
        log.info("추가 완료. 현재 인원 {} 명", channel.getParticipants().size());
    }

    @Override
    public void removeParticipant(ChannelMemberRequest dto) {
        log.info("채널 참가자 {} 명 제외 요청 들어옴 - {}", dto.userUuids().size(), dto.channelId());
        List<User> users = userRepository.findAllById(dto.userUuids());
        Channel channel = channelRepository.findById(dto.channelId())
                .orElseThrow(() -> new ChannelNotFoundException(dto.channelId()));
        log.trace("삭제 전 참가자 - {} 명", channel.getParticipants().size());
        channel.removeParticipant(new HashSet<>(users));
        log.info("삭제 완료. 현재 인원 {} 명", channel.getParticipants().size());
    }

    private Instant getLastMassageAt(Channel channel) {
        log.info("채널 마지막 메세지 시점 조회 요청 들어옴 - {}", channel.getId());
        Optional<Message> lastByChannel = messageRepository.findLastByChannel(channel);
        return lastByChannel.map(BaseUpdatableEntity::getUpdatedAt).orElse(null);
    }
}
