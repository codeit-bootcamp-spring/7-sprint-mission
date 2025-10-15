package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.dto.ChannelInfo;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

import static com.sprint.mission.discodeit.service.file.FileUserService.ROOT_PATH;

public class FileChannelService implements ChannelService {

    private Map<UUID, Channel> data = new HashMap<>();
    private final UserService userService;

    public FileChannelService(UserService userService) {
        File file = new File(ROOT_PATH);
        if (!file.exists()) {
            file.mkdir();       // 생성해야 할 폴더 경로가 하나일 때
        }
        this.data = loadChannelData();
        this.userService = userService;
    }

    private Map<UUID, Channel> loadChannelData() {
        try (ObjectInputStream ois
                     = new ObjectInputStream(
                new FileInputStream(ROOT_PATH + "/channelData.ser"))) {
            return (Map<UUID, Channel>) ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException("채널 데이터 로드 실패");
        }
    }

    private void saveChannelData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(ROOT_PATH + "/channelData.ser"))){
            oos.writeObject(data);
            System.out.println("채널 데이터 저장 성공");
        } catch (Exception e) {
            throw new RuntimeException("채널 데이터 저장 실패");
        }
    }

    //============================================생성=====

    @Override
    public ChannelInfo createChannel(UUID userId, String channelName, Channel.ChannelType type) {
        User user = userService.findUserEntityById(userId)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없음"));
        if (channelName == null || channelName.isBlank())
            channelName = user.getUserName() + "의 채널";
        Channel newChannel = new Channel(user, channelName, type);
        this.data.put(newChannel.getId(), newChannel);

        saveChannelData();
        return new ChannelInfo(newChannel);
    }

    @Override
    public ChannelInfo createChannel(UUID userId, Channel.ChannelType type) {
        return this.createChannel(userId, null, type);
    }


    @Override
    public Optional<ChannelInfo> findChannelInfoById(UUID id) {
        return Optional.ofNullable(data.get(id)).map(ChannelInfo::new);
    }

    // message에 채널을 주기위해
    public Optional<Channel> findChannelEntityById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<ChannelInfo> findAll() {
        return data.values().stream().map(ChannelInfo::new).toList();
    }

    @Override
    public Optional<ChannelInfo> updateChannelName(UUID id, String newChannelName) {

        return findChannelEntityById(id).map(channel -> {
            channel.changeChannelName(newChannelName);
            saveChannelData();
            return new ChannelInfo(channel);
        });
    }

    @Override
    public Optional<ChannelInfo> addMemberToChannel(UUID channelId, UUID userId) {
        Optional<Channel> channelOp = findChannelEntityById(channelId);
        Optional<User> userOptional = userService.findUserEntityById(userId);

        if (channelOp.isEmpty() || userOptional.isEmpty()) {
            System.out.println("잘못된 입력");
            return Optional.empty();
        }
            Channel channel = channelOp.get();
            User user = userOptional.get();
            if (channel.addMember(user)) {
                System.out.println(user.getUserName() + " 님이 " + channel.getChannelName() + " 에 참가");
            } else System.out.println("이미 참여하고 있는 유저");
            saveChannelData();
            return Optional.of(new ChannelInfo(channel));

    }

    @Override
    public Optional<ChannelInfo> removeMemberFromChannel(UUID channelId, UUID userId) {
        Optional<Channel> channelOp = findChannelEntityById(channelId);
        Optional<User> userOptional = userService.findUserEntityById(userId);

        if (channelOp.isEmpty() || userOptional.isEmpty()) {
            System.out.println("잘못된 입력");
            return Optional.empty();
        }

        Channel channel = channelOp.get();
        User user = userOptional.get();
        if (channel.removeMember(user)) {
            System.out.println(user.getUserName() + " 님이 " + channel.getChannelName() + " 에서 삭제됨");
        } else System.out.println("채널에 없는 유저");
        saveChannelData();
        return Optional.of(new ChannelInfo(channel));

    }

    @Override
    public boolean deleteChannel(UUID id) {
        if (data.remove(id) != null) {
            System.out.println("채널 삭제 성공");
            saveChannelData();
            return true;
        } else {
            System.out.println("해당 채널이 존재하지 않음");
            return false;
        }
    }

}
