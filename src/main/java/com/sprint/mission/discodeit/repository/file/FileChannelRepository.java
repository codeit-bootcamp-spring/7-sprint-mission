package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ChannelVisibility;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> channelStore = new HashMap<>();
    private final Map<UUID, Set<UUID>> joinedChannels = new HashMap<>();
    private final String channelPath;
    private final String joinedPath;

    public FileChannelRepository(String channelPath, String joinedPath) {
        this.channelPath = channelPath;
        this.joinedPath = joinedPath;
        loadChannels();
        loadJoinedChannels();
    }

    // --- 채널 정보 저장 ---
    private void saveChannels() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(channelPath))) {
            oos.writeObject(channelStore);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- 유저-채널 관계 저장 ---
    private void saveJoinedChannels() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(joinedPath))) {
            oos.writeObject(joinedChannels);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadChannels() {
        File file = new File(channelPath);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(channelPath))) {
            Map<UUID, Channel> loaded = (Map<UUID, Channel>) ois.readObject();
            channelStore.clear();
            channelStore.putAll(loaded);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadJoinedChannels() {
        File file = new File(joinedPath);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(joinedPath))) {
            Map<UUID, Set<UUID>> loaded = (Map<UUID, Set<UUID>>) ois.readObject();
            joinedChannels.clear();
            joinedChannels.putAll(loaded);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 유저가 속한 채널 id 목록을 joinedChannels Map에 저장
    // key : user uuid
    // value : 유저가 속한 channel uuid 목록
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

    // 채널에서 유저가 나가거나 채널이 삭제되면 joinedChannels 목록에서 삭제
    @Override
    public void deleteChannelIdForUser(UUID channelId, UUID userId) {
        Set<UUID> channelIds = joinedChannels.get(userId);

        channelIds.remove(channelId);
        joinedChannels.put(userId, channelIds); // 채널 삭제 후 저장
    }

    @Override
    public void save(Channel channel) {
        channelStore.put(channel.getId(), channel);
        saveChannels();

        // 비공개 채널의 경우에만 참여한 채널 정보 저장
        if(channel.getVisibility() == ChannelVisibility.PRIVATE) {
            saveJoinedChannels();
        }
    }

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
        saveChannels();
    }

    @Override
    public void deleteById(UUID channelId) {
        // 삭제되는 채널 id를 유저들이 속해 있는 채널 리스트(joinedChannels)에서도 삭제
        findById(channelId).ifPresent(channel -> channel.getMemberIds()
                .forEach(member -> deleteChannelIdForUser(channelId, member))
        );

        channelStore.remove(channelId);

        // 삭제된 후 목록 저장
        saveChannels();
        saveJoinedChannels();
    }

    @Override
    public void deleteMember(Channel channel, UUID targetId){
        deleteChannelIdForUser(channel.getId(), targetId); // 강퇴된 유저가 가진 채널 목록에서 채널 UUID 삭제
        channel.delMember(targetId); // 채널에서 강퇴된 유저 삭제
        save(channel); // 변경된 채널 정보 저장

        // 삭제된 후 목록 저장
        saveChannels();
        saveJoinedChannels();
    }

    @Override
    public boolean existsByName(String name) {
        return findAll().stream().anyMatch(c -> c.getChannelName().equals(name));
    }

    @Override
    public boolean existsById(UUID channelId) {
        return channelStore.containsKey(channelId);
    }

    @Override
    public boolean isUserJoinedChannel(UUID userId, UUID channelId){
        return joinedChannels.get(userId).stream()
                .anyMatch(id -> channelId.equals(id));
    }






}
