package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.config.AppConfig;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FileChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> channelStore = new HashMap<>();
    private final Map<UUID, Set<UUID>> joinedChannels = new HashMap<>();
    private final String channelPath = AppConfig.DATA_PATH + "\\channels.sav";
    private final String joinedPath = AppConfig.DATA_PATH + "\\joined.sav";

    private FileChannelRepository() {
        loadChannels();
        loadJoinedChannels();
    }

    private static FileChannelRepository instance = new FileChannelRepository();

    public static FileChannelRepository getInstance() {
        return instance;
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

    // 채널에서 유저가 나가거나 채널이 삭제되면 joinedChannels 목록에서 삭제
    @Override
    public void deleteChannelIdForUser(UUID channelId, User user) {
        Set<UUID> channelIds = Optional.ofNullable(joinedChannels.get(user.getId()))
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        channelIds.remove(channelId);
        joinedChannels.put(user.getId(), channelIds); // 채널 삭제 후 저장
    }

    @Override
    public void save(Channel channel) {
        channelStore.put(channel.getId(), channel);
        saveChannels();
        saveJoinedChannels();
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
        saveChannels();
    }

    @Override
    public void updateAdmin(UUID id, User admin) {
        Channel channel = findById(id);
        channel.setAdmin(admin);
        channelStore.put(id, channel);
        saveChannels();
    }

    @Override
    public void deleteById(UUID id) {
        // 삭제되는 채널 id를 유저들이 속해 있는 채널 리스트(joinedChannels)에서도 삭제
        findById(id).getMembers()
                .forEach(member -> deleteChannelIdForUser(id, member));

        channelStore.remove(id);

        // 삭제된 후 목록 저장
        saveChannels();
        saveJoinedChannels();
    }

    @Override
    public void deleteMember(Channel channel, User target){
        deleteChannelIdForUser(channel.getId(), target); // 강퇴된 유저가 가진 채널 목록에서 채널 UUID 삭제
        channel.delMember(target); // 채널에서 강퇴된 유저 삭제
        save(channel); // 변경된 채널 정보 저장
    }
}
