package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.User;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import com.sprint.mission.discodeit.service.file.ReadService;
import com.sprint.mission.discodeit.service.file.LoadService;


public class FileChannelRepository implements ChannelRepository {

    private static final String filename = "channels";

    private List<Channel> loadAll() {
        List<Channel> list = ReadService.read(filename, Channel.class);
        return (list != null) ? list : new LinkedList<>();
    }

    private void saveAll(List<Channel> list) {
        LoadService.load(filename, list);
    }

    @Override
    public Channel create(User user, String channelName) {
        Channel ch = new Channel(user, channelName);
        List<Channel> channels = loadAll();
        channels.add(ch);
        saveAll(channels);
        System.out.printf("%s가 채널을 만들었습니다\n", user.getUserName());
        return ch;
    }

    @Override
    public Channel read(UUID channelId) {
        List<Channel> channels = loadAll();
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

    @Override
    public List<Channel> readAll() {
        List<Channel> channels = loadAll();
        System.out.printf("%d개의 채널@@@\n", channels.size());
        channels.forEach(System.out::println);
        return channels;
    }

    @Override
    public Channel update(UUID channelId, Consumer<Channel> updater) {
        System.out.println("수정");
        List<Channel> channels = loadAll();
        Channel channel = channels.stream()
                .filter(ch -> ch.getId().equals(channelId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("고유넘버 없다: " + channelId));

        updater.accept(channel);
        saveAll(channels);
        return channel;
    }

    @Override
    public boolean delete(UUID channelId) {
        List<Channel> channels = loadAll();
        boolean removed = channels.removeIf(ch -> ch.getId().equals(channelId));
        if (removed) {
            saveAll(channels);
        }
        return removed;
    }
}