package com.sprint.mission.discodeit;


import com.sprint.mission.discodeit.DTO.ChannelDTO;
import com.sprint.mission.discodeit.DTO.UserDTO;
import com.sprint.mission.discodeit.application.ConsoleApplication;
import com.sprint.mission.discodeit.entity.*;

import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageRoomService;
import com.sprint.mission.discodeit.service.UserService;


import java.util.UUID;

import static com.sprint.mission.discodeit.etc.PrintUtil.*;

public class JavaApplication {

    public static void main(String[] args) {
        //메서드 안에서는 접근자 세팅 불가능
        //어차피 {}안에서만 유효하니까.

        AppConfig appConfig = new AppConfig();

        UserService userService = appConfig.getUserService();
        MessageRoomService messageRoomService= appConfig.getMessageRoomService();
        ChannelService channelService = appConfig.getChannelService();

        ConsoleApplication consoleApplication = appConfig.getConsoleApplication();


        //User생성 - 필수 필드 입력 안할 시 실패
        User user = new User();
        try {
            userService.save(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        printLine();

        //User 생성 -필수 필드 입력 시 성공!
        user.setPassword("1111");
        user.setEmail("@");
        user.setUsername("ian");
        user.setPhoneNumber("01011111111");
        user.setNickname("ianNickname");
        userService.save(user);

        //User 찾기
        User findUser = userService.findById(user.getId());
        System.out.println("같은 객체인가?"+ (findUser.equals(user)));
        printLine();


        //User 수정 -성공
        UserDTO userDTO = new UserDTO();
        userDTO.setPhoneNumber("01022222222");
        userService.update(user.getId(), userDTO);
        System.out.println("user = " + user.getPhoneNumber());
        printLine();


        //User 삭제 - 유저가 저장되어 있을 경우
        userService.remove(user);
        printLine();

        //User삭제 - 유저가 없을 경우
        try {
            userService.remove(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        printLine();


        //Channel 생성 실패
        Channel channel = new Channel();
        try {
            channelService.save(channel);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        printLine();


        //Channel 생성 성공

        channel.setServerName("테스트 서버1");
        channel.setServerLevel(1L);
        channelService.save(channel);
        printLine();


        //Channel 찾기 - 실패
        try {
            Channel unsavedChannel = new Channel();
            channelService.findById(unsavedChannel.getId());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        printLine();


        //Channel 찾기 - 성공
        Channel findChannel = channelService.findById(channel.getId());
        System.out.println("같은 객체인가요? "+(findChannel.equals(channel)));
        printLine();


        //Channel 수정 - 성공
        //채널 수정을 위해서 수정된 양식을 전달한 엔티티를 만들었음
        ChannelDTO channelDTO = new ChannelDTO();
        channelDTO.setPrivate(true);
        channelDTO.setServerName("테스트 서버A");
        channelDTO.setServerLevel(2L);
        UUID channelId = channel.getId();
        channelService.update(channelId, channelDTO);
        printLine();


        //Channel 삭제
        channelService.remove(channel);
        printLine();


        //MessageRoom 생성
        MessageRoom messageRoom = new MessageRoom();
        messageRoom.setMessageRoomType(MessageRoomType.SERVER_MESSAGE_ROOM);
        messageRoom.setMessageRoomName("서버 안에 있는 채팅방");
        messageRoomService.save(messageRoom);
        printLine();


        //MessageRoom 찾기 - 실패
        try {
            MessageRoom messageRoom1 = new MessageRoom();
            messageRoomService.findById(messageRoom1.getId());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        printLine();

        //MessageRoom 찾기 -성공
        MessageRoom findMessageRoom = messageRoomService.findById(messageRoom.getId());
        System.out.println("같은 객체인가요? "+(findMessageRoom.equals(messageRoom)));
        printLine();

        //MessageRoom 삭제
        messageRoomService.remove(messageRoom);
        printLine();

        //User 모두 조회
        userService.save(user);
        User user2 = new User();
        user2.setUsername("Teddy");
        user2.setEmail("@2");
        user2.setPhoneNumber("1");
        user2.setPassword("1111");
        user2.setNickname("TeddyNickname");
        userService.save(user2);
        System.out.println("[유저 목록]");
        userService.findAll().forEach(user1-> System.out.println(user1.getUsername()));
        printLine();

        //Channel 모두 조회
        channelService.save(channel);
        Channel channel2 = new Channel();
        channel2.setPrivate(false);
        channel2.setServerLevel(3L);
        channel2.setServerName("테스트 서버B");
        channelService.save(channel2);
        System.out.println("[채널 목록]");
        channelService.findAll().forEach(c-> System.out.println(c.getServerName()));
        printLine();

        channel.addMember(user.getId());
        user.addMyChannel(channel.getId());
        user2.addMyChannel(channel.getId());
        channel.addMember(user2.getId());



        //MessageRoom 모두 조회
//        messageRoomService.save(messageRoom);
//        MessageRoom messageRoom2 = new MessageRoom();
//        messageRoom2.setMessageRoomName("테스트 서버A의 채팅방");
//        messageRoom2.setMessageRoomType(MessageRoomType.SERVER_MESSAGE_ROOM);
//        messageRoomService.save(messageRoom2);
//        System.out.println("[채팅방 목록]");
//        messageRoomService.findAll().forEach(m-> System.out.println(m.getMessageRoomName()));
//        printLine();

//        channel.addMessageRoom(messageRoom2.getId());

        //지금까지는 간단한 CRUD를 구현했음
        //---------------------------------------------------------------------------------
        //여기서부터는 콘솔 기반 어플리케이션
        userService.findAll().forEach(user1 -> System.out.println(user1.getEmail()));
        System.out.println();
        System.out.println();


        /*
        * email: @
        * 비밀번호: 1111
        * 로그인 해서 여러가지 기능을 콘솔로 할 수 있게 구현
        *
        * 다른 유저
        * 이메일: @2
        * 비밀번호: 1111
        *
        * 콘솔을 통해
        * 회원가입, 로그인, 서버 확인, 친구 확인, 서버 내 채팅방 확인
        * 서버 만들기, 채팅창 만들기, 채팅방에 채팅 보내기
        * 친구 추가, 친구 추가 요청 확인, 친구 신청 받기
        * 을 구현했습니다.
        *
         * */
        consoleApplication.start();
    }
}
