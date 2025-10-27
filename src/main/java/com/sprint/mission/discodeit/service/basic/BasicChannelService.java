package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.entity.dto.channelDto.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.exception.NotFoundUserException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
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
    private final UserService userService;

    @Override
    public ChannelInfoDto createChannel(ChannelCreateRequestDto createDto) {
        User user = userService.findUserEntityById(createDto.getAdminId())
                .orElseThrow(() -> new NotFoundUserException("사용자를 찾을 수 없음"));

        Channel newChannel = new Channel(user, createDto.getChannelName(), createDto.getType());
        channelRepository.save(newChannel);
        return ChannelInfoDto.from(newChannel);
    }

    @Override
    public Optional<ChannelInfoDto> findChannelInfoById(UUID id) {
        return channelRepository.findById(id).map(ChannelInfoDto::from);
    }

    // message에 채널을 주기위해
    public Optional<Channel> findChannelEntityById(UUID id) {
        return channelRepository.findById(id);
    }

    @Override
    public List<ChannelInfoDto> findAll() {
        return channelRepository.findAll().stream().map(ChannelInfoDto::from).collect(Collectors.toList());
    }

    @Override
    public Optional<ChannelInfoDto> findChannelInfoByChannelName(String channelName) {
        return channelRepository.findByChannelName(channelName).map(ChannelInfoDto::from);
    }

    public Optional<ChannelInfoDto> findChannelByName(String channelName) {
        return channelRepository.findByChannelName(channelName).map(ChannelInfoDto::from);
    }

    @Override
    public Optional<ChannelInfoDto> updateChannelName(UUID id, String newChannelName) {

        return channelRepository.findById(id).map(channel -> {
            channel.changeChannelName(newChannelName);
            channelRepository.save(channel);
            return ChannelInfoDto.from(channel);
        });
    }

    @Override
    public Optional<ChannelInfoDto> addMemberToChannel(UUID channelId, UUID userId) {

        Optional<Channel> channelOp = channelRepository.findById(channelId);
        Optional<User> userOp = userService.findUserEntityById(userId);

        if (channelOp.isEmpty() || userOp.isEmpty()){
            System.out.println("채널 또는 사용자를 찾을 수 없음");
            return Optional.empty();
        }

        Channel channel = channelOp.get();
        User user = userOp.get();
        if (channel.addMember(user)) {
            System.out.println(user.getUserName() + " 님이 " + channel.getChannelName() + " 에 참가");
        } else System.out.println("이미 참여하고 있는 유저");

        channelRepository.save(channel);
        return Optional.of(ChannelInfoDto.from(channel));


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
        Optional<User> userOptional = userService.findUserEntityById(userId);

        if (channelOp.isEmpty() || userOptional.isEmpty()) {
            System.out.println("잘못된 입력");
            return Optional.empty();
        }

        Channel channel = channelOp.get();
        User user = userOptional.get();
        if (channel.removeMember(user)) {
            System.out.println(user.getUserName() + " 님이 " + channel.getChannelName() + " 에서 삭제됨");
        } else System.out.println("채널에 없는 유저");

        channelRepository.save(channel);
        return Optional.of(ChannelInfoDto.from(channel));
    }

    @Override
    public boolean deleteChannel(UUID id) {

        return channelRepository.findById(id).map(channel -> {
            channelRepository.deleteById(id);
            return true;
        }).orElse(false);

        /*
        if (channelRepository.findById(id).isPresent()) {
            channelRepository.deleteById(id);
            return true;
        }
        return false;
         */
    }
}
