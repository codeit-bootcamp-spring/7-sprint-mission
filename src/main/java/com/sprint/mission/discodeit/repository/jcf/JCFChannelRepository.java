package com.sprint.mission.discodeit.repository.jcf;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.User;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class JCFChannelRepository implements ChannelRepository {

    private final List<Channel> channels = new LinkedList<>();

    private static final JCFChannelRepository INSTANCE = new JCFChannelRepository();
    public static JCFChannelRepository getInstance() { return INSTANCE; }
    private JCFChannelRepository() {}

    @Override
    public  Channel create(User user, String channelName) {
        Channel ch = new Channel(user, channelName);
        channels.add(ch);
        System.out.printf("%s가 채널을 만들었습니다\n", user.getUserName());
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

    @Override
    public  List<Channel> readAll() {
        System.out.printf("%d개의 채널@@@\n", channels.size());
        channels.forEach(System.out::println);
        return new LinkedList<>(channels);
    }

    @Override
    public  Channel update(UUID channelId, Consumer<Channel> updater) {
        System.out.println("수정");
        return channels.stream()
                .filter(ch -> ch.getId().equals(channelId))
                .findFirst()
                .map(ch -> {
                    updater.accept(ch);
                    return ch;
                })
                .orElseThrow(() -> new IllegalArgumentException("고유넘버 없다: " + channelId));
    }

    @Override
    public synchronized boolean delete(UUID channelId) {
        return channels.removeIf(ch -> ch.getId().equals(channelId));
    }
}
