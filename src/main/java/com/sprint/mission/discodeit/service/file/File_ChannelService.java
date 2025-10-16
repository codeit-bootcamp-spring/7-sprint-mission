package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.File_Common;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.jcf.JCF_MessageService;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.sprint.mission.discodeit.entity.ChannelType.ONE_VS_ONE;

public class File_ChannelService implements ChannelService, MessageService {
    private static final String FILE_PATH = File_Common.ROOT_PATH + "/File_ChannelService.txt";
    private static File file_ChannelService = new File(FILE_PATH);
    private static List<Channel> channelList = new ArrayList<>();

    private static File_ChannelService service = new File_ChannelService();
    private File_ChannelService() {
        File_Common.fileCreate(file_ChannelService, File_Common.ROOT_PATH);
    }
    static public File_ChannelService getInstance() { return service; }


    //===============================
    //========== @Override ==========
    //===============================
    @Override
    public Channel createChannel(User ownerUser, String channelName) {
        String message = "\n\uD83C\uDF7F\uD83C\uDF7F [" + ownerUser.getUserName() + "]가 [" + channelName + "] 채널 생성 \uD83C\uDF7F\uD83C\uDF7F\n";
        File_Common.okMessage(message);

        Channel channel = new Channel(ownerUser, channelName);
        channelList.add(channel);
        File_Common.fileWrite(channelList, FILE_PATH, "");

        return channel;
    }

    @Override
    public Channel getChannel(Channel in_Channel) {
        String message = "getChannel: [" + in_Channel.getChannelName() + "]";

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH));) {

            Boolean isExist = false;
            List<Channel> channels = (List<Channel>)ois.readObject();

            for (Channel channel : channels) {
                if (channel.getChannelName().equals(in_Channel.getChannelName())) {

                    File_Common.okMessage(message);
                    return channel;
                }
            }

            File_Common.errMessage(message);
            return null;
        } catch (Exception e) {
            File_Common.errMessage(message);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Channel[] getAllChannels() {
        String message = "getAllChannels_";

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH));) {

            Boolean isExist = false;
            List<Channel> channels = (List<Channel>)ois.readObject();

            for (Channel channel : channels) {
                File_Common.okMessage(message + "[" + channel.getChannelName() + "]");
            }

            return null; // 안써!
        } catch (Exception e) {
            File_Common.errMessage(message);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateChannelName(Channel in_Channel, String reName) {
        String message = "updateChannelName: [" + in_Channel.getChannelName() + "]에서 -> [" + reName + "]으로 변경";

        for (Channel channel : channelList) {
            if (channel.getChannelName().equals(reName)) {
                int index = channelList.indexOf(channel);
                channel.setChannelName(channel.getChannelName(), reName);
                channelList.set(index, channel);

                File_Common.fileWrite(channelList, FILE_PATH, message);

                return;
            }
        }

        File_Common.errMessage(message);
    }

    @Override
    public void updateChannelType(Channel channel, ChannelType channelType) {
        if ( channelType == ONE_VS_ONE && channel.users.size() > 1 ) {
            String message = "인원수 초과로(총 " + channel.getUsers().size() + "명) [" + channel.getChannelName() + "] 채널은 1:1 채널방으로 변경 불가";
            File_Common.errMessage(message);
        }
        else {
            channelList.get(channelList.indexOf(channel)).setChannelType(channelType);

            String message = "\uD83D\uDEAB [" + channel.getChannelName() + "] 채널은 ["+ channelType + "] 타입 채널방으로 변경됨\n";
            File_Common.fileWrite(channelList, FILE_PATH, message);
        }
    }

    @Override
    public void deleteChannel(UUID uuid) { // 🖤🖤🖤🖤🖤🖤🖤🖤 아이디로 객체 찾기!!
        Optional<Channel> channel1 = channelList.stream().filter(channel -> channel.getId().equals(uuid)).findFirst();
        List<Channel> channels = channelList.stream().filter(channel -> !channel.getId().equals(uuid)).collect(Collectors.toList());

        String message = "[" + channel1.get().getChannelName() + "] 채널 삭제";
        File_Common.fileWrite(channels, FILE_PATH, message);

//        this.channelList = new ArrayList<>();
//        this.channelList.addAll(channels);

//        String message = "[" + channel1.get().getChannelName() + "] 채널 삭제";
//        System.out.println("//////");
//
//        Optional<Channel> channel1 = channelList.stream().filter(channel -> channel.getId().equals(uuid)).findFirst(); //
//
//        String message = "[" + channel1.get().getChannelName() + "] 채널 삭제";
//        System.out.println("//////" + message);
//        channelList.remove(channel1.get());
////        System.out.println("///////////" + message);
//

//        channel1.ifPresent(channel -> channelList.remove(channel));// .ifPresent();

//        if (channel1.isPresent()) {
//            String message = "[" + channel1.get().getChannelName() + "] 채널 삭제";
//            Channel bingoChannel = channel1.get();
//            int index_I = channelList.indexOf(bingoChannel);
//            System.out.println("===== " + String.valueOf(index_I) + "===" + channel1.get().getChannelName());
////            channelList.remove(index_I);
////            File_Common.errMessage("!!!! removed" + String.valueOf(removed));
////            File_Common.errMessage("!!!!removed removed removed" + String.valueOf(index));
////            if () {
////                File_Common.fileWrite(channelList, FILE_PATH, message);
////            }
////            else {
////                File_Common.errMessage(message);
////            }
//        }
//        else {
//            File_Common.errMessage("uuid = " + uuid.toString() + "채널 삭제 오류");
//        }
    }

    @Override
    public void getAll_Messages(Channel channel) {
        File_Common.errMessage("❌❌❌ getAll_Messages 이런걸 언제 구현?! PASS! ❌❌❌");
    }

    @Override
    public Message get_Message(Channel channel, UUID messageID) {
        File_Common.errMessage("❌❌❌ get_Message 이런걸 언제 구현?! PASS! ❌❌❌");
        return null;
    }

    @Override
    public void update_Message(Channel channel, UUID messageID, String message) {
        File_Common.errMessage("❌❌❌ update_Message 이런걸 언제 구현?! PASS! ❌❌❌");
    }

    @Override
    public void delete_Message(Channel channel, UUID messageID) {
        File_Common.errMessage("❌❌❌ delete_Message 이런걸 언제 구현?! PASS! ❌❌❌");
    }

    //===============================
    //========== @Func ==========
    //===============================

    public UUID sendMessage(Channel in_Channel, User user, String strMessage) {
        int index = channelList.indexOf(in_Channel);
        Channel channel = channelList.get(index);

        if (channel == null) {
            File_Common.errMessage(in_Channel.getChannelName() + "는 삭제된 채널임! - [" + strMessage  + "] 메세지 전송 불가! \n");
            return null;
        }
        else {
            JCF_MessageService jcf_message = JCF_MessageService.getInstance();
            Message message = jcf_message.createMessage(strMessage);
            message.setUserID( user.getId());
            channel.sendMessage(user, message);

            return message.getId();
        }
    }

    public void setUser(Channel in_Channel, User user) {
        int index = channelList.indexOf(in_Channel);
        Channel channel = channelList.get(index);

        if (channel == null) {
            File_Common.errMessage(in_Channel.getChannelName() + "는 삭제된 채널임! - [" + user.getUserName()  + "] 입장 불가!! \n");
        }
        else {
            channel.setUser(user);
        }
    }
}
