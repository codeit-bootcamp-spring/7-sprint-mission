package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.config.AppConfig;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.utils.Printer;
import com.sprint.mission.discodeit.utils.TestDataInitializer;

import java.io.File;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {
        UserRepository userRepository;
        ChannelRepository channelRepository;
        MessageRepository messageRepository;

        UserService userService;
        ChannelService channelService;
        MessageService messageService;

        //JCFService
        userRepository = JCFUserRepository.getInstance();
        channelRepository = JCFChannelRepository.getInstance();
        messageRepository = JCFMessageRepository.getInstance();

        userService = new JCFUserService(userRepository, messageRepository);
        channelService = new JCFChannelService(channelRepository);
        messageService = new JCFMessageService(messageRepository);

        //도메인 별 서비스 구현체를 테스트
        System.out.println("----도메인 별 JCF 서비스 구현체 테스트----");
        //======================================================================================
        // user
        System.out.println("User 구현체 테스트");

        // 1. 유저 생성

        System.out.println("1. 유저 생성");
        System.out.println("새로운 유저를 생성합니다.");
        userService.createUser("조성만", "smjoe0302", "smjoe0302@naver.com", "010-7140-6533", "abc1234", "qwer1234");
        userService.createUser("조조", "jojo", "jojo@naver.com", "010-1234-5678", "abc12345", "qwer12345");
        userService.createUser("홍길동", "hong", "honggd@naver.com", "010-2431-6540", "fert1234", "q1w2e3r4");
        userService.createUser("김도윤", "doyoon", "doyoon17@gmail.com", "010-5832-1745", "iddoyoon", "pass9832");
        userService.createUser("이지민", "jimin", "jimin_09@naver.com", "010-4275-6081", "idjimin", "pw4127!!");
        userService.createUser("김철수", "chulsoo", "chulsoo01@naver.com", "010-1111-2222", "idchulsoo", "pass1234");


        // 2. 유저 조회
        System.out.println("2. 유저 조회\n");
        System.out.println("유저 조회(단건)=================");
        User read1 = userService.getUserByEmail("smjoe0302@naver.com");
        System.out.println("이메일로 유저 조회: " + read1);

        User read2 = userService.getUserByPhone("010-1234-5678");
        System.out.println("전화번호로 유저 조회: " + read2);

        User read3 = userService.getUserByUserId("fert1234");
        System.out.println("아이디로 유저 조회: " + read3);

        System.out.println("유저 조회 실패(단건)=================");

        String userEmail = "smjoe0302@daum.com";
        try {
            read1 = userService.getUserByEmail(userEmail);
            System.out.println("이메일로 유저 조회: " + read1);
        } catch (Exception e) {
            System.out.printf("%s : %s", userEmail, e.getMessage());
        }

        System.out.println("\n유저 조회(다건)=================");
        userService.getAllUsers().forEach(System.out::println);

        System.out.println("\n유저 로그인=================");
        User loginUser1 = userService.login("abc1234", "qwer1234");
        System.out.printf("안녕하세요 %s님\n", loginUser1.getNickName());

        try {
            System.out.println("\n유저 로그인 실패=================");
            User loginUser2 = userService.login("abc1234", "qwer123");
            System.out.printf("안녕하세요 %s님\n", loginUser2.getNickName());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n유저 닉네임 조회=================");
        String nickName1 = userService.getUserNickName(loginUser1.getId());
        System.out.printf("유저의 닉네임은 %s입니다.\n", nickName1);


        // 3. 유저 업데이트
        System.out.println("\n3. 유저 업데이트");
        read1 = userService.getUserByEmail("smjoe0302@naver.com"); // 유저 검색
        read1.setEmail("smjoe030302@gmail.com"); // 유저 이메일 정보 변경
        userService.updateUser(read1); // 유저 정보 업데이트

        // 변경 전 이메일을 검색
        try {
            System.out.println("\n업데이트된 유저 조회(실패)=================");
            read1 = userService.getUserByEmail("smjoe0302@naver.com");
            System.out.println("업데이트된 유저 조회: " + read1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // 변경 후 이메일로 유저 검색
        System.out.println("\n업데이트된 유저 조회(성공)=================");
        read1 = userService.getUserByEmail("smjoe030302@gmail.com");
        System.out.println("업데이트된 유저 조회: " + read1);

        // 4. 유저 삭제
        System.out.println("\n4. 유저 삭제");
        System.out.println("\n삭제 전 모든 유저: ");
        userService.getAllUsers().forEach(System.out::println);

        System.out.println("\n삭제 후 모든 유저: ");
        UUID id = userService.getUserByEmail("smjoe030302@gmail.com").getId(); // 유저의 UUID 정보 저장
        userService.deleteUser(id); // 해당 UUID에 해당하는 유저 삭제
        userService.getAllUsers().forEach(System.out::println); // 모든 유저 출력

        // channel
        System.out.println("======================================================================================");
        System.out.println("channel 구현체 테스트");

        userService.createUser("조성만", "smjoe0302", "smjoe0302@naver.com", "010-7140-6533", "abc1234", "qwer1234");

        User user1 = userService.getUserByEmail("smjoe0302@naver.com");
        User user2 = userService.getUserByEmail("jojo@naver.com");
        User user3 = userService.getUserByEmail("honggd@naver.com");

        // 1. 생성
        System.out.println("1. 채널 생성");

        // 1.1 채널 생성
        Channel newChannel1, newChannel2, newChannel3, newChannel4;

        newChannel1 = channelService.createChannel(ChannelType.VOICE, "코드잇 스프린트 스프링 백엔드 7기 음성 채널", user1);
        newChannel2 = channelService.createChannel(ChannelType.MESSAGE, "코드잇 스프린트 스프링 백엔드 7기 메시지 채널", user2);
        newChannel3 = channelService.createChannel(ChannelType.VOICE, "미니 게임 음성 채널", user3);
        newChannel4 = channelService.createChannel(ChannelType.VOICE, "미니 게임 메시지 채널", user3);


        // 1.2 채널에 멤버 추가
        System.out.println("\n채널에 멤버 추가=================");
        channelService.addMember(newChannel3.getId(), user2); // 특정 UUID 채널에 멤버 추가
        List<Channel> user2Channel = channelService.getChannelByUser(user2);

        System.out.println("user2가 속한 채널 : ");
        for (Channel channel : user2Channel) {
            System.out.printf("%s \n", channel);
        }

        // 2. 조회
        System.out.println("2. 채널 조회");

        // 2.1 id로 특정 채널 정보 조회(단건)
        System.out.println("\nid로 채널 조회=================");
        Channel channel = channelService.getChannel(newChannel2.getId());
        System.out.println(channel);

        // 2.2 유저가 속한 채널 정보 조회(다건)
        System.out.println("\n유저가 속한 채널들 조회=================");
        List<Channel> channels = channelService.getChannelByUser(user3);
        channels.forEach(System.out::println); // 1.2에서 추가한 멤버까지 출력 된다

        // 2.3 특정 타입의 채널 정보 조회(다건)
        System.out.println("\n음성 채널들 조회=================");
        List<Channel> voiceChannels = channelService.getChannelByType(ChannelType.VOICE);
        voiceChannels.forEach(System.out::println);

        System.out.println("\n메시지 채널들 조회=================");
        List<Channel> msgChannels = channelService.getChannelByType(ChannelType.MESSAGE);
        msgChannels.forEach(System.out::println);

        // 2.4 모든 채널 조회(다건)
        System.out.println("\n모든 채널들 조회=================");
        List<Channel> allChannels = channelService.getAllChannels();
        allChannels.forEach(System.out::println);

        // 3. 수정
        System.out.println("\n3. 채널 수정");

        // 3.1 특정 채널 관리자 변경
        System.out.println("\n채널 관리자 수정=================");
        Channel channelAdmin = channelService.getChannel(newChannel3.getId());

        // 3.1.1 관리자 변경 전
        System.out.println("\n관리자 변경 전: " + channelAdmin);

        // 3.1.2 관리자 변경 후
        channelService.updateAdmin(channelAdmin.getId(), user2);
        System.out.println("관리자 변경 후: " + channelAdmin);

        // 3.1.3 채널에 속하지 않은 인원을 관리자로 변경하려는 경우
        try {
            channelService.updateAdmin(channelAdmin.getId(), user1); //채널에 속하지 않은 인원을 관리자로 변경 요청시 거부
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        // 3.2 특정 채널 이름 변경
        System.out.println("\n채널 이름 수정=================");
        Channel channelName = channelService.getChannel(newChannel2.getId());

        System.out.println("\n채널 이름 변경 전: " + channelName);
        channelService.updateName(channelName.getId(), "코드잇 스프린트 스프링 백엔드 6기 메시지 채널");
        System.out.println("채널 이름 변경 후: " + channelName);

        // 4. 삭제
        // 4.1 채널 삭제

        System.out.println("\n4. 채널 삭제");
        System.out.println("\n채널 삭제 전: ");
        channelService.getAllChannels().forEach(System.out::println);

        try {
            channelService.deleteChannel(newChannel2.getId(), user1); //관리자가 아닌 경우 삭제 거부
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n채널 삭제 후: ");
        channelService.deleteChannel(newChannel2.getId(), user2);
        channelService.getAllChannels().forEach(System.out::println);

        // 4.2 채널 내 멤버 삭제
        System.out.println("\n채널 내 멤버 삭제=================");
        Channel channelDel = channelService.getChannel(newChannel3.getId());

        System.out.println("\n채널 내 멤버 삭제 전: ");
        System.out.println(channelDel);

        System.out.println("\n채널 내 멤버 삭제 후: ");
        try {
            channelService.deleteChannelMember(newChannel3.getId(), user2, user1); // 채널에 속하지 않은 멤버는 삭제 할 수 없다
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            channelService.deleteChannelMember(newChannel3.getId(), user2, user2); // 채널의 관리자는 채널에서 나갈 수 없다
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            channelService.deleteChannelMember(newChannel3.getId(), user3, user2); // 채널의 유저는 채널 내 유저를 삭제할 수 없다
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        channelService.deleteChannelMember(newChannel3.getId(), user2, user3);
        System.out.println(channelDel);

        // message
        System.out.println("======================================================================================");
        System.out.println("message 구현체 테스트");

        // 1. 메시지 생성
        System.out.println("1. 메시지 생성\n");

        user1 = userService.getUserByEmail("doyoon17@gmail.com");
        user2 = userService.getUserByEmail("jimin_09@naver.com");
        user3 = userService.getUserByEmail("chulsoo01@naver.com");

        messageService.createMessage(user1, user2, "안녕하세요 반갑습니다.");
        messageService.createMessage(user2, user1, "좋은 아침입니다.");
        messageService.createMessage(user2, user1, "무슨 일로 연락을 주셨나요?");

        messageService.createMessage(user1, user3, "오늘 그거 봤어?");
        messageService.createMessage(user3, user1, "뭐를?");
        messageService.createMessage(user1, user3, "국가정보자원관리원에 화재 났대");
        messageService.createMessage(user3, user1, "진짜? 몇시에?");


        // 2. 메시지 조회
        System.out.println("2. 메시지 조회");
        // 2.1 두 유저 간 제일 최근 메시지 조회(단건)
        System.out.println("두 유저 간 제일 최근 메시지 조회: ");
        Message lastestMsg = messageService.getLastestMessage(user1, user2);
        System.out.println(lastestMsg);

        // 2.2 두 유저 간 보낸 모든 메시지 조회(다건)
        System.out.println("\n두 유저 간 모든 메시지 조회: ");
        List<Message> messages = messageService.getMessagesBetween(user1, user2);
        messages.forEach(System.out::println);

        // 2.3 특정 유저의 모든 메시지 조회
        System.out.println("\n특정 유저의 모든 메시지 조회: ");
        List<Message> messages2 = messageService.getAllMessagesByUser(user1);
        messages2.forEach(System.out::println);

        // 3. 메시지 수정
        System.out.println("\n3. 메시지 수정");
        Message lastestMsg2 = messageService.getLastestMessage(user1, user3);

        System.out.println("\n메시지 수정 전: ");
        System.out.println(lastestMsg2);

        messageService.updateMessage(lastestMsg2.getId(), "아 9월 26일 20시 15분 경에 났네?");
        System.out.println("\n메시지 수정 후: ");
        System.out.println(lastestMsg2);

        // 4. 메시지 삭제
        System.out.println("\n4. 메시지 삭제");

        // 4.1 특정 메시지 삭제
        System.out.println("\n특정 메시지 삭제=================");
        System.out.println("\n메시지 삭제 전: ");
        System.out.println(messageService.getLastestMessage(user1, user3));

        messageService.deleteMessage(lastestMsg2.getId()); //user1과 user3간의 가장 최근 메시지 삭제

        System.out.println("\n메시지 삭제 후: ");
        System.out.println(messageService.getLastestMessage(user1, user3));

        // 4.2 특정 유저가 보낸 모든 메시지 삭제
        System.out.println("\n특정 유저가 보낸 모든 메시지 삭제=================");
        System.out.println("\n메시지 삭제 전: ");
        List<Message> messages3 = messageService.getAllMessagesByUser(user1);
        messages3.forEach(System.out::println);

        System.out.println("\n메시지 삭제 후: ");
        messageService.deleteMessagesByUser(user1);
        messages3 = messageService.getAllMessagesByUser(user1);
        messages3.forEach(System.out::println); //user1이 받은 메시지만 출력이 된다


        //요구사항을 이용한 간단한 프로그램 실행
        DiscodeitTest discodeitTest = DiscodeitTest.init();
        discodeitTest.run();

        // ===========================================
        // 테스트용 기본 유저 데이터 (테스트/샘플용)
        // ===========================================
        // 이름      | 아이디      | 비밀번호
        // -------------------------------------------
        // 테스트    | test1234   | test1234
        // 박지훈    | idjihun    | securePass1!
        // 이수빈    | idsubin    | myPassword2@
        // 최하늘    | idhaneul   | skyPass123!
        // 이영희    | idyounghee | pw5678
        // 박민수    | idminsu    | securepw1
        // 최지현    | idjihyun   | mypassword2
        // 오세훈    | idsehun    | hello4321
        // ===========================================
        // 실제 데이터 생성은 TestDataInitializer 클래스에서 수행
        // 기존 File I/O 서비스 사용으로 파일 내 유저 데이터 삭제시 유저 일부가 삭제되었을 수 있다
    }
}
