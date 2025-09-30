package com.sprint.mission.discodeit;


import com.sprint.mission.discodeit.entity.*;

import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageRoomService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.etc.PrintUtil.*;

public class JavaApplication {

    public static void main(String[] args) {
        //메서드 안에서는 접근자 세팅 불가능
        //어차피 {}안에서만 유효하니까.

        AppConfig appConfig = new AppConfig();
        JCFUserService userService = appConfig.userService();
        JCFMessageRoomService messageRoomService= appConfig.messageRoomService();
        JCFChannelService channelService = appConfig.channelService();

        //User생성 - 필수 필드 입력 안할 시 실패
        User user = new User();
        userService.addUser(user);
        printLine();

        //User 생성 -필수 필드 입력 시 성공!
        user.setPassword("11111111");
        user.setEmail("@gmail.com");
        user.setUsername("ian");
        user.setPhoneNumber("01011111111");
        userService.addUser(user);
        printLine();

        //User 찾기
        UUID id = user.getId();
        User findUser = userService.getUser(id);
        System.out.println("findUser = " + findUser.getUsername());
        printLine();


        //User 수정 - 실패
        UUID randomId = UUID.randomUUID();
        UserDto userDto = new UserDto();
        userService.updateUser(randomId, userDto);
        printLine();


        //User 수정 -성공
        userDto.setPhoneNumber("01022222222");
        userService.updateUser(id, userDto);
        System.out.println("user = " + user.getUsername());
        printLine();


        //User 삭제 - 유저가 저장되어 있을 경우
        userService.removeUser(user);
        printLine();

        //User삭제 - 유저가 없을 경우
        userService.removeUser(user);
        printLine();


        //Channel 생성 실패
        Channel channel = new Channel();
        channelService.addChannel(channel);
        printLine();


        //Channel 생성 성공

        channel.setServerName("테스트 서버1");
        channel.setServerLevel(1L);
        channelService.addChannel(channel);
        printLine();


        //Channel 찾기 - 실패
        //오류를 일으켰음
        try {
            Channel unsavedChannel = new Channel();
            channelService.getChannel(unsavedChannel.getId());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        printLine();


        //Channel 찾기 - 성공
        Channel findChannel = channelService.getChannel(channel.getId());
        System.out.println(findChannel==channel);
        printLine();




        //Channel 수정 - 실패

        //Channel 수정 - 성공
        //채널 수정을 위해서 수정된 양식을 전달한 엔티티를 만들었음
        UpdatedChannelDTO updatedChannelDTO = new UpdatedChannelDTO();
        updatedChannelDTO.setPrivate(true);
        updatedChannelDTO.setServerName("테스트 서버A");
        updatedChannelDTO.setServerLevel(2L);
        UUID channelId = channel.getId();
        channelService.updateChannel(channelId, updatedChannelDTO);
        printLine();


        //Channel 삭제
        channelService.removeChannel(channel);
        printLine();


        //MessageRoom 생성
        MessageRoom messageRoom = new MessageRoom();
        User user2 = new User();
        user2.setUsername("Teddy");

        List<User> participants = messageRoom.getParticipants();
        participants.add(user);
        participants.add(user2);
        messageRoom.setMessageRoomType(MessageRoomType.SERVER_MESSAGE_ROOM);
        messageRoom.setMessageRoomName("서버 안에 있는 채팅방");
        messageRoomService.addMessageRoom(messageRoom);
        printLine();


        //MessageRoom 찾기
        messageRoomService.getMessageRoom(messageRoom.getId());
        printLine();

        //MessageRoom 삭제
        messageRoomService.removeMessageRoom(messageRoom);
        printLine();

        //User 조회 - 모든 유저
        userService.addUser(user);
        user2.setEmail("teddy@email.com");
        user2.setPhoneNumber("1");
        user2.setPassword("111111111");
        userService.addUser(user2);
        System.out.println("[유저 목록]");
        userService.getAllUser().forEach(user1-> System.out.println(user1.toString()));
        printLine();


        //지금까지는 간단한 CRUD를 구현했음
        //---------------------------------------------------------------------------------
        //여기서부터는 도메인끼리 연결되는 함수

        //user와 user2의 채팅방
        MessageRoom dm = messageRoomService.findOrMakeDM(user, user2);
        userService.sendMessage(user, dm, "이건 첫번째 메세지입니다. dm을 지정하고 내용을 적어서 보내면 메세지가 생성됩니다.");
        userService.sendMessage(user2, dm, "이건 답장이야");
        dm.getHistory().forEach(m-> System.out.println("> "+m.getContent()));
        printLine();

        //user가 서버를 열고 user2 초대
        Channel madeByUserServer = channelService.openChannel(user, "user가 만든 서버", 1L, true);
        channelService.inviteMember(user, madeByUserServer, user2);
        System.out.println("<서버의 멤버 확인>");
        madeByUserServer.getMembers().forEach(u-> System.out.println(u.getUsername()));
        printLine();

        //친구 추가
        userService.sendFriendRequest(user,  user2);
        List<FriendRequest> receivedFriendRequests = user2.getReceivedFriendRequests();
        while(!receivedFriendRequests.isEmpty()){
            FriendRequest request = receivedFriendRequests.remove(0);
            if(request.getSender()==user){
                userService.acceptFriendRequest(request);
            }
        }
        printLine();


        //서버 채팅방 만들고 채팅 보내기
        MessageRoom messageRoomInServer = messageRoomService.makeServerMessageRoom(madeByUserServer, "서버 내의 채팅방");
        userService.sendMessage(user2,messageRoomInServer,"서버에 있는 채팅방에 채팅 남겨요");
        messageRoomInServer.getHistory().forEach(m-> System.out.println("> "+m.getContent()));


        //



    }
}
