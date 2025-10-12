package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> channelStore = new HashMap<>();
    private final UserService userService;

    public JCFChannelService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Channel createChannel(Channel.ChannelType channelType, String channelName, User admin) {
        Channel newChannel = new Channel(channelType, channelName, admin);
        channelStore.put(newChannel.getId(), newChannel);
        admin.addChannelId(newChannel.getId()); // 유저 객체에 속한 채널 UUID 리스트 저장
        return newChannel;
    }

    @Override
    public void addMember(UUID id, User member){
        channelStore.get(id).addMember(member);
        member.addChannelId(id); // 유저 객체에 속한 채널 UUID 리스트 저장
    }

    @Override
    public Channel getChannel(UUID id){
        return channelStore.get(id);
    }

    @Override
    public List<Channel> getChannelByUser(User user) {
        List<Channel> joinedChannels = new ArrayList<>();

        for(UUID id : user.getChannelIds()) {
            joinedChannels.add(channelStore.get(id));
        }

        return joinedChannels;
    }

    @Override
    public List<Channel> getChannelByType(Channel.ChannelType channelType) {
        List<Channel> channels = new ArrayList<>();

        for(UUID id : channelStore.keySet()){
            if(channelStore.get(id).getChannelType().equals(channelType)){
                channels.add(channelStore.get(id));
            }
        }

        return channels;
    }

    @Override
    public List<Channel> getAllChannels() {
        return new ArrayList<>(channelStore.values());
    }

    @Override
    public void updateAdmin(UUID id, User user) {
        Channel channel = channelStore.get(id);

        if (!channel.getMembers().contains(user)) {
            System.out.println("그 유저는 이 채널에 속해 있지 않아 관리자로 변경할 수 없습니다.");
            return;
        } else if (channel.getAdmin().equals(user)) {
            System.out.println("그 유저는 이미 이 채널의 관리자 입니다.");
            return;
        }

        channel.setAdmin(user);
    }

    @Override
    public void updateName(UUID id, String name) {
        Channel channel = channelStore.get(id);
        channel.setChannelName(name);
    }

    @Override
    public void delChannel(UUID id, User user) {
        Channel channel = channelStore.get(id);

        if(!channel.getAdmin().equals(user)) {
            System.out.println("관리자가 아니므로 채널을 삭제할 수 없습니다.");
            return;
        }

        userService.removeChannelFromUser(id, user); //유저에 저장된 채널 id 삭제
        channelStore.remove(id);
    }

    @Override
    public void delChannelMember(UUID id, User requester, User target) {
        Channel channel = channelStore.get(id);

        if(channel == null){
            System.out.println("해당 채널이 존재하지 않습니다.");
            return;
        }

        //삭제 요청 유저와 삭제될 유저가 동일하지 않으면
        if(!requester.getId().equals(target.getId())){
            if(!channel.getAdmin().equals(requester)) { //삭제 요청 유저가 관리자가 아니라면 삭제 거부
                System.out.println("관리자가 아니므로 다른 유저를 채널에서 삭제할 수 없습니다.");
                return;
            } else if(!channel.getMembers().contains(target)) {
                System.out.println("삭제하려는 유저가 채널에 속해 있지 않습니다.");
                return;
            }
        } else if(channel.getAdmin().equals(target)) {
            System.out.println("당신은 관리자이므로 채널을 나갈 수 없습니다.");
            return;
        }

        userService.removeChannelFromUser(id, target);
        channel.delMember(target);
    }
}
