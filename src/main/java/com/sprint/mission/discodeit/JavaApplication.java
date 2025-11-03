package com.sprint.mission.discodeit;
/*
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

import java.util.NoSuchElementException;
import java.util.UUID;

public class JavaApplication {
    enum SelectRepo { JCF, FILE }
    private static SelectRepo selectRepo = SelectRepo.JCF;

    private static void titlePrint(String title) {
        System.out.println(" ");
        System.out.println("==================== " + title + " ====================");
        System.out.println(" ");
    }

    public static void main(String[] args) {
        ChannelRepository chRepo;
        UserRepository userRepo;
        MessageRepository msgRepo;

        if(selectRepo == SelectRepo.FILE) {
            chRepo = new JCFChannelRepository();
            userRepo = new JCFUserRepository();
            msgRepo = new JCFMessageRepository();
        } else {
            chRepo = new FileChannelRepository();
            userRepo = new FileUserRepository();
            msgRepo = new FileMessageRepository();
        }

        ChannelService channelService = new BasicChannelService(chRepo);
        UserService userService = new BasicUserService(userRepo);
        MessageService messageService = new BasicMessageService(msgRepo, chRepo, userRepo);

        System.out.println("1. User Service 체크");
        titlePrint("등록");
        User user1 = userService
                .create(new User("이형일","dlguddlf","eldh1000@naver.com"));
        User user2 = userService
                .create(new User("김태언", "rlaxodjs", "xodjs@gmail.com"));
        User user3 = userService
                .create(new User("최지혜", "chlwlgP", "wlgP@daum.net"));
        System.out.println("Users count: " + userService.getAll().size());

        System.out.print(user1.getUsername() + "님이 가입하셨습니다.");
        System.out.println("환영합니다!");
        System.out.println("Email: " + userService.getUsersByEmail(user1.getEmail()).get().getEmail());
        System.out.println("Name: " + userService.getUsersByName(user1.getUsername()).get(0).getUsername());

        titlePrint("로그인");
        User logined = userService.login(user1.getEmail(), "dlguddlf");
        System.out.println("Logged in!, " + logined.getUserState());

        titlePrint("로그아웃");
        userService.logout(user1.getEmail());
        System.out.println("Logged out!," + logined.getUserState());

        titlePrint("조회 단건");
        System.out.println(userService
                .getUsersByName(user1.getUsername()).get(0).getUsername() + "님이 확인되었습니다.");

        titlePrint("조회 다건");
        System.out.println("조회 결과: " + userService.getAll().size());

        titlePrint("수정");
        user1.setPassword("dltmdals");
        userService.update(user1);
        System.out.println("비밀번호가 변경되었습니다.");

        titlePrint("수정된 내용으로 로그인");
        User logined2 = userService.login(user1.getEmail(), "dltmdals");
        System.out.println("Logged in!, " + logined2.getUserState());

        titlePrint("로그아웃");
        userService.logout(user1.getEmail());
        System.out.println("Logged out!," + logined2.getUserState());


        titlePrint("수정된 데이터 조회");
        userService.getUsersByName("이형일");
        System.out.println("이름: " + userService
                .getUsersByName(user1.getUsername()).get(0).getUsername());
        System.out.println("이메일: " + userService
                .getUsersByEmail(user1.getEmail()).get().getEmail());
        System.out.println(userService
                .getUsersByName(user1.getUsername()).get(0).getUsername() + "님이 확인되었습니다.");

        titlePrint("Fail case");
        try {
            userService.login(user1.getEmail(), "dlgydbgfegg");
            System.out.println("Fail: 이메일 또는 비밀번호가 맞지 않습니다.");
        } catch (IllegalArgumentException e) {
            System.out.println("로그인 실패! " + e.getMessage());
        }

        titlePrint("삭제");
        boolean delete = userService.delete(user1.getId());
        System.out.println("제거 결과: " + delete);

        titlePrint("삭제된 데이터 조회");
        try {
            userService.get(user1.getId());
            System.out.println("Fail: 삭제된 유저가 여전히 존재합니다.");
        } catch (NoSuchElementException e) {
            System.out.println( "삭제 되었습니다! , "+ e.getMessage());
        }

        System.out.println("현재 총 인원: " + userService.getAll().size());
        System.out.println("남은 인원: " + userService.getAll());

        // 재사용을 위해 다시 등등
        user1 = userService
                .create(new User("이형일","dlguddlf","eldh1000@naver.com"));

        System.out.println(" ");
        System.out.println("2. Channel Service 체크");
        titlePrint("등록");
        Channel channel1 = channelService
                .create(new Channel(ChannelType.VOICE, "Sprint1"));
        Channel channel2 = channelService
                .create(new Channel(ChannelType.TEXT, "Sprint2"));
        Channel channel3 = channelService
                .create(new Channel(ChannelType.VOICE, "Sprint3"));
        Channel channel4 = channelService
                .create(new Channel(ChannelType.TEXT, "Sprint4"));

        System.out.println("채널 생성 완료!");
        System.out.println("현재 채널 수: " + channelService.getAll().size());

        titlePrint("조회 단건");
        Channel channelStore = channelService.get(channel1.getId());
        System.out.println("채널 확인: " + channelService.get(channelStore.getId()).channelName());

        titlePrint("조회 다건");
        System.out.println("채널 확인: " + channelService.getAll().size());

        titlePrint("Join");
        String ch1 = channelService.get(channel1.getId()).channelName();
        boolean joined1 = channelService.join(channel1.getId(), user2.getId());
        channelService.get(channel1.getId())
                .getMembers()
                .forEach((id,role) -> System.out.println(ch1 + " : " + role));
        System.out.println("접속 결과: " + joined1);
        System.out.println("현재 채널의 인원 수: " + channelService
                .get(channel1.getId())
                .getMembers().size());
        System.out.println(" ");

        // 중복가입 체크
        boolean joined2 = channelService.join(channel1.getId(), user2.getId());
        channelService.get(channel1.getId())
                .getMembers()
                .forEach((id,role) -> System.out.println(ch1 + " : " + role));
        System.out.println("접속 결과: " + joined2);
        System.out.println("현재 채널의 인원 수: " + channelService
                .get(channel1.getId())
                .getMembers().size());
        System.out.println(" ");

        boolean joined3 = channelService.join(channel1.getId(), user3.getId());
        channelService.get(channel1.getId())
                .getMembers()
                .forEach((id,role) -> System.out.println(ch1 + " : " + role));
        System.out.println("접속 결과: " + joined3);
        System.out.println("현재 채널의 인원 수: " + channelService
                .get(channel1.getId())
                .getMembers().size());

        titlePrint("Fail case");
        try {
            channelService.join(UUID.randomUUID(), user2.getId());
            System.out.println("Fail: 존재하지 않는 채널에 join 성공");
        } catch (NoSuchElementException e) {
            System.out.println("존재하지 않는 채널 join 거부: " + e.getMessage());
        }

        try {
            channelService.join(channel1.getId(), null);
            System.out.println("Fail: null userId join 가능");
        } catch (IllegalArgumentException e) {
            System.out.println("null userId join 불가: " + e.getMessage());
        }

        titlePrint("Leave");
        System.out.println("탈퇴 전 채널의 인원 수: " + channel1.getMembers().size());
        System.out.println(" ");
        boolean leave1 = channelService.leave(channel1.getId(), user2.getId());
        channelService.get(channel1.getId())
                .getMembers()
                .forEach((id,role) -> System.out.println(ch1 + " : " + role));
        System.out.println("탈퇴 결과: " + leave1);
        System.out.println("현재 채널의 인원 수: " + channelService.get(channel1.getId())
                .getMembers().size());
        System.out.println(" ");

        // 중복 탈퇴 체크
        boolean leave2 = channelService.leave(channel1.getId(), user2.getId());
        channelService.get(channel1.getId())
                .getMembers()
                .forEach((id,role) -> System.out.println(ch1 + " : " + role));
        System.out.println("탈퇴 결과: " + leave2);
        System.out.println("현재 채널의 인원 수: " +  channelService.get(channel1.getId())
                .getMembers().size());

        titlePrint("SlowMode");
        channelService.setSlowModeSeconds(channel1.getId(), 10);

        channelService.join(channel1.getId(), user2.getId());

        messageService.create(new Message("테스트1", user2.getUsername(), user2.getId(), channel1.getId()));
        try {
            messageService.create(new Message("테스트2", user2.getUsername(), user2.getId(), channel1.getId()));
            System.out.println("슬로우모드에서 딜레이 없이 전송 됨!");
        } catch (IllegalStateException e) {
            System.out.println("남은 시간초 : " + e.getMessage());
        }

        titlePrint("수정");
        channel1.setChannelName("Sprint1!!!!!");
        channelService.update(channel1);
        System.out.println("채널이 수정되었습니다.");

        titlePrint("수정된 데이터 조회");
        channelService.get(channel1.getId());
        System.out.println("채널 확인: " + channelService.get(channel1.getId()).channelName());

        titlePrint("삭제");
        boolean deleted2 = channelService.delete(channel1.getId());
        System.out.println("제거 결과: " + deleted2);

        titlePrint("삭제된 데이터 조회");
        try {
            channelService.get(channel1.getId());
            System.out.println("Fail: 삭제된 채널이 여전히 존재합니다.");
        } catch (NoSuchElementException e) {
            System.out.println("채널 삭제!");
        }
        System.out.println("남은 채널 수: " + channelService.getAll().size());

        // 이후 사용하기 위해 재등록
        channel1 = channelService
                .create(new Channel(ChannelType.VOICE, "Sprint1"));
        // 3. Message Service 체크 시작 직전
        channelService.join(channel1.getId(), user1.getId()); // message1: user1 -> channel1
        channelService.join(channel2.getId(), user2.getId()); // message2: user2 -> channel2
        channelService.join(channel3.getId(), user3.getId()); // message3: user3 -> channel3
        channelService.join(channel2.getId(), user1.getId()); // message4: user1 -> channel2


        System.out.println(" ");
        System.out.println("3. Message Service 체크");
        titlePrint("등록");
        Message message1 = messageService
                .create(new Message("하이", user1.getUsername(), user1.getId(), channel1.getId()));
        Message message2 = messageService
                .create(new Message("하이2", user2.getUsername(), user2.getId(), channel2.getId()));
        Message message3 = messageService
                .create(new Message("하이3", user3.getUsername(), user3.getId(), channel3.getId()));
        Message message4 = messageService
                .create(new Message("하이4", user1.getUsername(), user1.getId(), channel2.getId()));
        System.out.println("채팅을 입력해주세요!");

        titlePrint("조회 단건");
        System.out.println(messageService.getMessagesByChannel(channel1.getId()));
        System.out.println(messageService.getMessagesByAuthor(user1.getId()));
        System.out.println(messageService.getMessagesByChannelAndAuthor(channel1.getId(), user1.getId()));

        titlePrint("조회 다건");
        System.out.println("메세지의 총 개수: " + messageService.getAllMessages().size());
        System.out.println(messageService.getAllMessages());

        titlePrint("수정");
        System.out.println("수정전 " + message1.getUserName() + " 님의 메세지 : " + message1.getContent());
        message1.setContent("백엔드 sprint 미션 중입니당");
        messageService.update(message1);
        System.out.println("메세지가 수정 되었습니다.");

        titlePrint("수정된 데이터 조회");
        System.out.println("수정된 " + message1.getUserName() + " 님의 메세지 : " + message1.getContent());
        System.out.println(messageService.getAllMessages());

        titlePrint("삭제");
        boolean msg = messageService.delete(message1.getId());
        System.out.println("삭제 결과: " + msg);

        titlePrint("삭제된 데이터 조회");
        try {
            messageService.get(message1.getId());
            System.out.println("메세지가 삭제되지 않고 여전히 존재합니다.");
        } catch (NoSuchElementException e) {
            System.out.println("메세지가 삭제 되었습니다.");
        }
            System.out.println("남은 메세지: " + messageService.getAllMessages());

        titlePrint("키워드 검색");
        String keyword = "하이";
        System.out.println("키워드 " + keyword + " 검색: " + messageService.searchByKeyword(keyword).size());
        messageService.searchByKeyword(keyword)
                .forEach(m -> System.out.println("- " + m.getContent()));

    }
}

 */
