package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.friendrequest.domain.FriendRequest;
import com.sprint.mission.discodeit.messageroom.presentation.MessageRoomService;
import com.sprint.mission.discodeit.messageroom.domain.MessageRoom;
import com.sprint.mission.discodeit.messageroom.domain.MessageRoomType;
import com.sprint.mission.discodeit.server.presentation.ServerService;
import com.sprint.mission.discodeit.server.domain.Server;
import com.sprint.mission.discodeit.user.presentation.UserService;
import com.sprint.mission.discodeit.user.domain.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class DiscodeitApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscodeitApplication.class, args);
        //메서드 안에서는 접근자 세팅 불가능
        //어차피 {}안에서만 유효하니까.

        DiContainer diContainer = new DiContainer();

//        UserService userService = diContainer.getUserService();
//        MessageRoomService messageRoomService= diContainer.getMessageRoomService();
//        ServerService serverService = diContainer.getServerService();



        //User생성 - 필수 필드 입력 안할 시 실패

//        try {
//            User.create(""," "," ","");
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//        printLine();
//
//        //User 생성 -필수 필드 입력 시 성공!
//        User user = User.create("test@email.com", "abcd1234", "Ian", "010-1234-5678");
//        userService.register(user);
//        printLine();
//
//
//        //User 찾기
//        User findUser = userService.findById(user.getId());
//        System.out.println("같은 객체인가?"+ (findUser.equals(user)));
//        printLine();
//
//
//
//
//        //User 삭제 - 유저가 저장되어 있을 경우
//        userService.remove(user);
//        printLine();
//
//        //User삭제 - 유저가 없을 경우
//        try {
//            userService.remove(user);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//        printLine();
//
//
//        //서버 생성 실패
//
//        try {
//            Server server = Server.create("");
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//        printLine();
//
//
//
//        //서버 생성 & 저장 성공
//        Server server = Server.create("테스트 서버1");
//        serverService.save(server);
//        printLine();
//
//
//
//
//
//        //서버 찾기 - 성공
//        Server foundServer = serverService.findById(server.getId());
//        System.out.println("같은 객체인가요? "+(server.equals(foundServer)));
//        printLine();
//
//
//
//        //server 삭제
//        serverService.remove(server);
//        printLine();
//
//        //server 찾기 실패
//        try {
//            serverService.findById(server.getId());
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//        printLine();
//
//
//        //MessageRoom 생성 실패
//        try {
//            MessageRoom.create("", MessageRoomType.DM);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//        printLine();
//
//        //MessageRoom 생성 & 저장 성공
//        MessageRoom messageRoom = MessageRoom.create("first DM", MessageRoomType.DM);
//        messageRoomService.save(messageRoom);
//        printLine();
//
//
//
//        //MessageRoom 찾기 -성공
//        MessageRoom findMessageRoom = messageRoomService.findById(messageRoom.getId());
//        System.out.println("같은 객체인가요? "+(findMessageRoom.equals(messageRoom)));
//        printLine();
//
//
//
//        //MessageRoom 삭제
//        messageRoomService.remove(messageRoom);
//        printLine();
//
//        //MessageRoom 찾기 - 실패
//        try {
//            messageRoomService.findById(messageRoom.getId());
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//        printLine();
//
//        //User 모두 조회
//        userService.save(user);
//        User user2 = User.create("test2@email.com", "abcd1234", "roro", "010-4321-5678");
//        userService.save(user2);
//        printLine();
//
//        System.out.println("[유저 목록]");
//        userService.findAll().forEach(user1-> System.out.println(user1.getUsername()));
//        printLine();
//
//        //Channel 모두 조회
//        serverService.save(server);
//        Server server2 = Server.create("테스트 서버2");
//        serverService.save(server2);
//        printLine();
//
//        System.out.println("[채널 목록]");
//        serverService.findAll().forEach(c-> System.out.println(c.getServerName()));
//
//
//
//        //MessageRoom 모두 조회
//        messageRoomService.save(messageRoom);
//        MessageRoom messageRoom2 = MessageRoom.create("second MR", MessageRoomType.DM);
//        messageRoomService.save(messageRoom2);
//        printLine();
//
//        System.out.println("[채팅방 목록]");
//        messageRoomService.findAll().forEach(m-> System.out.println(m.getMessageRoomName()));
//        printLine();
//
//        //친구 추가 요청 보내기
//        FriendRequest friendRequest = userService.sendFriendRequest(user, user2);
//        System.out.println("보낸 사람이 user가 맞나요? "+(friendRequest.getSenderId()==user.getId()));
//        System.out.println("받은 사람이 user2가 맞나요? "+(friendRequest.getReceiverId()==user2.getId()));
//
//        //받은 친구 요청 확인하기
//        List<FriendRequest> receivedFriendRequests = userService.getReceivedFriendRequests(user2);
//        System.out.println("[친구 요청 보낸 사람]");
//        receivedFriendRequests.forEach(r-> System.out.println(userService.findById(r.getSenderId()).getUsername()));
//        FriendRequest request = receivedFriendRequests.stream().filter(r -> r.getSenderId() == user.getId()).findFirst().orElse(null);
//        printLine();
//
//        //친구 요청 받기 - 실패
//        try {
//            userService.acceptFriendRequest(user, request);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//        printLine();
//        userService.acceptFriendRequest(user2, request);
//
//    }
//
//    private static void printLine(){
//        System.out.println("======================================");
    }

}
