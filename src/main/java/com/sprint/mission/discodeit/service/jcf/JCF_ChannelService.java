package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.sprint.mission.discodeit.entity.ChannelType.ONE_VS_ONE;

public class JCF_ChannelService implements ChannelService, MessageService {
    private static JCF_ChannelService channel = new JCF_ChannelService();
    public static JCF_ChannelService getInstance() {
        return channel;
    }

    public final Map<UUID, Channel> dataChannel;
    private JCF_ChannelService() {
        dataChannel = new HashMap<>();
    }


    //===============================
    //========== @Override ==========
    //===============================

    @Override
    public Channel createChannel(User ownerUser, String channelName){
        System.out.printf("\n\uD83C\uDF7F\uD83C\uDF7F [%s]가 [%s] 채널 생성 \uD83C\uDF7F\uD83C\uDF7F\n", ownerUser.getUserName(), channelName); // 🍵🍿

        Channel channel1 = new Channel(ownerUser, channelName);
        dataChannel.put(channel1.getId(), channel1);
        return channel1;
    }

    @Override
    public Channel getChannel(Channel channel) {
        Channel channel1 = dataChannel.get(channel.getId());
        System.out.println("\uD83D\uDC8C getChannel data : " + channel1); // 💌
        return channel1;
    }

    @Override
    public Channel[] getAllChannels() {
        for (Map.Entry<UUID, Channel> entry : dataChannel.entrySet()) {
            System.out.println("\uD83D\uDC8C getAllChannels data : " + entry.getValue()); // 💌
        }
        return dataChannel.values().toArray(new Channel[0]);
    }

    @Override
    public void updateChannelName(Channel channel, String name) {
        Channel channel1 = dataChannel.get(channel.getId());
        channel1.setChannelName(channel1.getChannelName(), name);
    }

    @Override
    public void updateChannelType(Channel channel, ChannelType channelType) {
        if ( channelType == ONE_VS_ONE && channel.users.size() > 1 ) {
            System.out.printf("\uD83D\uDEAB 인원수 초과로(총 %s명) [%s] 채널은 1:1 채널방으로 변경 불가 \n", channel.getUsers().size(), channel.getChannelName()); // 🚫
        }
        else {
            Channel channel1 = dataChannel.get(channel.getId());
            channel1.setChannelType(channelType);
            System.out.printf("\uD83D\uDEAB [%s] 채널은 [%s] 타입 채널방으로 변경됨\n", channel.getChannelName(), channelType); // 🚫
        }
    }

    @Override
    public void deleteChannel(UUID uuid) {
        Channel channel = dataChannel.get(uuid);
        Channel remove = dataChannel.remove(uuid);

        if (remove != null) {
            System.out.printf("\uD83D\uDEA8 [%s] 채널 삭제 \n", channel.getChannelName()); // 🚨
        }
        else {
            System.out.println("\uD83D\uDEA8\uD83D\uDEA8 [%s] 채널 삭제 err!"); // 🚨
        }
    }

    @Override
    public void getAll_Messages(Channel channel) {
        Channel channel1 = dataChannel.get(channel.getId());
        if (channel1 == null) {
            System.out.printf("\uD83D\uDEA8\uD83D\uDEA8 [%s]는 삭제된 채널임! - getAllMessages() 호출 불가 \n", channel.getChannelName()); // 🚨
        }
        else {
            channel1.getAllMessages();
        }
    }

    @Override
    public Message get_Message(Channel channel, UUID messageID) {
        Channel channel1 = dataChannel.get(channel.getId());
        if (channel1 == null) {
            System.out.printf("\uD83D\uDEA8\uD83D\uDEA8 [%s]는 삭제된 채널임! - getMessage() 호출 불가 \n", channel.getChannelName()); // 🚨
            return null;
        }
        else {
            return channel1.get_Message(messageID);
        }
    }

    @Override
    public void update_Message(Channel channel, UUID messageID, String message) {
        Channel channel1 = dataChannel.get(channel.getId());
        if (channel1 == null) {
            System.out.printf("\uD83D\uDEA8\uD83D\uDEA8 [%s]는 삭제된 채널임! - updateMessage() 호출 불가 \n", channel.getChannelName()); // 🚨
        }
        else {
            channel1.update_Message(messageID, message);
        };
    }

    @Override
    public void delete_Message(Channel channel, UUID messageID) {
        Channel channel1 = dataChannel.get(channel.getId());
        if (channel1 == null) {
            System.out.printf("\uD83D\uDEA8\uD83D\uDEA8 [%s]는 삭제된 채널임! - deleteMessage() 호출 불가 \n", channel.getChannelName()); // 🚨
        }
        else {
            channel1.delete_Message(messageID);
        }
    }

    //===============================
    //========== @Func ==========
    //===============================

    public UUID sendMessage(Channel channel, User user, String strMessage) {
        Channel channel1 = dataChannel.get(channel.getId());
        if (channel1 == null) {
            System.out.printf("\uD83D\uDEA8 [%s]는 삭제된 채널임! - [%s] 메세지 전송 불가! \n", channel.getChannelName(), strMessage); // 🚨

            return null;
        }
        else {
            JCF_MessageService jcf_message = JCF_MessageService.getInstance();
            Message message = jcf_message.createMessage(strMessage);
            message.setUserID( user.getId());
            channel1.sendMessage(user, message);

            return message.getId();
        }
    }

    public void setUser(Channel channel, User user) {
        Channel channel1 = dataChannel.get(channel.getId());
        if (channel1 == null) {
            System.out.printf("\uD83D\uDEA8 [%s]는 삭제된 채널임! - [%s] 입장 불가! \n", channel.getChannelName(), user.getUserName()); // 🚨
        }
        else {
            channel1.setUser(user);
        }
    }
}
