package com.sprint.mssion.discodeit.service.jcf;

import com.sprint.mssion.discodeit.entity.Channel;
import com.sprint.mssion.discodeit.entity.Message;
import com.sprint.mssion.discodeit.entity.User;
import com.sprint.mssion.discodeit.service.ChannelService;

import java.util.*;

import static com.sprint.mssion.discodeit.service.jcf.JCFUserservice.userRepo;
import static com.sprint.mssion.discodeit.service.jcf.JCfMessageService.msgRepo;

public class JCFChannelService implements ChannelService {
    public final static Map<UUID, Channel> channelRepo;

    static {
        channelRepo = new HashMap<>();
    }

    @Override
    public Channel create(Channel.ChannelType type, String channelName, String channelDescription) {
        System.out.println("채널 생성");
        Channel channel = new Channel(type, channelName, channelDescription);
        channelRepo.put(channel.getCommon().getId(), channel);
        return channel;
    }

    @Override
    public Channel read(UUID channelId) {
        System.out.println("채널 단건 검색");
        if (channelRepo.containsKey(channelId)) {
            return channelRepo.get(channelId);
        }
        throw new NoSuchElementException("채널을 찾을 수 없습니다." + channelId);
    }

    @Override
    public List<Channel> readAll() {
        System.out.println("채널 전체 검색");
        List<Channel> channels = new ArrayList<>();
        for (UUID key : channelRepo.keySet()) {
            Channel channel = channelRepo.get(key);
            channels.add(channel);
        }
        return channels;
    }

    @Override
    public void update(UUID channelId, Channel.ChannelType type, String channelName, String channelDescription) {
        System.out.println("채널 업데이트");
        if (channelRepo.containsKey(channelId)) {
            Channel channel = channelRepo.get(channelId);
            channel.setType(type);
            channel.setChannelName(channelName);
            channel.setDesc(channelDescription);
            channel.getCommon().touch();
            return;
        }
        throw new NoSuchElementException("채널을 찾을 수 없습니다." + channelId);
    }

    @Override
    public void delete(UUID channelId) {
        System.out.println("채널 삭제");
        if (!channelRepo.containsKey(channelId)) {
            throw new NoSuchElementException("채널을 찾을 수 없습니다." + channelId);
        }
        // 채널 삭제를 하면, 해당 채널에 속해있던 각 유저 참여채널 리스트에서 해당 채널 삭제
        for (UUID key : userRepo.keySet()) {
            User user = userRepo.get(key);
            user.removeChannel(channelId);
        }

        // 해당 채널에 속해있던 메시지도 삭제
        Iterator<UUID> msgIt = msgRepo.keySet().iterator();
        while (msgIt.hasNext()) {
            UUID key = msgIt.next();
            Message msg = msgRepo.get(key);
            if (msg.getChannelId().equals(channelId)) {
                msgIt.remove(); // 안전하게 제거
            }
        }
        channelRepo.remove(channelId);
        System.out.println("삭제 완료");

    }
}
