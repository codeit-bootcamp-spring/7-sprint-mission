package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    @Override
    public Channel createChannel(ChannelType channelType, String channelName, UUID adminId) {
        existsByName(channelName); // 이름 중복 저장 불가

        Channel newChannel = new Channel(channelType, channelName, adminId);
        channelRepository.addChannelIdForUser(newChannel.getId(), adminId); // 유저 객체에 속한 채널 UUID 리스트 저장
        channelRepository.save(newChannel);
        return newChannel;
    }

    @Override
    public void addMember(UUID channelId, UUID userId){
        Channel channel = channelRepository.findById(channelId);

        if(channel.getMembers().contains(userId)){
            throw new IllegalArgumentException("이미 멤버가 채널에 속해있습니다.");
        }

        channel.addMember(userId);
        channelRepository.addChannelIdForUser(channel.getId(), userId); // 유저 객체에 속한 채널 UUID 리스트 저장
        channelRepository.save(channel);
    }

    @Override
    public Channel getChannel(UUID id){
        return channelRepository.findById(id);
    }

    @Override
    public List<Channel> getChannelByUser(UUID userId) {
        return channelRepository.findByUser(userId);
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
    public void updateAdmin(UUID channelId, UUID userId) {
        Channel channel = channelRepository.findById(channelId);

        if (!channel.getMembers().contains(userId)) {
            throw new IllegalArgumentException("그 유저는 이 채널에 속해 있지 않아 관리자로 변경할 수 없습니다.");
        } else if (channel.getAdminId().equals(userId)) {
            throw new IllegalArgumentException("그 유저는 이미 이 채널의 관리자 입니다.");
        }

        channelRepository.updateAdmin(channelId, userId);
    }

    @Override
    public void updateName(UUID id, String name) {
        existsByName(name);
        channelRepository.updateName(id, name);
    }

    @Override
    public void deleteChannel(UUID channelId, UUID userId) {
        Channel channel = channelRepository.findById(channelId);

        if(!userId.equals(channel.getAdminId())) {
            throw new IllegalArgumentException("관리자가 아니므로 채널을 삭제할 수 없습니다.");
        }

        channelRepository.deleteById(channelId); // 채널 삭제
    }

    @Override
    public void deleteChannelMember(UUID id, UUID requesterId, UUID targetId) {
        Channel channel = channelRepository.findById(id);

        //삭제 요청 유저와 삭제될 유저가 동일하지 않으면
        if(!requesterId.equals(targetId)){
            if(!requesterId.equals(channel.getAdminId())) { //삭제 요청 유저가 관리자가 아니라면 삭제 거부
                throw new IllegalArgumentException("관리자가 아니므로 다른 유저를 채널에서 삭제할 수 없습니다.");
            } else if(!channel.getMembers().contains(targetId)) {
                throw new IllegalArgumentException("삭제하려는 유저가 채널에 속해 있지 않습니다.");
            }
        } else if(channel.getAdminId().equals(targetId)) {
            throw new IllegalArgumentException("당신은 관리자이므로 채널을 나갈 수 없습니다.");
        }

        channelRepository.deleteMember(channel, targetId);
    }

    @Override
    public boolean isUserJoinedChannel(UUID userId, Channel channel){
        return getChannelByUser(userId).contains(channel);
    }

    @Override
    public void existsByName(String name) {
        if (channelRepository.findAll().stream().anyMatch(c -> c.getChannelName().equals(name))) {
            throw new IllegalArgumentException("채널 이름이 존재합니다. 다시 입력해주세요.");
        }
    }
}
