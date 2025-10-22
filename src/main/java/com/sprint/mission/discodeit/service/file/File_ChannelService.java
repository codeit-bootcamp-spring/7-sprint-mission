package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.File_ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.File_Common;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.jcf.JCF_MessageService;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.entity.ChannelType.ONE_VS_ONE;

public class File_ChannelService implements ChannelService, MessageService {
    public static final String FILE_PATH = File_Common.ROOT_PATH + "/File_ChannelService.ser";
    public File file_ChannelService = new File(FILE_PATH);
    private File_ChannelService() {
        File_Common.fileCreate(file_ChannelService, File_Common.ROOT_PATH);
    }

    private static File_ChannelService service = new File_ChannelService();
    static public File_ChannelService getInstance() { return service; }

    private List<Channel> channelList = new ArrayList<>();
    private File_ChannelRepository channelRepository = new File_ChannelRepository();

    //===============================
    //========== @Override ==========
    //===============================
    @Override
    public Channel createChannel(User user, String channelName) {
        String message = "\uD83C\uDF7F\uD83C\uDF7F [" + user.getUserName() + "]가 [" + channelName + "] 채널 생성 \uD83C\uDF7F\uD83C\uDF7F";
        File_Common.okMessage(message);

        Channel channel = new Channel(user, channelName);
        channelList.add(channel);
        // File_Common.fileWrite(channel, FILE_PATH, "");
        channelRepository.channelWrite(channelList, "");

        return channel;
    }

    @Override
    public void getChannel(Channel in_Channel) {
        String message = "getChannel: [" + in_Channel.getChannelName() + "]";

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH));) {

//            Boolean isExist = false;
            List<Channel> channels = (List<Channel>)ois.readObject();

            for (Channel channel : channels) {
                if (channel.getChannelName().equals(in_Channel.getChannelName())) {
//                    isExist = true;
                    File_Common.okMessage(message);
                    return;
                }
            }

            File_Common.errMessage(message);
            return;
        } catch (Exception e) {
            File_Common.errMessage(message);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void getAllChannels() {
        for (Channel channel : channelList) {
            String message = "getAllChannels: " + channel.getChannelName();
            File_Common.okMessage(message);
        }
    }

//    @Override
//    public void updateChannelName(Channel in_Channel, String reName) {
//        String message = "updateChannelName: [" + in_Channel.getChannelName() + "]에서 -> [" + reName + "]으로 변경";
//
//        for (Channel channel : channelList) {
//            if (channel.getChannelName().equals(reName)) {
//                int index = channelList.indexOf(channel);
//                channel.setChannelName(channel.getChannelName(), reName);
//                channelList.set(index, channel);
//
////                File_Common.fileWrite(channelList, FILE_PATH, message);
//                channelRepository.channelWrite(channelList, message);
//                return;
//            }
//        }
//
//        System.out.println("\uD83D\uDC9A\uD83D\uDC9A\uD83D\uDC9A");
//        File_Common.errMessage(message);
//    }
    @Override
    public void updateChannelName(Channel in_Channel, String reName) {
        String message = "updateChannelName: [" + in_Channel.getChannelName() + "]에서 -> [" + reName + "]으로 변경";

        for (Channel channel : channelList) {
            if (channel.getChannelName().equals(in_Channel.getChannelName())) {
                int index = channelList.indexOf(channel);
                channel.setChannelName(channel.getChannelName(), reName);
                channelList.set(index, channel);

    //                File_Common.fileWrite(channelList, FILE_PATH, message);
                channelRepository.channelWrite(channelList, message);
                return;
            }
        }

//        System.out.println("\uD83D\uDC9A\uD83D\uDC9A\uD83D\uDC9A");
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
//            File_Common.fileWrite(channelList, FILE_PATH, message);
            channelRepository.channelWrite(channelList, message);
        }
    }

//    @Override
//    public Message createMessage(String msg) {
//        return null; // 안써!
//    }

    @Override
    public void deleteChannel(UUID uuid) { // 🖤🖤🖤🖤🖤🖤🖤🖤 아이디로 객체 찾기!!
        Channel findChannel = channelList.stream().filter(channel -> channel.getId().equals(uuid)).findFirst().get();
        String message = "[" + findChannel.getChannelName() + "] 채널 삭제";
        channelList.remove(findChannel);
//        File_Common.fileWrite(channelList, FILE_PATH, message);
        channelRepository.channelWrite(channelList, message);
    }

    @Override
    public void getAll_Messages(Channel in_Channel) {
        int index = channelList.indexOf(in_Channel);
        if (index == -1) {
            String message = "\uD83D\uDEA8\uD83D\uDEA8 [" + in_Channel.getChannelName() + "]는 삭제된 채널임! - getAllMessages() 호출 불가";
            File_Common.errMessage(message);
        }
        else {
            Channel channel = channelList.get(index);
            channel.getAllMessages();
        }
    }

    @Override
    public void get_Message(Channel in_Channel, UUID messageID) {
        int index = channelList.indexOf(in_Channel);
        if (index == -1) {
            String message = "\uD83D\uDEA8\uD83D\uDEA8 [" + in_Channel.getChannelName() + "]는 삭제된 채널임! - get_Message() 호출 불가";
            File_Common.errMessage(message);
        }
        else {
            Channel channel = channelList.get(index);
            channel.get_Message(messageID);
        }
    }

    @Override
    public void update_Message(Channel in_Channel, UUID messageID, String message) {
        int index = channelList.indexOf(in_Channel);
        if (index == -1) {
            String errMessage = "\uD83D\uDEA8\uD83D\uDEA8 [" + in_Channel.getChannelName() + "]는 삭제된 채널임! - update_Message() 호출 불가";
            File_Common.errMessage(errMessage);
        }
        else {
            Channel channel = channelList.get(index);
            channel.update_Message(messageID, message);
        }
    }

    @Override
    public void delete_Message(Channel in_Channel, UUID messageID) {
        int index = channelList.indexOf(in_Channel);
        if (index == -1) {
            String message = "\uD83D\uDEA8\uD83D\uDEA8 [" + in_Channel.getChannelName() + "]는 삭제된 채널임! - delete_Message() 호출 불가";
            File_Common.errMessage(message);
        }
        else {
            Channel channel = channelList.get(index);
            channel.delete_Message(messageID);
        }
    }

    //===============================
    //========== @Func ==========
    //===============================

    public UUID sendMessage(Channel in_Channel, User user, String strMessage) {
        int index = channelList.indexOf(in_Channel);
        if (index == -1) {
            String message = "\uD83D\uDEA8\uD83D\uDEA8 [" + in_Channel.getChannelName() + "]는 삭제된 채널임! - sendMessage() 호출 불가";
            File_Common.errMessage(message);

            return null;
        }
        else {
            JCF_MessageService jcf_message = JCF_MessageService.getInstance();
            Message message = jcf_message.neoMessage(strMessage);
            message.setUserID(user.getId());
            channelList.get(index).sendMessage(user, message);

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
