package com.sprint.mission.discodeit;


import com.sprint.mission.discodeit.DTO.ChannelDTO;
import com.sprint.mission.discodeit.DTO.UserDTO;
import com.sprint.mission.discodeit.entity.*;

import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageRoomService;
import com.sprint.mission.discodeit.service.jcf.*;
import com.sprint.mission.discodeit.vo.Invitation;
import com.sprint.mission.discodeit.vo.InvitationType;

import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.etc.PrintUtil.*;

public class JavaApplication {

    public static void main(String[] args) {
        //메서드 안에서는 접근자 세팅 불가능
        //어차피 {}안에서만 유효하니까.

        AppConfig appConfig = new AppConfig();
        JCFUserService userService = appConfig.getUserService();
        JCFMessageRoomService messageRoomService= appConfig.getMessageRoomService();
        JCFChannelService channelService = appConfig.getChannelService();


        //User생성 - 필수 필드 입력 안할 시 실패
        User user = new User();
        try {
            userService.save(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        printLine();

        //User 생성 -필수 필드 입력 시 성공!
        user.setPassword("11111111");
        user.setEmail("@gmail.com");
        user.setUsername("ian");
        user.setPhoneNumber("01011111111");
        user.setNickname("ianNickname");
        userService.save(user);

        //User 찾기
        User findUser = userService.findById(user.getId());
        System.out.println("같은 객체인가?"+ (findUser==user));
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
        System.out.println("같은 객체인가요? "+(findChannel==channel));
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
        System.out.println("같은 객체인가요? "+(findMessageRoom==messageRoom));
        printLine();

        //MessageRoom 삭제
        messageRoomService.remove(messageRoom);
        printLine();

        //User 모두 조회
        userService.save(user);
        User user2 = new User();
        user2.setUsername("Teddy");
        user2.setEmail("teddy@email.com");
        user2.setPhoneNumber("1");
        user2.setPassword("111111111");
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

        //MessageRoom 모두 조회
        messageRoomService.save(messageRoom);
        MessageRoom messageRoom2 = new MessageRoom();
        messageRoom2.setMessageRoomName("user와 user2의 DM");
        messageRoom2.setMessageRoomType(MessageRoomType.DM);
        messageRoomService.save(messageRoom2);
        System.out.println("[채팅방 목록]");
        messageRoomService.findAll().forEach(m-> System.out.println(m.getMessageRoomName()));
        printLine();


        //지금까지는 간단한 CRUD를 구현했음
        //---------------------------------------------------------------------------------
        //여기서부터는 도메인끼리 연결되는 함수

        //친구 추가
        Invitation invitation = userService.sendFriendRequest(user, user2.getId());
        List<Invitation> user2Invitations = user2.getMyInvitations();
        for (Invitation user2Invitation : user2Invitations) {
            if(user2Invitation.getType()== InvitationType.FRIEND_INVITATION){
                System.out.println("친구 요청을 보낸 사람: "+userService.findById(user2Invitation.getSenderId()).getUsername());
            }
        }
        userService.acceptFriendRequest(invitation);
        List<UUID> friends1 = user.getFriends();
        System.out.println("["+user.getUsername()+"의 친구목록]");
        for (UUID friend : friends1) {
            System.out.println("> "+userService.findById(friend).getUsername());
        }
        List<UUID> friends2 = user2.getFriends();
        System.out.println("["+user2.getUsername()+"의 친구목록]");
        for (UUID friend : friends2) {
            System.out.println("> "+userService.findById(friend).getUsername());
        }
        printLine();



        //user가 서버를 열고 user2 초대
        //1.서버 오픈
        Channel madeByUserServer = channelService.openChannel(user, "user가 만든 서버", 1L, true);
        //2. 초대장 보내고 받기
        Invitation invitation1 = channelService.sendInvitation(madeByUserServer, user2);
        channelService.acceptChannelRequest(user2, invitation1);

        System.out.println("<서버의 멤버 확인>");
        List<UUID> members = madeByUserServer.getMembers();
        for (UUID member : members) {
            User u = userService.findById(member);
            System.out.println(u.getNickname());
        }
        printLine();

        //서버 채팅방 만들고 채팅 보내기
        MessageRoom messageRoomInChannel = messageRoomService.makeChannelMessageRoom(madeByUserServer);
        userService.sendMessage(user, messageRoomInChannel, "이건 첫 채팅이야");
        userService.sendMessage(user2, messageRoomInChannel, "이건 답장이야");
        System.out.println("채팅 기록 확인");
        messageRoomInChannel.getHistory().forEach(m-> System.out.println("> "+m.getContent()));
    }
}
