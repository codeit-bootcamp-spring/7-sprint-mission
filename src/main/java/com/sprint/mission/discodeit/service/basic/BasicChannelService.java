package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel createChannel(ChannelType channelType, String channelName, User admin) {
        existsByName(channelName); // 이름 중복 저장 불가

        Channel newChannel = new Channel(channelType, channelName, admin);
        channelRepository.addChannelIdForUser(newChannel.getId(), admin); // 유저 객체에 속한 채널 UUID 리스트 저장
        channelRepository.save(newChannel);
        return newChannel;
    }

    @Override
    public void addMember(UUID channelId, User member){
        Channel channel = channelRepository.findById(channelId);

        if(channel.getMembers().contains(member)){
            throw new IllegalArgumentException("이미 멤버가 채널에 속해있습니다.");
        }

        channel.addMember(member);
        channelRepository.addChannelIdForUser(channel.getId(), member); // 유저 객체에 속한 채널 UUID 리스트 저장
        channelRepository.save(channel);
    }

    @Override
    public Channel getChannel(UUID id){
        return channelRepository.findById(id);
    }

    @Override
    public List<Channel> getChannelByUser(User user) {
        return channelRepository.findByUser(user);
    }

    @Override
    public List<Channel> getChannelByType(ChannelType type) {
        return channelRepository.findByType(type);
    }

    @Override
    public List<Channel> getAllChannels() {
        return channelRepository.findAll();
    }

    @Override
    public void updateAdmin(UUID id, User user) {
        Channel channel = channelRepository.findById(id);

        if (!channel.getMembers().contains(user)) {
            throw new IllegalArgumentException("그 유저는 이 채널에 속해 있지 않아 관리자로 변경할 수 없습니다.");
        } else if (channel.getAdmin().equals(user)) {
            throw new IllegalArgumentException("그 유저는 이미 이 채널의 관리자 입니다.");
        }

        channelRepository.updateAdmin(id, user);
    }

    @Override
    public void updateName(UUID id, String name) {
        existsByName(name);
        channelRepository.updateName(id, name);
    }

    @Override
    public void deleteChannel(UUID id, User user) {
        Channel channel = channelRepository.findById(id);

        if(!user.equals(channel.getAdmin())) {
            throw new IllegalArgumentException("관리자가 아니므로 채널을 삭제할 수 없습니다.");
        }

        channelRepository.deleteById(id); // 채널 삭제
    }

    @Override
    public void deleteChannelMember(UUID id, User requester, User target) {
        Channel channel = channelRepository.findById(id);

        //삭제 요청 유저와 삭제될 유저가 동일하지 않으면
        if(!requester.getId().equals(target.getId())){
            if(!requester.equals(channel.getAdmin())) { //삭제 요청 유저가 관리자가 아니라면 삭제 거부
                throw new IllegalArgumentException("관리자가 아니므로 다른 유저를 채널에서 삭제할 수 없습니다.");
            } else if(!channel.getMembers().contains(target)) {
                throw new IllegalArgumentException("삭제하려는 유저가 채널에 속해 있지 않습니다.");
            }
        } else if(channel.getAdmin().equals(target)) {
            throw new IllegalArgumentException("당신은 관리자이므로 채널을 나갈 수 없습니다.");
        }

        channelRepository.deleteMember(channel, target);
    }

    @Override
    public boolean isUserJoinedChannel(User user, Channel channel){
        return getChannelByUser(user).contains(channel);
    }

    @Override
    public void existsByName(String name) {
        boolean nameContaining = channelRepository.findAll().stream()
                .anyMatch(c -> name.equals(c.getChannelName()));

        if(nameContaining) {
            throw new IllegalArgumentException("채널 이름이 존재합니다. 다시 입력해주세요.");
        }
    }
}
