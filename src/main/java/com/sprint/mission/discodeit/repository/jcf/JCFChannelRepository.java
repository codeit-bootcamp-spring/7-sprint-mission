package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * JCFChannelRepository
 * -----------------
 * Java Collection Framework(JCF)을 이용해 메시지를 메모리에 저장하는 구현체입니다.
 *
 * 실제 DB를 사용하지 않고 Map<UUID, Channel>를 저장소로 활용합니다.
 * joinedChannels에 유저가 속한 채널 UUID를 저장합니다.
 */
public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> channelStore = new HashMap<>();
    private final Map<UUID, Set<UUID>> joinedChannels = new HashMap<>();

    private JCFChannelRepository() {}

    private static JCFChannelRepository instance = new JCFChannelRepository();

    public static JCFChannelRepository getInstance() {
        return instance;
    }

    @Override
    public void save(Channel channel) {
        channelStore.put(channel.getId(), channel);
    }

    @Override
    public Channel findById(UUID id) {
        return Optional.ofNullable(channelStore.get(id))
                .orElseThrow(() -> new IllegalArgumentException("해당 UUID를 가진 채널이 존재하지 않습니다."));
    }

    @Override
    public List<Channel> findByUser(User user) {
        return joinedChannels.get(user.getId()).stream()
                .map(key -> channelStore.get(key))
                .collect(Collectors.toList());
    }

    @Override
    public List<Channel> findByType(ChannelType type) {
        return channelStore.values().stream()
                .filter(c -> c.getChannelType() == type)
                .collect(Collectors.toList());
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channelStore.values());
    }

    @Override
    public void updateName(UUID id, String name) {
        Channel channel = findById(id);
        channel.setChannelName(name);
        channelStore.put(id, channel);
    }

    @Override
    public void updateAdmin(UUID id, User admin) {
        Channel channel = findById(id);
        channel.setAdmin(admin);
        channelStore.put(id, channel);
    }

    @Override
    public void deleteById(UUID id) {
        // 삭제되는 채널 id를 유저들이 속해 있는 채널 리스트(joinedChannels)에서도 삭제
        findById(id).getMembers()
                .forEach(member -> deleteChannelIdForUser(id, member));

        channelStore.remove(id);
    }

    @Override
    public void addChannelIdForUser(UUID channelId, User user) {
        Set<UUID> channelIds;

        if (joinedChannels.containsKey(user.getId())) {
            channelIds = joinedChannels.get(user.getId());
        } else {
            channelIds = new HashSet<>();
        }

        channelIds.add(channelId);
        joinedChannels.put(user.getId(), channelIds); // 채널 추가 후 저장
    }

    @Override
    public void deleteChannelIdForUser(UUID channelId, User user) {
        Set<UUID> channelIds = Optional.ofNullable(joinedChannels.get(user.getId()))
                .orElseThrow(() -> new IllegalArgumentException("삭제될 유저가 속한 채널이 없거나 유저가 존재하지 않습니다."));

        channelIds.remove(channelId);
        joinedChannels.put(user.getId(), channelIds); // 채널 삭제 후 저장
    }

    @Override
    public void deleteMember(Channel channel, User target){
        deleteChannelIdForUser(channel.getId(), target); // 강퇴된 유저가 가진 채널 목록에서 채널 UUID 삭제
        channel.delMember(target); // 채널에서 강퇴된 유저 삭제
        save(channel); // 변경된 채널 정보 저장
    }
}
