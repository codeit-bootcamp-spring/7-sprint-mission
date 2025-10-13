package com.sprint.mission.discodeit.entity;

import java.util.*;

import static com.sprint.mission.discodeit.entity.ChannelType.MANY_USERS;
import static com.sprint.mission.discodeit.entity.ChannelType.ONE_VS_ONE;

public class Channel extends CommonModel {
    private ChannelType channelType;
    private String channelName;
    public Map<UUID, User> users;
    public final Map<UUID, Message> messages;

    public Channel(User ownerUser, String channelName) {
        super();
        channelType = MANY_USERS;
        this.channelName = channelName;
        users = new HashMap<>();
        messages = new HashMap<>();
//        this.ownerUser = ownerUser;
        ownerUser.setOwnChannelID(this.getId()); // 채널을 만든경우 소유한 채널 ID를 갖는다.
        setUser(ownerUser);
        getChannelType();
        System.out.println("\uD83D\uDCCC 1:1 채널로 변경 가능합니다!  \n"); // 📌
    }

    //===========================
    public ChannelType getChannelType() {
        System.out.printf("\uD83D\uDCCC %s 채널은 %s 방 입니다  \n", channelName, channelType.name()); // 📌
        return channelType;
    }

    public void setChannelType(ChannelType channelType) {
        this.channelType = channelType;
        System.out.printf("\uD83D\uDC8C %s 으로 채널 타입 변경 \n", channelType.name()); // 💌
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName, String channelName_Neo) {
        this.channelName = channelName_Neo;
        System.out.printf("\uD83D\uDC8C [%s] 에서 [%s] 으로 채널 이름 변경 \n", channelName, channelName_Neo); // 💌
    }

    public void sendMessage(User user, Message message) {
       if ( users.values().contains(user)) {
           messages.put(message.getId(), message);
           System.out.printf("\uD83D\uDC8C %s \uD83D\uDCAD [%s]_%s :: %s \n",  user.getUpdatedAt(), channelName, user.getUserName(), message.getMsg()); // 💌💭
       }
       else {
           System.out.printf("\uD83D\uDEA8 [%s] 님은 [%s] 채널방에 입장 후 메세지를 보낼 수 있어요! \n", user.getUserName(),  channelName); // 🚨
       }
    }

    public Map<UUID, User> getUsers() {
        return users;
    }

    public void getAllMessages() {
        System.out.printf("\uD83D\uDC8C [%s] 채널의 모든 메세지 출력 ======================================>> \n", channelName); // 💌
        this.messages.values()
                .stream()
                .sorted(Comparator.comparing(Message::getUpdatedAt))
                .forEach(message -> System.out.printf("\uD83D\uDC8C [%s]_%s : %s \n",
                        channelName,
                        users.get(message.getUserID()).getUserName(),
                        message.getMsg())); // 💌
        System.out.printf("\uD83D\uDC8C [%s] 채널의 모든 메세지 출력 =============== The End =================|| \n", channelName); // 💌
    }

    public Message get_Message(UUID messageID) {
        Message msg = messages.get(messageID);;
        if (msg == null) {
            System.out.println("\uD83D\uDEA8 없는 메세지"); // 🚨
        }
        else {
            System.out.println("\uD83D\uDC8C 찾는 메세지는 [%s]" + msg.getMsg()); // 💌
        }
        return msg;
    }

    public void update_Message(UUID messageID, String message) {
        Message message1 = messages.get(messageID);
        if (message1 == null) {
            System.out.println("\uD83D\uDEA8\uD83D\uDEA8 메세지 업데이트 오류"); // 🚨
        }
        else {
            String oldMessage = message1.getMsg();
            message1.setMsg(message);
            messages.put(messageID, message1);
            System.out.printf("\uD83D\uDEA8 메세지 수정 \uD83D\uDEA8 [%s]_[%s] : [%s] -> [%s] \uD83D\uDEA8\n",
                    this.channelName,
                    users.get(message1.getUserID()).getUserName(),
                    oldMessage,
                    message);  // 🚨
        }
    }

    public void delete_Message(UUID messageID) {
        Message message1 = messages.get(messageID);
        if (message1 == null) {
            System.out.println("\uD83D\uDEA8\uD83D\uDEA8 메세지 삭제 오류"); // 🚨
        }
        else {
            messages.remove(messageID);
            System.out.printf("\uD83D\uDEA8 메세지 삭제 \uD83D\uDEA8 [%s]_[%s] : [%s] \uD83D\uDEA8\n",
                    this.channelName,
                    users.get(message1.getUserID()).getUserName(),
                    message1.getMsg());  // 🚨
        }
    }

    public void setUser(User user) {
        if (channelType == ONE_VS_ONE && users.size() > 1) {
            System.out.println("\uD83D\uDEAB 1:1 채널방이라" + user.getUserName() + "입장 불가"); // 🚫
        }
        else {
            this.users.put(user.getId(), user);
            System.out.printf("\uD83E\uDD70 [%s] 채널에 [%s] 입장 \uD83E\uDD70\n", channelName, user.getUserName()); // 🥰
        }
    }

    public void removeUser(User user) {
        if (user.getOwnChannelID() != null) {
            System.out.printf("\uD83D\uDEA8 [%s] 님는 [%s] 채널 방장이야 너~!! 방장은 채널을 나갈 수 없어! 방장을 넘기던가! \n", user.getUserName(), channelName); // 🚨
        }
        else {
            this.users.remove(user.getUserName());
            System.out.printf("⭐ [%s] 채널서 [%s] 퇴장 \n", channelName, user.getUserName()); // ⭐
        }
    }

    @Override
    public String toString() {
        return "\uD83C\uDF31 Channel{" +
                super.toString() +
                "name = '" + channelName + '\'' +
                '}';
    }
}
