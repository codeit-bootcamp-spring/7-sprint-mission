package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFServiceFactory;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;
import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {
        //도메인 별 서비스 구현체를 테스트

        //팩토리 패턴으로 서비스 객체 주입
        JCFServiceFactory serviceFactory = JCFServiceFactory.getInstance();

        UserService userService = serviceFactory.getUserService();
        ChannelService channelService = serviceFactory.getChannelService();
        MessageService messageService = serviceFactory.getMessageService();

        //======================================================================================
        // user
        System.out.println("User 구현체 테스트");

        // 1. 유저 생성
        // 각 입력 요소 별 setter의 거름망 코드 추가 필요
        // 새 유저 생성시 id로 중복 확인 필요
        System.out.println("1. 유저 생성");
        userService.createUser("조성만", "smjoe0302", "smjoe0302@naver.com", "010-7140-6533", "abc1234", "qwer1234");
        userService.createUser("조조", "jojo", "jojo@naver.com", "010-1234-5678", "abc12345", "qwer12345");
        userService.createUser("홍길동", "hong", "honggd@naver.com", "010-2431-6540", "fert1234", "q1w2e3r4");

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
        read1 = userService.getUserByEmail("smjoe0302@daum.com");
        if(read1==null) System.out.println("조회된 유저가 없습니다.");

        System.out.println("\n유저 조회(다건)=================");
        userService.getAllUsers().forEach(System.out::println);

        // 3. 유저 업데이트
        System.out.println("\n3. 유저 업데이트");
        read1 = userService.getUserByEmail("smjoe0302@naver.com"); // 유저 검색
        read1.setEmail("smjoe030302@gmail.com"); // 유저 이메일 정보 변경
        userService.updateUser(read1); // 유저 정보 업데이트

        // 변경 전 이메일을 검색
        System.out.println("\n업데이트된 유저 조회(실패)=================");
        read1 = userService.getUserByEmail("smjoe0302@naver.com");
        if (read1 == null) System.out.println("검색된 사용자가 없습니다.");
        else System.out.println("업데이트된 유저 조회: " + read1);

        // 변경 후 이메일로 유저 검색
        System.out.println("\n업데이트된 유저 조회(성공)=================");
        read1 = userService.getUserByEmail("smjoe030302@gmail.com");
        if (read1 == null) System.out.println("검색된 사용자가 없습니다.");
        else System.out.println("업데이트된 유저 조회: " + read1);

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
        channelService.createChannel(Channel.ChannelType.VOICE, "코드잇 스프린트 스프링 백엔드 7기 음성 채널", user1);
        channelService.createChannel(Channel.ChannelType.MESSAGE, "코드잇 스프린트 스프링 백엔드 7기 메시지 채널", user2);
        channelService.createChannel(Channel.ChannelType.VOICE, "미니 게임 음성 채널", user3);
        channelService.createChannel(Channel.ChannelType.VOICE, "미니 게임 메시지 채널", user3);

        // 1.2 채널에 멤버 추가
        Channel channel = channelService.getChannel(user3.getChannelIds().get(0)); //하나의 채널만 가지고 있으므로 간단하게 채널 UUID 불러오기
        channelService.addMember(channel.getId(), user2); // 특정 UUID 채널에 멤버 추가

        // 2. 조회
        System.out.println("2. 채널 조회");

        // 2.1 id로 특정 채널 정보 조회(단건)
        System.out.println("\nid로 채널 조회=================");
        channel = channelService.getChannel(user3.getChannelIds().get(1));
        System.out.println(channel);

        // 2.2 유저가 속한 채널 정보 조회(다건)
        System.out.println("\n유저가 속한 채널들 조회=================");
        List<Channel> channels = channelService.getChannelByUser(user3);
        channels.forEach(System.out::println); // 1.2에서 추가한 멤버까지 출력 된다

        // 2.3 특정 타입의 채널 정보 조회(다건)
        System.out.println("\n음성 채널들 조회=================");
        List<Channel> voiceChannels = channelService.getChannelByType(Channel.ChannelType.VOICE);
        voiceChannels.forEach(System.out::println);

        System.out.println("\n메시지 채널들 조회=================");
        List<Channel> msgChannels = channelService.getChannelByType(Channel.ChannelType.MESSAGE);
        msgChannels.forEach(System.out::println);

        // 2.4 모든 채널 조회(다건)
        System.out.println("\n모든 채널들 조회=================");
        List<Channel> allChannels = channelService.getAllChannels();
        allChannels.forEach(System.out::println);

        // 3. 수정
        System.out.println("\n3. 채널 수정");

        // 3.1 특정 채널 관리자 변경
        System.out.println("\n채널 관리자 수정=================");
        Channel channelAdmin = channelService.getChannel(user3.getChannelIds().get(0));

        // 3.1.1 관리자 변경 전
        System.out.println("\n관리자 변경 전: " + channelAdmin);

        // 3.1.2 관리자 변경 후
        channelService.updateAdmin(channelAdmin.getId(), user2);
        System.out.println("관리자 변경 후: " + channelAdmin);

        // 3.1.3 채널에 속하지 않은 인원을 관리자로 변경하려는 경우
        channelService.updateAdmin(channelAdmin.getId(), user1); //채널에 속하지 않은 인원을 관리자로 변경 요청시 거부

        // 3.2 특정 채널 이름 변경
        System.out.println("\n채널 이름 수정=================");
        Channel channelName = channelService.getChannel(user2.getChannelIds().get(0));

        System.out.println("\n채널 이름 변경 전: " + channelName);
        channelService.updateName(channelName.getId(), "코드잇 스프린트 스프링 백엔드 6기 메시지 채널");
        System.out.println("채널 이름 변경 후: " + channelName);

        // 4. 삭제
        // 4.1 채널 삭제
        Channel channelDel = channelService.getChannel(user2.getChannelIds().get(0));

        System.out.println("\n4. 채널 삭제");
        System.out.println("\n채널 삭제 전: ");
        channelService.getAllChannels().forEach(System.out::println);

        channelService.delChannel(user2.getChannelIds().get(0), user1); //관리자가 아닌 경우 삭제 거부

        System.out.println("\n채널 삭제 후: ");
        channelService.delChannel(user2.getChannelIds().get(0), user2);
        channelService.getAllChannels().forEach(System.out::println);

        // 4.2 채널 내 멤버 삭제
        System.out.println("\n채널 내 멤버 삭제=================");
        channelDel = channelService.getChannel(user2.getChannelIds().get(0));

        System.out.println("\n채널 내 멤버 삭제 전: ");
        System.out.println(channelDel);

        System.out.println("\n채널 내 멤버 삭제 후: ");
        channelService.delChannelMember(user2.getChannelIds().get(0), user2, user1); // 채널에 속하지 않은 멤버는 삭제 할 수 없다
        channelService.delChannelMember(user2.getChannelIds().get(0), user2, user2); // 채널의 관리자는 채널에서 나갈 수 없다
        channelService.delChannelMember(user2.getChannelIds().get(0), user3, user2); // 채널의 유저는 채널 내 유저를 삭제할 수 없다
        channelService.delChannelMember(user2.getChannelIds().get(0), user2, user3);
        System.out.println(channelDel);

        // message
        System.out.println("======================================================================================");
        System.out.println("message 구현체 테스트");

        // 1. 메시지 생성
        System.out.println("1. 메시지 생성\n");
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
        messageService.delMessageByUser(user1);
        messages3 = messageService.getAllMessagesByUser(user1);
        messages3.forEach(System.out::println); //user1이 받은 메시지만 출력이 된다

        // 할일
        // getChannelIds() 삭제하기
        // 디스코드 기준으로 필요한 정보 도출 or 수정 해보기
        // 메시지 : 메시지 시간이랑 보낸 사람, 받은 사람 닉네임을 출력시키기

        // 질문 사항
        // user 클래스에 속해있는 채널 id들을 담은 리스트를 필드로 두는 것은 별로인가?

    }
}
