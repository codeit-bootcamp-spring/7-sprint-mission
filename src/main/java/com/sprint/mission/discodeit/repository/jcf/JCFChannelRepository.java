package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
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

    @Override
    public void addChannelIdForUser(UUID channelId, UUID userId) {
        Set<UUID> channelIds;

        if (joinedChannels.containsKey(userId)) {
            channelIds = joinedChannels.get(userId);
        } else {
            channelIds = new HashSet<>();
        }

        channelIds.add(channelId);
        joinedChannels.put(userId, channelIds); // 채널 추가 후 저장
    }

    @Override
    public void deleteChannelIdForUser(UUID channelId, UUID userId) {
        Set<UUID> channelIds = joinedChannels.get(userId);

        channelIds.remove(channelId);
        joinedChannels.put(userId, channelIds); // 채널 삭제 후 저장
    }

    @Override
    public void save(Channel channel) { channelStore.put(channel.getId(), channel); }

    @Override
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(channelStore.get(id));
    }

    @Override
    public List<Channel> findByUser(UUID userId) {
        return joinedChannels.get(userId).stream()
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
    public void update(Channel channel) {
        channelStore.replace(channel.getId(), channel);
    }

    @Override
    public void deleteById(UUID channelId) {
        // 삭제되는 채널 id를 유저들이 속해 있는 채널 리스트(joinedChannels)에서도 삭제
        findById(channelId).ifPresent(channel -> channel.getMemberIds()
                .forEach(memberId -> deleteChannelIdForUser(channelId, memberId))
        );

        channelStore.remove(channelId);
    }

    @Override
    public void deleteMember(Channel channel, UUID targetId){
        deleteChannelIdForUser(channel.getId(), targetId); // 강퇴된 유저가 가진 채널 목록에서 채널 UUID 삭제
        channel.deleteMember(targetId); // 채널에서 강퇴된 유저 삭제
        save(channel); // 변경된 채널 정보 저장
    }

    @Override
    public List<UUID> findAllJoinedByUserId(UUID userId) {
        return new ArrayList<>(joinedChannels.get(userId));
    }
}
