package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.exceptions.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.common.exceptions.user.UserNotFoundException;
import com.sprint.mission.discodeit.dto.entity.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.entity.channel.request.ChannelMemberRequest;
import com.sprint.mission.discodeit.dto.entity.channel.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.entity.channel.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.entity.channel.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.entity.channel.response.ChannelParticipantIdsResponse;
import com.sprint.mission.discodeit.dto.entity.user.UserDto;
import com.sprint.mission.discodeit.dto.mapper.ChannelMapper;
import com.sprint.mission.discodeit.dto.mapper.UserMapper;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.common.enums.ChannelScope;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
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
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public ChannelDto createPublicChannel(PublicChannelCreateRequest dto) {
        Channel channel = Channel.createPublicChannel(
                dto.name(),
                dto.description()
        );
        channelRepository.save(channel);
        return ChannelMapper.toDto(channel, getLastMassageAt(channel));
    }

    @Override
    public ChannelParticipantIdsResponse createPrivateChannel(PrivateChannelCreateRequest dto) {
        Channel channel = Channel.createPrivateChannel(
                dto.participantIds().stream()
                        .map(u -> userRepository.findById(u)
                                .orElseThrow(() -> new UserNotFoundException(u)))
                        .collect(Collectors.toSet())
        );
        return ChannelParticipantIdsResponse.toDto(channel);
    }


    @Override
    public ChannelDto update(UUID id, ChannelUpdateRequest dto) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new ChannelNotFoundException(id));
        if (channel.getType() == ChannelScope.PRIVATE) {
            throw new IllegalArgumentException("Private 채널은 수정할 수 없습니다.");
        }

        if (dto.newName() != null) {
            channel.setName(dto.newName());
        }

        if (dto.newDescription() != null) {
            channel.setDescription(dto.newDescription());
        }

        channelRepository.save(channel);
        return ChannelMapper.toDto(channel, getLastMassageAt(channel));
    }

    @Override
    public void delete(UUID id) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new ChannelNotFoundException(id));
        channelRepository.deleteById(id);
        messageRepository.deleteAllByChannel(channel);
        readStatusRepository.deleteAllByChannel(channel);
    }

    @Override
    public List<ChannelDto> getAll() {
        return channelRepository.findAll().stream()
                .map(channel -> ChannelMapper.toDto(channel, getLastMassageAt(channel)))
                .toList();
    }

    @Override
    public List<ChannelDto> getAllVisibleByUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return Stream.concat(channelRepository.findAllPublic().stream(), channelRepository.findAllPrivateByUser(user).stream())
                .map(c -> {
                    Optional<Message> last = messageRepository.findLastByChannel(c);
                    Instant lastMessageAt = null;
                    if (last.isPresent()) {
                        lastMessageAt = last.get().getCreatedAt();
                    }
                    return ChannelMapper.toDto(c, lastMessageAt);
                })
                .toList();
    }

    @Override
    public ChannelDto get(UUID id) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new ChannelNotFoundException(id));
        return ChannelMapper.toDto(channel
        , getLastMassageAt(channel));
    }

    @Override
    public List<UserDto> getAllParticipants(UUID id) {
        return channelRepository.findById(id)
                .orElseThrow(() -> new ChannelNotFoundException(id)).getParticipants().stream()
                .map(UserMapper::toDto)
                .toList();
    }

    @Override
    public void addParticipant(ChannelMemberRequest dto) {
        List<User> users = userRepository.findAllById(dto.userUuids());
        Channel channel = channelRepository.findById(dto.channelId())
                .orElseThrow(() -> new ChannelNotFoundException(dto.channelId()));
        channel.addParticipant(new HashSet<>(users));
    }

    @Override
    public void removeParticipant(ChannelMemberRequest dto) {
        List<User> users = userRepository.findAllById(dto.userUuids());
        channelRepository.findById(dto.channelId())
                .orElseThrow(() -> new ChannelNotFoundException(dto.channelId()))
                .removeParticipant(new HashSet<>(users));
    }

    private Instant getLastMassageAt(Channel channel) {
        Optional<Message> lastByChannel = messageRepository.findLastByChannel(channel);
        return lastByChannel.map(BaseUpdatableEntity::getUpdatedAt).orElse(null);
    }
}
