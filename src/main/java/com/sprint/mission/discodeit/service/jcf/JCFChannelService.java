package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.entity.dto.ChannelInfo;

import java.util.*;


public class JCFChannelService implements ChannelService {

    private final Map<UUID, Channel> data;
    private final UserService userService;

    public JCFChannelService(UserService userService) {
        this.data = new HashMap<>();
        this.userService = userService;
    }

    @Override
    public ChannelInfo createChannel(UUID userId, String channelName, Channel.ChannelType type) {
        User user = userService.findUserEntityById(userId)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없음"));
        if (channelName == null || channelName.isBlank())
            channelName = user.getUserName() + "의 채널";

        Channel newChannel = new Channel(user, channelName, type);
        this.data.put(newChannel.getId(), newChannel);
        return new ChannelInfo(newChannel);
    }
    @Override
    public ChannelInfo createChannel(UUID userId, Channel.ChannelType type) {
        return this.createChannel(userId, null, type);
    }


    @Override
    public Optional<ChannelInfo> findChannelInfoById(UUID id) {
        return Optional.ofNullable(data.get(id)).map(ChannelInfo::new);
    }

    // message에 채널을 주기위해
    public Optional<Channel> findChannelEntityById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<ChannelInfo> findAll() {
        return data.values().stream().map(ChannelInfo::new).toList();
    }

    @Override
    public Optional<ChannelInfo> updateChannelName(UUID id, String newChannelName) {

        return Optional.ofNullable(data.get(id)).map(channel -> {
            channel.changeChannelName(newChannelName);
            return new ChannelInfo(channel);
        });
    }

    @Override
    public Optional<ChannelInfo> addMemberToChannel(UUID channelId, UUID userId) {
        Optional<Channel> channelOp = Optional.ofNullable(data.get(channelId));
        Optional<User> userOptional = userService.findUserEntityById(userId);

        if (channelOp.isPresent() && userOptional.isPresent()) {
            Channel channel = channelOp.get();
            User user = userOptional.get();
            if (channel.addMember(user)) {
                System.out.println(user.getUserName() + " 님이 " + channel.getChannelName() + " 에 참가");
            }
            else System.out.println("이미 참여하고 있는 유저");
            return Optional.of(new ChannelInfo(channel));
        }
        System.out.println("잘못된 입력");
        return Optional.empty();
    }

    @Override
    public Optional<ChannelInfo> removeMemberFromChannel(UUID channelId, UUID userId) {
        Optional<Channel> channelOp = Optional.ofNullable(data.get(channelId));
        Optional<User> userOptional = userService.findUserEntityById(userId);
        if (channelOp.isPresent() && userOptional.isPresent()) {
            Channel channel = channelOp.get();
            User user = userOptional.get();
            if (channel.removeMember(user)) {
                System.out.println(user.getUserName() + " 님이 " + channel.getChannelName() + " 에서 삭제됨");
            }
            else System.out.println("채널에 없는 유저");
            return Optional.of(new ChannelInfo(channel));
        }
        System.out.println("잘못된 입력");
        return Optional.empty();
    }

    @Override
    public boolean deleteChannel(UUID id) {
        if (data.remove(id) != null) {
            System.out.println("채널 삭제 성공");
            return true;
        } else {
            System.out.println("해당 채널이 존재하지 않음");
            return false;
        }
    }
}
