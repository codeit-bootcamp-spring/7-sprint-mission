package com.sprint.mission.discodeit.entity.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.service.ChannelService;

import java.util.LinkedList;
import java.util.List;

public class JCFChannelService implements ChannelService {

    private final List<Channel> channels = new LinkedList<>();;

    public JCFChannelService() {

    }




    @Override
    public  void create(User user,String channelName) {
        Channel ch = new Channel(user,channelName);
        channels.add(ch);
        ch.setChannelName(channelName);
        System.out.printf("%s가 채널을 만들엇습니다\n",user.getUserName());

    }

    @Override
    public  void read(Channel channel) {
        System.out.println("===== 채널 정보 =====");
        System.out.printf("채널 ID: %s%n",channel.getId().toString().replace("-", ""));
        System.out.printf("생성 시각: %s%n",channel.getCreatedAt());
        System.out.printf("채널 이름: %s%n",channel.getChannelName());
        System.out.printf("채널장(boss): %s\n",channel.getBose().getUserNickname());
        System.out.println("멤버 목록:");
        for (User u : channel.getUsers()) {
            System.out.println(" - " + u.getUserNickname());
        }
        System.out.println("====================");

    }

    //파라미터 어떻게 해야할것같다
    @Override
    public void update(Channel channel,String channelName) {
           channel.setChannelName(channelName);
    }


    @Override
    public void readAll() {
        System.out.println("채널 리스트");
        for (Channel channel : channels) {
            System.out.println(" - " + channel.getChannelName());
        }
    }

    @Override
    public void delete(Channel channel) {
           channels.remove(channel);
        System.out.printf("%s채널이 삭제되었습니다\n",channel.getChannelName());
    }
}
