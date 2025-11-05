package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.common.Util;
import com.sprint.mission.discodeit.entity.dto.*;
import com.sprint.mission.discodeit.service.basic.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class DiscodeitApplication {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

//        test_UserService(context);
//        test_UserStatusService(context);
//        test_ChannelService(context);
//        test_MessageService(context);
//        test_ReadStatusService(context);
////        test_BinaryContentService(context);
//        test_AuthService(context);
//
//        //🚨 테스트 후 폴더 삭제!!
//        new FileUtil(USER).cleanup();
    }

    static void test_UserService(ConfigurableApplicationContext context) {
        Util.okMessage("🔴🔴🔴🔴🔴 test_UserService  🔴🔴🔴🔴🔴");
        try {
            UserService userService = context.getBean(UserService.class);
            UserStatusService userStatusService = context.getBean(UserStatusService.class);
            ChannelService channelService = context.getBean(ChannelService.class);
            MessageService messageService = context.getBean(MessageService.class);
            ReadStatusService readStatusService = context.getBean(ReadStatusService.class);
            BinaryContentService binaryContentService = context.getBean(BinaryContentService.class);
            AuthService authService = context.getBean(AuthService.class);

//            Path path = Paths.get(System.getProperty("user.dir"), "/src/main/resources").resolve("gyul.png");

//                File imageFile = new File("/Users/my05030/Desktop/장미연/7-sprint-mission/png/gyul.png");
//                BufferedImage image = ImageIO.read(imageFile);
            Dto_User dtoUser_2 = Dto_User.from("🍊gyul", "1234", "gyul@email.com");
            Res_User user2 = userService.create(dtoUser_2, Optional.empty());

//                File imageFile3 = new File("/Users/my05030/Desktop/장미연/7-sprint-mission/png/tiger.png");
//                BufferedImage image3 = ImageIO.read(imageFile3);
            Dto_User dtoUser_3 = Dto_User.from("🐯호랭이", "1234", "호랭이@email.com");
            Res_User user3 = userService.create(dtoUser_3, Optional.empty());

            userService.update(user3.id()
                    , Dto_User.from("🐯호랭이는 어흥", "호랭123", "어흥이@email.com")
                    , null);
            userService.findAll();
//            Util.okMessage("♣️user3.readStatusID() = [" + user3.readStatusID() + "]");
            userService.find(user3.id());
            userService.delete(user2.id());
            userService.findAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static void test_UserStatusService(ConfigurableApplicationContext context) {
        Util.okMessage("🔴🔴🔴🔴🔴 test_UserStatusService 🟠🟠🟠🟠🟠");
        try {
            UserService userService = context.getBean(UserService.class);
            UserStatusService userStatusService = context.getBean(UserStatusService.class);
            ChannelService channelService = context.getBean(ChannelService.class);
            MessageService messageService = context.getBean(MessageService.class);
            ReadStatusService readStatusService = context.getBean(ReadStatusService.class);
            BinaryContentService binaryContentService = context.getBean(BinaryContentService.class);
            AuthService authService = context.getBean(AuthService.class);

            Dto_User dtoUser_4 = Dto_User.from("🦊여우", "1234", "여우@email.com");
            Res_User user4 = userService.create(dtoUser_4, Optional.empty());

            Dto_User dtoUser_5 = Dto_User.from("🐼팬더", "1234", "팬더@email.com");
            Res_User user5 = userService.create(dtoUser_5, Optional.empty());

            Res_UserStatus userStatus_Creat = userStatusService.create(user4.id());
            Res_UserStatus userStatus_Find = userStatusService.find(userStatus_Creat.id());
            userStatusService.findAll();
            Dto_UserStatus dto_UserStatus4 = Dto_UserStatus.from(userStatus_Creat.id());
            userStatusService.update(dto_UserStatus4);

            Res_UserStatus userStatus_Creat5 = userStatusService.create(user5.id());
            Util.okMessage("♣️ user5.readStatusID()= " + user5.userName());
            userStatusService.delete(userStatus_Creat5.id());
            userStatusService.findAll();

            userService.findAll();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static void test_ChannelService(ConfigurableApplicationContext context) {
        Util.okMessage("🔴🔴🔴🔴🔴 test_ChannelService  🟡️🟡️🟡️🟡️🟡️️");
        try {
            UserService userService = context.getBean(UserService.class);
            UserStatusService userStatusService = context.getBean(UserStatusService.class);
            ChannelService channelService = context.getBean(ChannelService.class);
            MessageService messageService = context.getBean(MessageService.class);
            ReadStatusService readStatusService = context.getBean(ReadStatusService.class);
            BinaryContentService binaryContentService = context.getBean(BinaryContentService.class);
            AuthService authService = context.getBean(AuthService.class);

            Dto_User dtoUser_6 = Dto_User.from("🐶바둑이", "1234", "바둑이@email.com");
            Res_User user6 = userService.create(dtoUser_6, Optional.empty());

            Dto_User dtoUser_7 = Dto_User.from("🐽꿀꿀", "1234", "꿀꿀@email.com");
            Res_User user7 = userService.create(dtoUser_7, Optional.empty());

            Res_Channel resPublic = channelService.createPublic(Dto_CreateChannelPublic.from("🔰동물농장", "동물농장 채널이양 🐾"));
            Res_Channel resPrivate = channelService.createPrivate(Dto_CreateChannelPrivate.from(Arrays.asList(user6.id(), user7.id())));

            Dto_Channel channelPublic = Dto_Channel.fromPublic(resPublic);
            Res_ChannelFind resChannelFind = channelService.find(channelPublic);
            Dto_Channel channelPrivate = Dto_Channel.fromPrivate(resPrivate);
            Res_ChannelFind resChannelFind_II = channelService.find(channelPrivate);

            Util.okMessage("================");
            List<Res_ChannelFind> allByUserId = channelService.findAllByUserId(user6.id());

            channelService.update(Dto_ChannelUpdate.from(resPublic.id(), null, "퍼블릭 채널이야", "desc- 퍼블릭 채널이야"));
            channelService.delete(resPublic.id());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static void test_MessageService(ConfigurableApplicationContext context) {
        Util.okMessage("🔴🔴🔴🔴🔴 test_MessageService  🟢🟢🟢🟢🟢️");
        try {
            UserService userService = context.getBean(UserService.class);
            UserStatusService userStatusService = context.getBean(UserStatusService.class);
            ChannelService channelService = context.getBean(ChannelService.class);
            MessageService messageService = context.getBean(MessageService.class);
            ReadStatusService readStatusService = context.getBean(ReadStatusService.class);
            BinaryContentService binaryContentService = context.getBean(BinaryContentService.class);
            AuthService authService = context.getBean(AuthService.class);

            Dto_User dtoUser_8 = Dto_User.from("🐸개굴", "1234", "개굴@email.com");
            Res_User user8 = userService.create(dtoUser_8, Optional.empty());

            Res_Channel resPublic = channelService.createPublic(Dto_CreateChannelPublic.from("🔰메세지 테스트 채널", "메세지 테스트 채널 이야!!"));

            Dto_Message dtoMessage = Dto_Message.from(resPublic.id(), user8.id(), "메세지 테스트 하장");
            Res_Message resMessage = messageService.create(dtoMessage, Optional.empty());

            Dto_Message dtoMessage_II = Dto_Message.from(resPublic.id(), user8.id(), "2nd 메세지 테스트 하장");
            Res_Message resMessage_II = messageService.create(dtoMessage_II, Optional.empty());

            messageService.find(resMessage.id());
            messageService.updateMessage(Dto_MessageUpdate.from(resMessage.id(), "1st 메세지 테스트로 변경"));
            messageService.findAllByChannleId(resPublic.id());
            messageService.deleteMessage(resMessage_II.id());
            messageService.findAllByChannleId(resPublic.id());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static void test_ReadStatusService(ConfigurableApplicationContext context) {
        Util.okMessage("🔴🔴🔴🔴🔴 test_ReadStatusService  🔵🔵🔵🔵🔵");
        try {
            UserService userService = context.getBean(UserService.class);
            UserStatusService userStatusService = context.getBean(UserStatusService.class);
            ChannelService channelService = context.getBean(ChannelService.class);
            MessageService messageService = context.getBean(MessageService.class);
            ReadStatusService readStatusService = context.getBean(ReadStatusService.class);
            BinaryContentService binaryContentService = context.getBean(BinaryContentService.class);
            AuthService authService = context.getBean(AuthService.class);

            Dto_User dtoUser_9 = Dto_User.from("🦄유니콘", "1234", "유니콘@email.com");
            Res_User user9 = userService.create(dtoUser_9, Optional.empty());

            Res_ReadStatus resReadStatus = readStatusService.create(Dto_ReadStatus.from(user9.id(), channelService.findAllByUserId(user9.id()).get(0).id()));
            Res_ReadStatus resReadStatus1 = readStatusService.find(resReadStatus.id());
            readStatusService.update(Dto_ReadStatusUpdate.from(resReadStatus1.id()));
            readStatusService.findAllByUserId(user9.id());
            readStatusService.delete(resReadStatus1.id());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static void test_BinaryContentService(ConfigurableApplicationContext context) {
        Util.okMessage("🔴🔴🔴🔴🔴 test_BinaryContentService  🟣🟣🟣🟣🟣");
        try {
            UserService userService = context.getBean(UserService.class);
            UserStatusService userStatusService = context.getBean(UserStatusService.class);
            ChannelService channelService = context.getBean(ChannelService.class);
            MessageService messageService = context.getBean(MessageService.class);
            ReadStatusService readStatusService = context.getBean(ReadStatusService.class);
            BinaryContentService binaryContentService = context.getBean(BinaryContentService.class);
            AuthService authService = context.getBean(AuthService.class);


            Dto_User dtoUser_1 = Dto_User.from("🐶바둑이", "1234", "바둑이@email.com");
            Res_User user1 = userService.create(dtoUser_1, Optional.empty());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static void test_AuthService(ConfigurableApplicationContext context) {
        Util.okMessage("🔴🔴🔴🔴🔴 test_AuthService  🟤🟤🟤🟤🟤");
        try {
            UserService userService = context.getBean(UserService.class);
            UserStatusService userStatusService = context.getBean(UserStatusService.class);
            ChannelService channelService = context.getBean(ChannelService.class);
            MessageService messageService = context.getBean(MessageService.class);
            ReadStatusService readStatusService = context.getBean(ReadStatusService.class);
            BinaryContentService binaryContentService = context.getBean(BinaryContentService.class);
            AuthService authService = context.getBean(AuthService.class);

            authService.isLogin(Dto_AuthService.from("🦊여우", "1234"));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
