package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.entity.dto.channelDto.ChannelUpdateDto;
import com.sprint.mission.discodeit.entity.dto.channelDto.PrivateChannelRequestDto;
import com.sprint.mission.discodeit.entity.dto.channelDto.PublicChannelRequestDto;
import com.sprint.mission.discodeit.entity.entityType.ChannelType;
import com.sprint.mission.discodeit.exception.NotFoundChannelException;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.exception.NotFoundUserException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.entity.dto.channelDto.ChannelResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;

    private ChannelResponseDto toDto(Channel channel) {
//        Optional<Message> lastMessage = messageRepository.findAllByChannelId(channel.getId())
//                .stream().findFirst();

        // 맨 위 메시지 찾기 -> createAt에서 max를 사용해서 찾는걸로
        Optional<Message> lastMessage = messageRepository.findTopByChannelId(channel.getId());
        return ChannelResponseDto.from(channel, lastMessage.orElse(null));
    }


    @Override
    public ChannelResponseDto createPublicChannel(PublicChannelRequestDto requestDto) {
        User admin = userRepository.findById(requestDto.adminId())
                .orElseThrow(() -> new NotFoundUserException("사용자를 찾을 수 없음"));

        Channel newChannel = new Channel(admin, requestDto.channelName(), ChannelType.PUBLIC);
        readStatusRepository.save(new ReadStatus(admin.getId(), newChannel.getId()));
        channelRepository.save(newChannel);
        return toDto(newChannel);
    }

    @Override
    public ChannelResponseDto createPrivateChannel(PrivateChannelRequestDto requestDto) {
        User admin = userRepository.findById(requestDto.adminId())
                .orElseThrow(() -> new NotFoundUserException("사용자를 찾을 수 없음"));

        Channel newChannel = new Channel(admin, null, ChannelType.PRIVATE);

        Set<UUID> privateMemberIds = new HashSet<>(requestDto.memberIds());
        for (UUID memberIds : privateMemberIds) {
            User member = userRepository.findById(memberIds)
                    .orElseThrow(() -> new NotFoundUserException("멤버를 찾을 수 없음"));
            newChannel.addMember(member);
        }

        readStatusRepository.save(new ReadStatus(admin.getId(), newChannel.getId()));
        channelRepository.save(newChannel);
        return toDto(newChannel);
    }

    @Override
    public ChannelResponseDto findChannelInfoById(UUID id) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new NotFoundChannelException("채널을 찾을 수 없습니다."));

        return toDto(channel);
    }

    @Override
    public ChannelResponseDto findChannelInfoByChannelName(String channelName) {
        Channel channel = channelRepository.findByChannelName(channelName)
                .orElseThrow(() -> new NotFoundChannelException("채널을 찾을 수 없습니다."));

        return toDto(channel);
    }

    @Override
    public List<ChannelResponseDto> findAllByUserId(UUID userId) {
        List<Channel> allChannels = channelRepository.findAll();

        return allChannels.stream().filter(channel -> {
                    if (channel.getType() == ChannelType.PUBLIC) {
                        return true;
                    } else {
                        return channel.getMembers().stream()
                                .anyMatch(member -> member.getId().equals(userId));
                    }
                })
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ChannelResponseDto updateChannelName(ChannelUpdateDto updateDto) {

        Channel channel = channelRepository.findById(updateDto.channelId())
                .orElseThrow(() -> new NotFoundChannelException("채널을 찾을 수 없습니다."));

        if (channel.getType() == ChannelType.PRIVATE) {
            throw new IllegalStateException("PRIVATE 채널은 수정할 수 없습니다.");
        }
        channel.changeChannelName(updateDto.newChannelName());

        channelRepository.save(channel);
        return toDto(channel);

    }

    @Override
    public ChannelResponseDto addMemberToChannel(UUID channelId, UUID userId) {

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NotFoundChannelException("채널을 찾을 수 없습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUserException("사용자를 찾을 수 없습니다."));

        if (channel.getType() == ChannelType.PRIVATE) {
            throw new IllegalStateException("Private 채널은 수정이 불가능합니다.");
        }

        if (channel.addMember(user)) {
            readStatusRepository.save(new ReadStatus(userId, channelId));
            System.out.println(user.getUserName() + " 님이 " + channel.getChannelName() + " 에 참가");
            channelRepository.save(channel);

        } else System.out.println("이미 참여하고 있는 유저");

        return toDto(channel);


        /* flatMap으로 구현
        return channelRepository.findById(channelId)
                .flatMap(channel -> userService.findUserEntityById(userId)
                                .map(user -> {
                                    if (channel.addMember(user)) {
                                        System.out.println(user.getUserName() + " 님이 " + channel.getChannelName() + " 에 참가");
                                    } else System.out.println("이미 참여하고 있는 유저");
                                    channelRepository.save(channel);
                                    return ChannelInfo.from(channel);
                                })
                );
         */
    }

    @Override
    public ChannelResponseDto removeMemberFromChannel(UUID channelId, UUID userId) {

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NotFoundChannelException("채널을 찾을 수 없습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUserException("사용자를 찾을 수 없습니다."));

        if (channel.getType() == ChannelType.PRIVATE) {
            throw new IllegalStateException("Private 채널은 수정이 불가능합니다.");
        }

        if (channel.removeMember(user)) {
            readStatusRepository.deleteAllByUserIdAndChannelId(userId, channelId);
            System.out.println(user.getUserName() + " 님이 " + channel.getChannelName() + " 에서 삭제됨");
            channelRepository.save(channel);
        } else System.out.println("채널에 없는 유저");

        return toDto(channel);
    }

    @Override
    public boolean deleteChannel(UUID id) {

        return channelRepository.findById(id).map(channel -> {

            readStatusRepository.deleteAllByChannelId(id);
            messageRepository.deleteAllByChannelId(id);
            channelRepository.deleteById(id);

            return true;
        }).orElse(false);
    }
}
