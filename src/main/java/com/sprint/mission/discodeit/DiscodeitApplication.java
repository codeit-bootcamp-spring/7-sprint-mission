package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.application.dto.request.UserCreateRequestDto;
import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.domain.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class DiscodeitApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);
//        new를 이용해 필요한 객체를 생성하게 되면 개발자 필요한 곳에서 객체를 직접 생성해야 하고 객체의 생명주기를 직접 관리하게 된다.
//        같은 타입의 다른 객체가 필요할 경우, 객체 생성하는 코드를 직접 수정해야 하기도 한다.
//        그리고 같은 객체가 여러번 생성되지 않게 주의를 기울여야 한다
//        빈이라는 개념도 없고 그냥 객체가 존재한다.
//
//        나 같은 경우는 원래
//        WebConfig 클래스를 통해 모든 클래스들의 의존성 관리를 하고 있었다,
//                WebConfig를 생성해, 필요한 객체들을 가져와서 사용할 수 있었고, 다른 클래스로 갈아키우고 싶으면 WebConfig만 수정하면 되었다.
//        그리고 WebConfig에게 같은 타입의 객체를 요청하면 같은 매번 같은 객체가 응답된다.
//
//                내가 만든 WebConfig의 기능을 더 쉽게 해주는 게 스프링 컨테이너이다.
//        @Component 계열(@Repository, @Service, @Controller 등등)이 붙은 클래스들을 자동으로 빈 등록을 해준다
//
//        내가 만든 WebConfig에서는 객체 생성에 필요한 매개변수들을 직접 코딩해야 했다
//        그러나 이제는 스프링 컨테이너가 자동으로 해준다.
//        스프링 컨테이너가 관리하는 객체를 Bean이라고 하며, Bean은 컨테이너에서 기본적으로는 싱글톤으로 관리되고, 필요할 때 자동으로 주입된다.
//                객체의 생명주기도 컨테이너에서 자동으로 관리를 한다.


        //ApplicationContext의 하위 인터페이스인 ConfigurableApplicationContext를 사용해도 ok


        UserFacade userService = context.getBean(UserFacade.class);
        UserRepository userRepository = context.getBean(UserRepository.class);

        //유저 추가 dto 만들고 회원가입
        UserCreateRequestDto test1 =
                new UserCreateRequestDto(
                        "test@email",
                        "1234",
                        "test1",
                        "010-1234-1234",
                        null);

        try {
            userService.createUser(test1);

        } catch (IOException e) {
            System.out.println( e.getMessage());
        }
        User user =
                userRepository.findByEmail("test@email").orElse(null);
        System.out.println(user.getEmail());
        System.out.println(user.getPassword());
        System.out.println(user.getPhoneNumber());
        System.out.println(user.getUsername());

//        DiContainer diContainer = new DiContainer();

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
