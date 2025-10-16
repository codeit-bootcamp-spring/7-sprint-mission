package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.file.File_ChannelService;
import com.sprint.mission.discodeit.service.file.File_UserService;
import com.sprint.mission.discodeit.service.jcf.JCF_ChannelService;
import com.sprint.mission.discodeit.service.jcf.JCF_UserService;

import java.io.File;
import java.util.UUID;

import static com.sprint.mission.discodeit.entity.ChannelType.ONE_VS_ONE;

public class JavaApplication {

    public static void jcf_Test() {

        // User > User > User > User > User > User > User > User > User > User > User > User > User > User > User >
        // 1. 등록              : creatUser
        // 2. 조회(단건, 다건)    : getUser, getAllUsers
        // 3. 수정              : updateUser
        // 4. 수정된 데이터 조회
        // 5. 삭제              : deleteUser
        // 6. 조회를 통해 삭제되었는지 확인

        JCF_UserService jcf_User = JCF_UserService.getInstance();

        User user1 = jcf_User.creatUser("루시");
        User user2 = jcf_User.creatUser("내이름은 들장미 소녀 캔디");
        User user3 = jcf_User.creatUser("안소니");
        User user4 = jcf_User.creatUser("테리우스");

        jcf_User.updateUser("내이름은 들장미 소녀 캔디", "캔디");
        jcf_User.getUser("캔디");
        jcf_User.deleteUser("루시");
        jcf_User.getAllUsers();

        // Channel > Channel > Channel > Channel > Channel > Channel > Channel > Channel > Channel >
        // 1. 등록                  : createChannel
        // 2. 조회(단건, 다건)        : getChannel, getAllChannels
        // 3. 수정                  : updateChannelName, updateChannelType
        // 4. 수정된 데이터 조회
        // 5. 삭제                  : deleteChannel
        // 6. 조회를 통해 삭제되었는지 확인
        JCF_ChannelService jcf_channel = JCF_ChannelService.getInstance();
        Channel channel_I = jcf_channel.createChannel(user2, "캔디캔디");
        jcf_channel.sendMessage(channel_I, user2, "1. 빈 방이야");

        jcf_channel.setUser(channel_I, user3);
        jcf_channel.sendMessage(channel_I, user3, "2. hello~! nice to meet U~!!");
        jcf_channel.sendMessage(channel_I, user2, "3. hi~~!!!");
        UUID uuidMessage4 = jcf_channel.sendMessage(channel_I, user2, "4. hi~~!!!");

        jcf_channel.setUser(channel_I, user4);
        jcf_channel.sendMessage(channel_I, user4, "5. hello~! guys~!! how are you?");
        UUID messgeID_2 = jcf_channel.sendMessage(channel_I, user2, "6. hello new face! nice to meet you!!");

        jcf_channel.getAll_Messages(channel_I);

        Channel channel_II = jcf_channel.createChannel(user3, "세일러문");
        UUID messgeID_1 = jcf_channel.sendMessage(channel_II, user3, "세일러문 방장은 나다~~!!");
        User user5 = jcf_User.creatUser("생크림케익");
        User user6 = jcf_User.creatUser("겨울호빵");
        User user7 = jcf_User.creatUser("메뚜기");
        jcf_channel.setUser(channel_II,user5);
        jcf_channel.setUser(channel_II,user6);
        jcf_channel.sendMessage(channel_II, user5, "배고픈 시간이야. 생크림케익 먹구 싶따아~~! \uD83C\uDF82"); // 🎂
        jcf_channel.sendMessage(channel_II, user6, "나는 호빵이 먹구 시퍼엉~!! \uD83E\uDDC1"); // 🧁
        jcf_channel.sendMessage(channel_II, user7, "내가 누구게!! \uD83E\uDDC1");
        jcf_channel.setUser(channel_II,user7);
        jcf_channel.updateChannelType(channel_II, ONE_VS_ONE);
        jcf_channel.updateChannelName(channel_II, "달의 요정 세일러문");

        channel_II.removeUser(user3);

        jcf_channel.getChannel(channel_II);
        jcf_channel.getAllChannels();
        jcf_channel.deleteChannel(channel_II.getId());
        jcf_channel.getAllChannels();
        jcf_channel.sendMessage(channel_II, user7, "없는 방이 맞제??");

        User user8 = jcf_User.creatUser("user_test");
        jcf_channel.setUser(channel_II, user8);

        // Message> Message> Message> Message> Message> Message> Message> Message> Message> Message> Message>
        // 1. 등록                  : sendMessage
        // 2. 조회(단건, 다건)        : get_Message, get_RAllMessage
        // 3. 수정                  : update_Message
        // 4. 수정된 데이터 조회
        // 5. 삭제                  : delete_Message
        // 6. 조회를 통해 삭제되었는지 확인

        jcf_channel.getAll_Messages(channel_II);
        jcf_channel.delete_Message(channel_II, messgeID_1);
        jcf_channel.get_Message(channel_II, messgeID_1);
        jcf_channel.getAll_Messages(channel_II);
        jcf_channel.getAll_Messages(channel_I);
        jcf_channel.delete_Message(channel_I, messgeID_2);
        jcf_channel.getAll_Messages(channel_I);
        jcf_channel.update_Message(channel_I, uuidMessage4, "이정현의 바꿔 바꿔 바꿔~~!!");
        jcf_channel.getAll_Messages(channel_I);
    }
    public static void file_Test() {
        //=================================================================
        //==========🩷🩷🩷🩷🩷🩷🩷🩷 File  🩷🩷🩷🩷🩷🩷🩷🩷=============
        //=================================================================R
        // User > User > User > User > User > User > User > User > User > User > User > User > User > User > User >
        // 1. 등록              : creatUser
        // 2. 조회(단건, 다건)    : getUser, getAllUsers
        // 3. 수정              : updateUser
        // 4. 수정된 데이터 조회
        // 5. 삭제              : deleteUser
        // 6. 조회를 통해 삭제되었는지 확인
        System.out.println("//=====================================");
        System.out.println("//==========\uD83E\uDE77\uD83E\uDE77File \uD83E\uDE77\uD83E\uDE77=============");
        System.out.println("//=====================================");

        File_UserService file_User = File_UserService.getInstance();

        User file_User1 = file_User.creatUser("루시");
        User file_User2 = file_User.creatUser("내이름은 들장미 소녀 캔디");
        User file_User3 = file_User.creatUser("안소니");
        User file_User4 = file_User.creatUser("테리우스");

        file_User.updateUser("내이름은 들장미 소녀 캔디", "캔디");
        file_User.getUser("캔디");
        file_User.deleteUser("루시");
        file_User.getAllUsers();


        // Channel > Channel > Channel > Channel > Channel > Channel > Channel > Channel > Channel >
        // 1. 등록                  : createChannel
        // 2. 조회(단건, 다건)        : getChannel, getAllChannels
        // 3. 수정                  : updateChannelName, updateChannelType
        // 4. 수정된 데이터 조회
        // 5. 삭제                  : deleteChannel
        // 6. 조회를 통해 삭제되었는지 확인
        File_ChannelService file_channel = File_ChannelService.getInstance();
        Channel channel_III = file_channel.createChannel(file_User2, "캔디캔디");
        file_channel.sendMessage(channel_III, file_User2, "1. 빈 방이야");

        file_channel.setUser(channel_III, file_User3);
        file_channel.sendMessage(channel_III, file_User3, "2. hello~! nice to meet U~!!");
        file_channel.sendMessage(channel_III, file_User2, "3. hi~~!!!");
        UUID uuidMessage5 = file_channel.sendMessage(channel_III, file_User2, "4. hi~~!!!");

        file_channel.setUser(channel_III, file_User4);
        file_channel.sendMessage(channel_III, file_User4, "5. hello~! guys~!! how are you?");
        UUID messgeID_6 = file_channel.sendMessage(channel_III, file_User2, "6. hello new face! nice to meet you!!");

        file_channel.getAll_Messages(channel_III);
//
        Channel channel_IV = file_channel.createChannel(file_User3, "세일러문");
        UUID messgeID_11 = file_channel.sendMessage(channel_IV, file_User3, "세일러문 방장은 나다~~!!");
        User file_user5 = file_User.creatUser("생크림케익");
        User file_user6 = file_User.creatUser("겨울호빵");
        User file_user7 = file_User.creatUser("메뚜기");
        file_channel.setUser(channel_IV, file_user5);
        file_channel.setUser(channel_IV, file_user6);
        file_channel.sendMessage(channel_IV, file_user5, "배고픈 시간이야. 생크림케익 먹구 싶따아~~! \uD83C\uDF82"); // 🎂
        file_channel.sendMessage(channel_IV, file_user6, "나는 호빵이 먹구 시퍼엉~!! \uD83E\uDDC1"); // 🧁
        file_channel.sendMessage(channel_IV, file_user7, "내가 누구게!! \uD83E\uDDC1");
        file_channel.setUser(channel_IV, file_user7);
        file_channel.updateChannelType(channel_IV, ONE_VS_ONE);
        file_channel.updateChannelName(channel_IV, "달의 요정 세일러문");

        channel_IV.removeUser(file_User3);

        file_channel.getChannel(channel_IV);
        file_channel.getAllChannels();
        file_channel.deleteChannel(channel_IV.getId());
        file_channel.getAllChannels();
        file_channel.sendMessage(channel_IV, file_user7, "없는 방이 맞제??");

//        User file_user8 = file_User.creatUser("user_test");
//        file_channel.setUser(channel_IV, file_user8);

        // Message> Message> Message> Message> Message> Message> Message> Message> Message> Message> Message>
        // 1. 등록                  : sendMessage
        // 2. 조회(단건, 다건)        : get_Message, get_RAllMessage
        // 3. 수정                  : update_Message
        // 4. 수정된 데이터 조회
        // 5. 삭제                  : delete_Message
        // 6. 조회를 통해 삭제되었는지 확인

        file_channel.getAll_Messages(channel_IV);
        file_channel.delete_Message(channel_IV, messgeID_11);
        file_channel.get_Message(channel_IV, messgeID_11);
        file_channel.getAll_Messages(channel_IV);
        file_channel.getAll_Messages(channel_IV);
        file_channel.delete_Message(channel_IV, messgeID_6);
        file_channel.getAll_Messages(channel_IV);
        file_channel.update_Message(channel_IV, uuidMessage5, "이정현의 바꿔 바꿔 바꿔~~!!");
        file_channel.getAll_Messages(channel_IV);
    }
    public static void jcf_Repository_Test() {
    }
    public static void file_Repository_Test() {
    }

    public static void main(String[] args) {

        JavaApplication javaApplication = new JavaApplication();

//        jcf_Test();
        file_Test();
//        jcf_Repository_Test();
//        file_Repository_Test();
    }
}