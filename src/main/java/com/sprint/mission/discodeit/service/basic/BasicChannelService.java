package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.entity.dto.channelDto.ChannelUpdateDto;
import com.sprint.mission.discodeit.entity.dto.channelDto.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.entity.dto.channelDto.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.entity.status.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.exception.NotFoundUserException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.entity.dto.channelDto.ChannelInfoDto;
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

    private ChannelInfoDto toDto(Channel channel) {
//        Optional<Message> lastMessage = messageRepository.findAllByChannelId(channel.getId())
//                .stream().findFirst();

        // 맨 위 메시지 찾기 -> createAt에서 max를 사용해서 찾는걸로
        Optional<Message> lastMessage = messageRepository.findTopByChannelId(channel.getId());
        return ChannelInfoDto.from(channel, lastMessage.orElse(null));
    }


    @Override
    public ChannelInfoDto createPublicChannel(PublicChannelCreateRequestDto createDto) {
        User admin = userRepository.findById(createDto.adminId())
                .orElseThrow(() -> new NotFoundUserException("사용자를 찾을 수 없음"));

        Channel newChannel = new Channel(admin, createDto.channelName(), ChannelType.PUBLIC);
        channelRepository.save(newChannel);
        return toDto(newChannel);
    }

    @Override
    public ChannelInfoDto createPrivateChannel(PrivateChannelCreateRequestDto createDto) {
        User admin = userRepository.findById(createDto.adminId())
                .orElseThrow(() -> new NotFoundUserException("사용자를 찾을 수 없음"));

        Channel newChannel = new Channel(admin, null, ChannelType.PRIVATE);

        Set<UUID> privateMemberIds = new HashSet<>(createDto.memberIds());
        for (UUID memberIds : privateMemberIds) {
            User member = userRepository.findById(memberIds)
                    .orElseThrow(() -> new NotFoundUserException("멤버를 찾을 수 없음"));
            newChannel.addMember(member);
        }

        channelRepository.save(newChannel);
        return toDto(newChannel);
    }

    @Override
    public Optional<ChannelInfoDto> findChannelInfoById(UUID id) {
        return channelRepository.findById(id).map(this::toDto);
    }

    // message에 채널을 주기위해
    public Optional<Channel> findChannelEntityById(UUID id) {
        return channelRepository.findById(id);
    }


    @Override
    public List<ChannelInfoDto> findAllByUserId(UUID userId) {
        List<Channel> allChannels = channelRepository.findAll();

        return allChannels.stream().filter(channel -> {
            if (channel.getType() == ChannelType.PUBLIC) {
                return true;
            }
            else {
                return channel.getMembers().stream()
                        .anyMatch(member -> member.getId().equals(userId));
            }
        })
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ChannelInfoDto> findChannelInfoByChannelName(String channelName) {
        return channelRepository.findByChannelName(channelName).map(this::toDto);
    }

    public Optional<ChannelInfoDto> findChannelByName(String channelName) {
        return channelRepository.findByChannelName(channelName).map(this::toDto);
    }

    @Override
    public Optional<ChannelInfoDto> updateChannelName(ChannelUpdateDto updateDto) {

        return channelRepository.findById(updateDto.channelId()).map(channel -> {
            if (channel.getType() == ChannelType.PRIVATE) {
                throw new IllegalStateException("PRIVATE 채널은 수정할 수 없습니다.");
            }
            channel.changeChannelName(updateDto.newChannelName());

            channelRepository.save(channel);
            return toDto(channel);
        });
    }

    @Override
    public Optional<ChannelInfoDto> addMemberToChannel(UUID channelId, UUID userId) {

        Optional<Channel> channelOp = channelRepository.findById(channelId);
        Optional<User> userOp = userRepository.findById(userId);

        if (channelOp.isEmpty() || userOp.isEmpty()){
            System.out.println("채널 또는 사용자를 찾을 수 없음");
            return Optional.empty();
        }

        Channel channel = channelOp.get();
        if (channel.getType() == ChannelType.PRIVATE) {
            System.out.println("Private 채널은 수정이 불가능합니다.");
            return Optional.empty();
        }

        User user = userOp.get();
        if (channel.addMember(user)) {
            System.out.println(user.getUserName() + " 님이 " + channel.getChannelName() + " 에 참가");
        } else System.out.println("이미 참여하고 있는 유저");

        channelRepository.save(channel);
        return Optional.of(toDto(channel));


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
    public Optional<ChannelInfoDto> removeMemberFromChannel(UUID channelId, UUID userId) {
        Optional<Channel> channelOp = channelRepository.findById(channelId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (channelOp.isEmpty() || userOptional.isEmpty()) {
            System.out.println("잘못된 입력");
            return Optional.empty();
        }

        Channel channel = channelOp.get();
        if (channel.getType() == ChannelType.PRIVATE) {
            System.out.println("Private 채널은 수정이 불가능합니다.");
            return Optional.empty();
        }

        User user = userOptional.get();
        if (channel.removeMember(user)) {
            System.out.println(user.getUserName() + " 님이 " + channel.getChannelName() + " 에서 삭제됨");
        } else System.out.println("채널에 없는 유저");

        channelRepository.save(channel);
        return Optional.of(toDto(channel));
    }

    @Override
    public boolean deleteChannel(UUID id) {

        return channelRepository.findById(id).map(channel -> {
            channelRepository.deleteById(id);
            return true;
        }).orElse(false);
    }
}
