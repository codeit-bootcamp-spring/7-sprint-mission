package com.sprint.mission.discodeit.entity.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.service.ChannelService;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.function.Consumer;

public class JCFChannelService implements ChannelService {

    private  final  List<Channel>  channels;

    private static final JCFChannelService INSTANCE = new JCFChannelService();

    private  JCFChannelService(){
        channels = new LinkedList<>();
    }

    //이것도 다 똑같다
    public static JCFChannelService getInstance(){
        return INSTANCE;
    }





    @Override
    public Channel create(User user,String channelName) {
        Channel ch = new Channel(user,channelName);
        channels.add(ch);
        ch.setChannelName(channelName);
        System.out.printf("%s가 채널을 만들엇습니다\n",user.getUserName());
        return ch;
    }

    @Override
    public  Channel read(UUID channelId) {
        Channel channel = channels.stream()
                .filter(ch -> ch.getId().equals(channelId))
                .findFirst()
                .orElse(null);
        if (channel == null) {
            System.out.println("해당 채널 없습니다: " + channelId);
        } else {
            System.out.println(channel);
        }
       return channel;
    }

    //파라미터 어떻게 해야할것같다
    @Override
    public Channel update(UUID channelId, Consumer<Channel> updater) {
        System.out.println("수정");
        return channels.stream()
                .filter(u -> u.getId().equals(channelId))
                .findFirst()
                .map(u -> {
                    updater.accept(u); // 여러개 가능하게
                    return u;
                })
                .orElseThrow(() -> new IllegalArgumentException("고유넘버 없다: " + channelId));
    }


    @Override
    public List<Channel> readAll() {
        System.out.printf("%d개의 채널@@@\n",channels.size());
        channels
                .forEach(System.out::println);
        return channels;
    }

    @Override
    public boolean delete(UUID channelId) {
        return channels.removeIf(u -> u.getId().equals(channelId));

    }
}
