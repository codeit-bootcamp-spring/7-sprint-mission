package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileChannelService implements ChannelService {
    private final Map<UUID, Channel> channelStore = new HashMap<>();
    private final Map<UUID, Set<UUID>> joinedChannels = new HashMap<>();
    private final String CHANNEL_FILE;
    private final String JOINED_FILE;

    public FileChannelService(String CHANNEL_FILE,  String JOINED_FILE) {
        this.CHANNEL_FILE = CHANNEL_FILE;
        this.JOINED_FILE = JOINED_FILE;
        loadChannels();
        loadJoinedChannels();
    }

    // --- 채널 정보 저장 ---
    private void saveChannels() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CHANNEL_FILE))) {
            oos.writeObject(channelStore);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- 유저-채널 관계 저장 ---
    private void saveJoinedChannels() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(JOINED_FILE))) {
            oos.writeObject(joinedChannels);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadChannels() {
        File file = new File(CHANNEL_FILE);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CHANNEL_FILE))) {
            Map<UUID, Channel> loaded = (Map<UUID, Channel>) ois.readObject();
            channelStore.clear();
            channelStore.putAll(loaded);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadJoinedChannels() {
        File file = new File(JOINED_FILE);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(JOINED_FILE))) {
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
    public void deleteChannelIdForUser(UUID channelId, User user) {
        Set<UUID> channelIds = joinedChannels.get(user.getId());
        if (channelIds != null) {
            channelIds.remove(channelId);
        }
        joinedChannels.put(user.getId(), channelIds); // 채널 삭제 후 저장
    }


    @Override
    public Channel createChannel(Channel.ChannelType channelType, String channelName, User admin) {
        Channel newChannel = new Channel(channelType, channelName, admin);

        // 이름 중복 저장 불가
        for(Channel c : channelStore.values()){
            if(c.getChannelName().equals(channelName)){
                System.out.println("동일한 이름의 채널이 이미 존재합니다.");
                return null;
            }
        }

        channelStore.put(newChannel.getId(), newChannel);
        addChannelIdForUser(newChannel.getId(), admin); // 유저 객체에 속한 채널 UUID 리스트 저장
        saveChannels();
        saveJoinedChannels();
        return newChannel;
    }

    @Override
    public void addMember(UUID channelId, User member){
        Channel channel = channelStore.get(channelId);

        if(channel.getMembers().contains(member)){
            System.out.println("이미 멤버가 채널에 속해있습니다.");
            return;
        }

        channel.addMember(member);
        channelStore.put(channel.getId(), channel);
        addChannelIdForUser(channelId, member); // 유저 객체에 속한 채널 UUID 리스트 저장
        saveChannels();
        saveJoinedChannels();
    }

    @Override
    public Channel getChannel(UUID id){
        return channelStore.get(id);
    }

    @Override
    public List<Channel> getChannelByUser(User user) {
        return joinedChannels.get(user.getId()).stream()
                .map(key -> channelStore.get(key))
                .collect(Collectors.toList());
    }

    @Override
    public List<Channel> getChannelByType(Channel.ChannelType type) {
        return channelStore.values().stream()
                .filter(c -> c.getChannelType() == type)
                .collect(Collectors.toList());
    }

    @Override
    public List<Channel> getAllChannels() {
        return new ArrayList<>(channelStore.values());
    }

    @Override
    public void updateAdmin(UUID id, User user) {
        Channel channel = channelStore.get(id);

        if (!channel.getMembers().contains(user)) {
            System.out.println("그 유저는 이 채널에 속해 있지 않아 관리자로 변경할 수 없습니다.");
            return;
        } else if (channel.getAdmin().equals(user)) {
            System.out.println("그 유저는 이미 이 채널의 관리자 입니다.");
            return;
        }

        channel.setAdmin(user);
        channelStore.put(id, channel);
        saveChannels();
    }

    @Override
    public void updateName(UUID id, String name) {
        Channel channel = channelStore.get(id);
        channel.setChannelName(name);
        channelStore.put(id, channel);
        saveChannels();
    }

    @Override
    public void deleteChannel(UUID id, User user) {
        Channel channel = channelStore.get(id);

        if(!channel.getAdmin().equals(user)) {
            System.out.println("관리자가 아니므로 채널을 삭제할 수 없습니다.");
            return;
        }

        // 삭제되는 채널 id를 유저들이 속해 있는 채널 리스트(joinedChannels)에서도 삭제
        for(User member : channel.getMembers()){
            deleteChannelIdForUser(id, member);
        }

        channelStore.remove(id);

        // 삭제된 후 목록 저장
        saveChannels();
        saveJoinedChannels();
    }

    @Override
    public void deleteChannelMember(UUID id, User requester, User target) {
        Channel channel = channelStore.get(id);

        if(channel == null){
            System.out.println("해당 채널이 존재하지 않습니다.");
            return;
        }

        //삭제 요청 유저와 삭제될 유저가 동일하지 않으면
        if(!requester.getId().equals(target.getId())){
            if(!channel.getAdmin().equals(requester)) { //삭제 요청 유저가 관리자가 아니라면 삭제 거부
                System.out.println("관리자가 아니므로 다른 유저를 채널에서 삭제할 수 없습니다.");
                return;
            } else if(!channel.getMembers().contains(target)) {
                System.out.println("삭제하려는 유저가 채널에 속해 있지 않습니다.");
                return;
            }
        } else if(channel.getAdmin().equals(target)) {
            System.out.println("당신은 관리자이므로 채널을 나갈 수 없습니다.");
            return;
        }

        deleteChannelIdForUser(id, target); // 강퇴된 유저가 가진 채널 목록에서 채널 UUID 삭제
        channel.delMember(target); // 채널에서 강퇴된 유저 삭제
        channelStore.put(channel.getId(), channel); // 변경된 채널 정보 저장

        // 삭제된 후 목록 저장
        saveChannels();
        saveJoinedChannels();
    }
}
