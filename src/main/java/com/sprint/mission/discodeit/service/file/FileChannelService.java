package com.sprint.mission.discodeit.service.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileChannelService implements ChannelService {
    private final List<Channel> channels = new ArrayList<>();
    private static final FileChannelService singleton = new FileChannelService();

    public static FileChannelService getInstance() {
        return singleton;
    }

    private FileChannelService() {}

    //방 생성
    @Override
    public Channel insert(Channel channel) {
        //로그인을 구현하지 않았으므로 임의로 나의 이메일을..
        Channel newChannel = new Channel("방1", FileUserService.loginUser);
        channels.add(newChannel);
        return newChannel;
    }

    //방 이름 변경
    @Override
    public Channel update(UUID id, String name) {
        Channel channel = findById(id);
        channel.setName(name);
        channel.setUpdatedAt(System.currentTimeMillis());

        return channel;
    }

    //방 삭제
    @Override
    public Channel delete(UUID id) {
        Channel channel = findById(id);
        channels.remove(channel);

        return channel;
    }

    //모든 채널 띄우기
    @Override
    public List<Channel> findAll() {
        return List.copyOf(channels);
    }

    //채널 하나 찾기
    @Override
    public Channel findById(UUID id) {
        return channels.stream().filter(
                ch -> ch.getId().equals(id)).findFirst().orElseThrow(
                () -> new RuntimeException("해당 ID를 가진 Channel를 찾을 수 없습니다: " + id)
        );
    }

    //채널 이름으로 찾기
    @Override
    public Channel findByName(String name){
        return channels.stream().filter(
                ch -> ch.getName().equals(name)).findFirst().orElseThrow(
                () -> new RuntimeException("해당 이름을 가진 Channel을 찾을 수 없음: "+ name)
        );
    }
}
