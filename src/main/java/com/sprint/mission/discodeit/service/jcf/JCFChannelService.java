package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.UpdatedChannelDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

public class JCFChannelService implements ChannelService {

    private final Map<UUID, Channel> channels=new HashMap<>();

    @Override
    public void addChannel(Channel channel) {
        if (channel.getServerName()==null){
            System.out.println("채널 생성 실패");

        } else {
            UUID key = channel.getId();
            channels.put(key, channel);
            System.out.println("채널 생성 성공");
        }


    }


    @Override
    public void removeChannel(Channel channel) {
        UUID findChannelId = channel.getId();
        channels.remove(findChannelId);
        System.out.println("채널 삭제 성공");

    }

    //채널을 찾기위한 메서드.
    //UUID로 채널 탐색
    @Override
    public Channel getChannel(Channel channel) {
        UUID key = channel.getId();
        if(!channels.containsKey(key)){

            throw new NoSuchElementException("채널을 찾을 수 없습니다");
        }
        Channel findChannel = channels.get(key);
        System.out.println("채널 찾기 성공");

        return findChannel;
    }

    @Override
    public void updateChannel(UUID channelId, UpdatedChannelDTO updatedChannelDTO) {
        if(!channels.containsKey(channelId)){
            throw new NoSuchElementException("채널을 찾을 수 없습니다.");
        }
        Channel findChannel = channels.get(channelId);
        if (findChannel.isPrivate()!=updatedChannelDTO.isPrivate()){
            System.out.println("[채널 공개 설정이 "+findChannel.isPrivate() +"에서 "+updatedChannelDTO.isPrivate()+"로 변경되었습니다]");
            findChannel.setPrivate(updatedChannelDTO.isPrivate());
        }
        if (updatedChannelDTO.getServerLevel()!=null){
            System.out.printf("[채널 레벨이 %s에서 %s로 변경되었습니다]\n", findChannel.getServerLevel(), updatedChannelDTO.getServerLevel());
            findChannel.setServerLevel(updatedChannelDTO.getServerLevel());
        }
        if(updatedChannelDTO.getManager()!=null){
            System.out.printf("[채널 매니저가 %s에서 %s로 변경되었습니다]\n",
                    findChannel.getManager().getUsername(), updatedChannelDTO.getManager().getUsername());
            findChannel.setManager(updatedChannelDTO.getManager());
        }
        if(updatedChannelDTO.getServerName()!=null){
            System.out.printf("[채널 이름이 %s에서 %s로 변경되었습니다]\n",
                    findChannel.getServerName(), updatedChannelDTO.getServerName());
            findChannel.setServerName(updatedChannelDTO.getServerName());
        }
        channels.put(channelId, findChannel);
        System.out.println("채널 정보 변경 성공");

    }

    public Map<UUID, Channel> getChannels() {
        return channels;
    }
    public Channel openChannel(User user ,String serverName, Long serverLevel, boolean isPrivate){
        Channel channel = new Channel();
        channel.setManager(user);
        channel.setServerName(serverName);
        channel.setServerLevel(serverLevel);
        channel.setPrivate(isPrivate);
        user.getMyChannel().add(channel);

        channels.put(channel.getId(), channel);
        return channel;

    }

    public void inviteMember(User fromUser ,Channel channel, User toUser) {
        if (!fromUser.getMyChannel().contains(channel)) {
            System.out.println("서버에 있지 않습니다.");
            return;
        }
        if (!(channel.getManager() == fromUser)) {
            System.out.println("초대 권한이 없습니다.");
            return;
        }
        channel.getMembers().add(toUser);

    }



}
