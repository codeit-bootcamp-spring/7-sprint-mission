package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.entity.dto.ChannelInfo;

import java.util.*;
import java.util.stream.Collectors;


public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final UserService userService;

    public BasicChannelService(ChannelRepository channelRepository, UserService userService) {
        this.channelRepository = channelRepository;
        this.userService = userService;
    }

    @Override
    public ChannelInfo createChannel(UUID userId, String channelName, ChannelType type) {
        User user = userService.findUserEntityById(userId)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없음"));
        if (channelName == null || channelName.isBlank())
            channelName = user.getUserName() + "의 채널";

        Channel newChannel = new Channel(user, channelName, type);
        channelRepository.save(newChannel);
        return new ChannelInfo(newChannel);
    }

    @Override
    public ChannelInfo createChannel(UUID userId, ChannelType type) {
        return this.createChannel(userId, null, type);
    }


    @Override
    public Optional<ChannelInfo> findChannelInfoById(UUID id) {
        return channelRepository.findById(id).map(ChannelInfo::new);
    }

    // message에 채널을 주기위해
    public Optional<Channel> findChannelEntityById(UUID id) {
        return channelRepository.findById(id);
    }

    @Override
    public List<ChannelInfo> findAll() {
        return channelRepository.findAll().stream().map(ChannelInfo::new).collect(Collectors.toList());
    }

    @Override
    public Optional<ChannelInfo> findChannelInfoByChannelName(String channelName) {
        return channelRepository.findByChannelName(channelName).map(ChannelInfo::new);
    }

    public Optional<ChannelInfo> findChannelByName(String channelName) {
        return channelRepository.findByChannelName(channelName).map(ChannelInfo::new);
    }

    @Override
    public Optional<ChannelInfo> updateChannelName(UUID id, String newChannelName) {

        return channelRepository.findById(id).map(channel -> {
            channel.changeChannelName(newChannelName);
            channelRepository.save(channel);
            return new ChannelInfo(channel);
        });
    }

    @Override
    public Optional<ChannelInfo> addMemberToChannel(UUID channelId, UUID userId) {

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
        return Optional.of(new ChannelInfo(channel));


        /* flatMap으로 구현
        return channelRepository.findById(channelId)
                .flatMap(channel -> userService.findUserEntityById(userId)
                                .map(user -> {
                                    if (channel.addMember(user)) {
                                        System.out.println(user.getUserName() + " 님이 " + channel.getChannelName() + " 에 참가");
                                    } else System.out.println("이미 참여하고 있는 유저");
                                    channelRepository.save(channel);
                                    return new ChannelInfo(channel);
                                })
                );
         */
    }

    @Override
    public Optional<ChannelInfo> removeMemberFromChannel(UUID channelId, UUID userId) {
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
        return Optional.of(new ChannelInfo(channel));
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
