package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    public JCFChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel createChannel(ChannelType channelType, String channelName, User admin) {
        Channel newChannel = new Channel(channelType, channelName, admin);

        // 이름 중복 저장 불가
        for(Channel c : channelRepository.findAll()){
            if(c.getChannelName().equals(channelName)){
                System.out.println("동일한 이름의 채널이 이미 존재합니다.");
                return null;
            }
        }

        channelRepository.addChannelIdForUser(newChannel.getId(), admin);
        channelRepository.save(newChannel);
        return newChannel;
    }

    @Override
    public void addMember(UUID channelId, User member){
        Channel channel = channelRepository.findById(channelId).orElse(null);
        if (channel != null) {
            if(channel.getMembers().contains(member)){
                System.out.println("이미 멤버가 채널에 속해있습니다.");
                return;
            }
            channel.addMember(member);
            channelRepository.addChannelIdForUser(channel.getId(), member); // 유저 객체에 속한 채널 UUID 리스트 저장
            channelRepository.save(channel);
        }
    }

    @Override
    public Channel getChannel(UUID id){
        return channelRepository.findById(id).orElse(null);
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
        Channel channel = channelRepository.findById(id).orElse(null);

        if (!channel.getMembers().contains(user)) {
            System.out.println("그 유저는 이 채널에 속해 있지 않아 관리자로 변경할 수 없습니다.");
            return;
        } else if (channel.getAdmin().equals(user)) {
            System.out.println("그 유저는 이미 이 채널의 관리자 입니다.");
            return;
        }

        channelRepository.updateAdmin(id, user);
    }

    @Override
    public void updateName(UUID id, String name) {
        channelRepository.updateName(id, name);
    }

    @Override
    public void deleteChannel(UUID id, User user) {
        Channel channel = channelRepository.findById(id).orElse(null);

        if(!channel.getAdmin().equals(user)) {
            System.out.println("관리자가 아니므로 채널을 삭제할 수 없습니다.");
            return;
        }

        channelRepository.deleteById(id); // 채널 삭제
    }

    @Override
    public void deleteChannelMember(UUID id, User requester, User target) {
        Channel channel = channelRepository.findById(id).orElse(null);

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

        channelRepository.deleteMember(channel, target);
    }
}
