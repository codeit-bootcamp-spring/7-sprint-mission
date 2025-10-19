package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.factory.FileServiceFactory;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.factory.JCFServiceFactory;
import com.sprint.mission.discodeit.utils.Printer;

import java.io.File;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {
        final String FILE_PATH = "C:\\Users\\user\\Workspace\\codeit-sprint-bootcamp\\7-sprint-mission\\data";
        String userFilePath = FILE_PATH + "\\test_users.sav";
        String channelFilePath = FILE_PATH + "\\test_channels.sav";
        String joinedFilePath = FILE_PATH + "\\test_joined_files.sav";
        String messageFilePath = FILE_PATH + "\\test_messages.sav";

        //도메인 별 서비스 구현체를 테스트

        //팩토리 패턴으로 서비스 객체 주입
        JCFServiceFactory serviceFactory = JCFServiceFactory.getInstance();
        FileServiceFactory fileServiceFactory = new FileServiceFactory(userFilePath, channelFilePath, joinedFilePath, messageFilePath);

        //JCFService
//        UserService userService = serviceFactory.getUserService();
//        ChannelService channelService = serviceFactory.getChannelService();
//        MessageService messageService = serviceFactory.getMessageService();

        //FileService
        UserService userService = fileServiceFactory.getUserService();
        ChannelService channelService = fileServiceFactory.getChannelService();
        MessageService messageService = fileServiceFactory.getMessageService();

        //======================================================================================
        // user
        System.out.println("User 구현체 테스트");

        // 1. 유저 생성
        try {
            System.out.println("1. 유저 생성");
            if (!new File(userFilePath).exists()) {
                System.out.println("새로운 유저를 생성합니다.");
                userService.createUser("조성만", "smjoe0302", "smjoe0302@naver.com", "010-7140-6533", "abc1234", "qwer1234");
                userService.createUser("조조", "jojo", "jojo@naver.com", "010-1234-5678", "abc12345", "qwer12345");
                userService.createUser("홍길동", "hong", "honggd@naver.com", "010-2431-6540", "fert1234", "q1w2e3r4");
                userService.createUser("김도윤", "doyoon", "doyoon17@gmail.com", "010-5832-1745", "iddoyoon", "pass9832");
                userService.createUser("이지민", "jimin", "jimin_09@naver.com", "010-4275-6081", "idjimin", "pw4127!");
                userService.createUser("김철수", "chulsoo", "chulsoo01@naver.com", "010-1111-2222", "idchulsoo", "pass1234");
            } else{
                System.out.println("유저가 존재하여 유저를 생성하지 않습니다.");
            }

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
            if (read1 == null) System.out.println("조회된 유저가 없습니다.");

            System.out.println("\n유저 조회(다건)=================");
            userService.getAllUsers().forEach(System.out::println);

            System.out.println("\n유저 로그인=================");
            User loginUser1 = userService.login("abc1234", "qwer1234");
            if (loginUser1 != null) {
                System.out.printf("안녕하세요 %s님\n", loginUser1.getNickName());
            } else {
                System.out.println("아이디 또는 비밀번호가 틀렸습니다.");
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
            Channel newChannel1, newChannel2, newChannel3 = null, newChannel4;

            //채널 파일이 저장 되어있으면 생성하지 않는다.
            if (!new File(channelFilePath).exists()) {
                newChannel1 = channelService.createChannel(Channel.ChannelType.VOICE, "코드잇 스프린트 스프링 백엔드 7기 음성 채널", user1);
                newChannel2 = channelService.createChannel(Channel.ChannelType.MESSAGE, "코드잇 스프린트 스프링 백엔드 7기 메시지 채널", user2);
                newChannel3 = channelService.createChannel(Channel.ChannelType.VOICE, "미니 게임 음성 채널", user3);
                newChannel4 = channelService.createChannel(Channel.ChannelType.VOICE, "미니 게임 메시지 채널", user3);
            } else { // 기존 테스트 환경으로 되돌린다
                System.out.println("테스트를 위해 채널 파일을 처음 상태로 돌립니다.");
                List<Channel> channels = channelService.getAllChannels();

                for(Channel c :  channels) {
                    if(c.getChannelName().equals("코드잇 스프린트 스프링 백엔드 7기 음성 채널")){
                        newChannel1 = c;
                    } else if (c.getChannelName().equals("미니 게임 음성 채널")) {
                        newChannel3 = c;
                        channelService.addMember(newChannel3.getId(), user3);
                        newChannel3.setAdmin(user3);
                    } else if (c.getChannelName().equals("미니 게임 메시지 채널")){
                        newChannel4 = c;
                    }
                }
                newChannel2 = channelService.createChannel(Channel.ChannelType.MESSAGE, "코드잇 스프린트 스프링 백엔드 7기 메시지 채널", user2);
            }

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
            Channel channelAdmin = channelService.getChannel(newChannel3.getId());

            // 3.1.1 관리자 변경 전
            System.out.println("\n관리자 변경 전: " + channelAdmin);

            // 3.1.2 관리자 변경 후
            channelService.updateAdmin(channelAdmin.getId(), user2);
            System.out.println("관리자 변경 후: " + channelAdmin);

            // 3.1.3 채널에 속하지 않은 인원을 관리자로 변경하려는 경우
            channelService.updateAdmin(channelAdmin.getId(), user1); //채널에 속하지 않은 인원을 관리자로 변경 요청시 거부

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

            channelService.deleteChannel(newChannel2.getId(), user1); //관리자가 아닌 경우 삭제 거부

            System.out.println("\n채널 삭제 후: ");
            channelService.deleteChannel(newChannel2.getId(), user2);
            channelService.getAllChannels().forEach(System.out::println);

            // 4.2 채널 내 멤버 삭제
            System.out.println("\n채널 내 멤버 삭제=================");
            Channel channelDel = channelService.getChannel(newChannel3.getId());

            System.out.println("\n채널 내 멤버 삭제 전: ");
            System.out.println(channelDel);

            System.out.println("\n채널 내 멤버 삭제 후: ");
            channelService.deleteChannelMember(newChannel3.getId(), user2, user1); // 채널에 속하지 않은 멤버는 삭제 할 수 없다
            channelService.deleteChannelMember(newChannel3.getId(), user2, user2); // 채널의 관리자는 채널에서 나갈 수 없다
            channelService.deleteChannelMember(newChannel3.getId(), user3, user2); // 채널의 유저는 채널 내 유저를 삭제할 수 없다
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
            System.out.println("======================================================================================");
            System.out.println("디스코드잇 어플리케이션 입니다.");

            //두번째 테스트를 위해 저장되는 파일 경로 변경
            userFilePath = FILE_PATH + "\\users.sav";
            channelFilePath = FILE_PATH + "\\channels.sav";
            joinedFilePath = FILE_PATH + "\\joined_files.sav";
            messageFilePath = FILE_PATH + "\\messages.sav";

            FileServiceFactory fileServiceFactory2 = new FileServiceFactory(userFilePath, channelFilePath, joinedFilePath, messageFilePath);
            userService = fileServiceFactory2.getUserService();
            channelService = fileServiceFactory2.getChannelService();
            messageService = fileServiceFactory2.getMessageService();

            //이미 생성했다면 생성하지 않는다.
            if(!new File(userFilePath).exists()) {
                // 계정 생성하기 -> 메시지 생성 위함
                userService.createUser("테스트", "test", "test@naver.com", "010-1234-5678", "test1234", "test1234");
                userService.createUser("박지훈", "jihun", "jihun01@gmail.com", "010-1234-5678", "idjihun", "securePass1!");
                userService.createUser("이수빈", "subin", "subin09@naver.com", "010-8765-4321", "idsubin", "myPassword2@");
                userService.createUser("최하늘", "haneul", "haneul77@kakao.com", "010-3344-5566", "idhaneul", "skyPass123!");
                userService.createUser("이영희", "younghee", "younghee02@gmail.com", "010-3333-4444", "idyounghee", "pw5678");
                userService.createUser("박민수", "minsu", "minsu03@daum.net", "010-5555-6666", "idminsu", "securepw1");
                userService.createUser("최지현", "jihyun", "jihyun04@naver.com", "010-7777-8888", "idjihyun", "mypassword2");
                userService.createUser("오세훈", "sehun", "sehun05@kakao.com", "010-9999-0000", "idsehun", "hello4321");

                User discordItUser1 = userService.getUserByEmail("test@naver.com");
                User discordItUser2 = userService.getUserByEmail("jihun01@gmail.com");
                User discordItUser3 = userService.getUserByEmail("subin09@naver.com");
                User discordItUser4 = userService.getUserByEmail("haneul77@kakao.com");
                User discordItUser5 = userService.getUserByEmail("younghee02@gmail.com");
                User discordItUser6 = userService.getUserByEmail("minsu03@daum.net");
                User discordItUser7 = userService.getUserByEmail("jihyun04@naver.com");
                User discordItUser8 = userService.getUserByEmail("sehun05@kakao.com");

                // 메시지 생성
                // (1) discordItUser1 ↔ discordItUser2
                messageService.createMessage(discordItUser1, discordItUser2, "안녕하세요, 잘 지내시죠?");
                messageService.createMessage(discordItUser2, discordItUser1, "네! 오랜만이에요.");
                messageService.createMessage(discordItUser1, discordItUser2, "이번 주말에 시간 괜찮으세요?");
                messageService.createMessage(discordItUser2, discordItUser1, "네, 좋아요. 같이 영화 볼까요?");

                // (2) discordItUser3 ↔ discordItUser4
                messageService.createMessage(discordItUser3, discordItUser4, "오늘 회의 준비는 잘 하고 있어?");
                messageService.createMessage(discordItUser4, discordItUser3, "응, 거의 다 끝났어. 너는?");
                messageService.createMessage(discordItUser3, discordItUser4, "나도 정리 중이야. 내일 자료 같이 확인하자.");
                messageService.createMessage(discordItUser4, discordItUser3, "좋아! 저녁에 영상 통화하자.");

                // (3) discordItUser5 ↔ discordItUser6
                messageService.createMessage(discordItUser5, discordItUser6, "어제 경기 봤어?");
                messageService.createMessage(discordItUser6, discordItUser5, "봤지! 진짜 재밌더라.");
                messageService.createMessage(discordItUser5, discordItUser6, "그러니까. 마지막 골은 전율이었어.");
                messageService.createMessage(discordItUser6, discordItUser5, "담주에 같이 보러 갈래?");

                // (4) discordItUser7 ↔ discordItUser8
                messageService.createMessage(discordItUser7, discordItUser8, "새로 나온 게임 해봤어?");
                messageService.createMessage(discordItUser8, discordItUser7, "응! 그래픽이 진짜 대박이야.");
                messageService.createMessage(discordItUser7, discordItUser8, "같이 파티 맺고 해볼래?");
                messageService.createMessage(discordItUser8, discordItUser7, "좋지! 오늘 밤에 접속하자.");

                // 채널 생성
                Channel channel1 = channelService.createChannel(Channel.ChannelType.VOICE, "스터디 음성 채널", discordItUser1);
                Channel channel2 = channelService.createChannel(Channel.ChannelType.MESSAGE, "프로젝트 토론 메시지 채널", discordItUser2);
                Channel channel3 = channelService.createChannel(Channel.ChannelType.VOICE, "게임 파티 음성 채널", discordItUser3);
                Channel channel4 = channelService.createChannel(Channel.ChannelType.MESSAGE, "공지사항 메시지 채널", discordItUser4);
                Channel channel5 = channelService.createChannel(Channel.ChannelType.VOICE, "음악 감상 음성 채널", discordItUser5);
                Channel channel6 = channelService.createChannel(Channel.ChannelType.MESSAGE, "자유 대화 메시지 채널", discordItUser6);
                Channel channel7 = channelService.createChannel(Channel.ChannelType.VOICE, "코딩 면접 준비 음성 채널", discordItUser7);
                Channel channel8 = channelService.createChannel(Channel.ChannelType.MESSAGE, "Q&A 메시지 채널", discordItUser8);

                // 채널에 멤버 추가
                // channel1 (관리자: discordItUser1)
                channelService.addMember(channel1.getId(), discordItUser3);
                channelService.addMember(channel1.getId(), discordItUser6);

                // channel2 (관리자: discordItUser2)
                channelService.addMember(channel2.getId(), discordItUser1);
                channelService.addMember(channel2.getId(), discordItUser4);
                channelService.addMember(channel2.getId(), discordItUser6);
                channelService.addMember(channel2.getId(), discordItUser8);

                // channel3 (관리자: discordItUser3)
                channelService.addMember(channel3.getId(), discordItUser2);
                channelService.addMember(channel3.getId(), discordItUser5);
                channelService.addMember(channel3.getId(), discordItUser7);

                // channel4 (관리자: discordItUser4)
                channelService.addMember(channel4.getId(), discordItUser1);
                channelService.addMember(channel4.getId(), discordItUser2);
                channelService.addMember(channel4.getId(), discordItUser6);
                channelService.addMember(channel4.getId(), discordItUser7);
                channelService.addMember(channel4.getId(), discordItUser8);

                // channel5 (관리자: discordItUser5)
                channelService.addMember(channel5.getId(), discordItUser4);

                // channel6 (관리자: discordItUser6)
                channelService.addMember(channel6.getId(), discordItUser2);
                channelService.addMember(channel6.getId(), discordItUser3);
                channelService.addMember(channel6.getId(), discordItUser5);
                channelService.addMember(channel6.getId(), discordItUser7);
                channelService.addMember(channel6.getId(), discordItUser8);

                // channel7 (관리자: discordItUser7)
                channelService.addMember(channel7.getId(), discordItUser3);
                channelService.addMember(channel7.getId(), discordItUser6);

                // channel8 (관리자: discordItUser8)
                channelService.addMember(channel8.getId(), discordItUser1);
                channelService.addMember(channel8.getId(), discordItUser2);
                channelService.addMember(channel8.getId(), discordItUser3);
                channelService.addMember(channel8.getId(), discordItUser4);
                channelService.addMember(channel8.getId(), discordItUser5);
                channelService.addMember(channel8.getId(), discordItUser6);

                // 채널 메시지 추가(메시지 채널에만 추가)

                // channel2 (discordItUser2 관리자) : "프로젝트 토론 메시지 채널"
                messageService.createMessage(discordItUser2, channel2, "이번 주까지 로그인 기능 마무리해야 할 것 같아요.");
                messageService.createMessage(discordItUser4, channel2, "DB 쪽은 제가 맡을게요.");
                messageService.createMessage(discordItUser6, channel2, "UI는 제가 진행할게요.");
                messageService.createMessage(discordItUser8, channel2, "테스트 케이스 작성은 제가 해보겠습니다.");
                messageService.createMessage(discordItUser2, channel2, "좋아요. 그럼 목요일까지 중간 점검합시다.");
                messageService.createMessage(discordItUser1, channel2, "확인했습니다!");

                // channel4 (discordItUser4 관리자) : "공지사항 메시지 채널"
                messageService.createMessage(discordItUser4, channel4, "📢 이번 주 토요일 정기 점검이 있습니다.");
                messageService.createMessage(discordItUser1, channel4, "확인했습니다!");
                messageService.createMessage(discordItUser2, channel4, "네 알겠습니다.");
                messageService.createMessage(discordItUser6, channel4, "그날은 접속 못하겠네요 ㅎㅎ");
                messageService.createMessage(discordItUser4, channel4, "불편 드려 죄송합니다. 더 나은 환경을 위해 점검하는 거예요.");
                messageService.createMessage(discordItUser7, channel4, "넵 고생 많으십니다!");

                // channel6 (discordItUser6 관리자) : "자유 대화 메시지 채널"
                messageService.createMessage(discordItUser6, channel6, "오늘 점심 뭐 먹었어요?");
                messageService.createMessage(discordItUser2, channel6, "저는 김치찌개 먹었어요 ㅎㅎ");
                messageService.createMessage(discordItUser8, channel6, "저는 편의점 샌드위치로 때웠어요.");
                messageService.createMessage(discordItUser7, channel6, "저는 돈가스 먹었습니다!");
                messageService.createMessage(discordItUser3, channel6, "다들 맛있게 드셨네요. 전 샐러드만 먹었어요.");
                messageService.createMessage(discordItUser6, channel6, "역시 점심 메뉴 얘기가 제일 재밌어요 ㅋㅋ");

                // channel8 (discordItUser8 관리자) : "Q&A 메시지 채널"
                messageService.createMessage(discordItUser8, channel8, "스프링에서 DI가 뭔지 설명해줄 수 있나요?");
                messageService.createMessage(discordItUser5, channel8, "의존성 주입이에요. 객체 간 결합도를 낮추는 개념이죠.");
                messageService.createMessage(discordItUser3, channel8, "맞아요. 보통 @Autowired 같은 걸로 구현합니다.");
                messageService.createMessage(discordItUser2, channel8, "또는 생성자 주입 방식도 많이 씁니다.");
                messageService.createMessage(discordItUser6, channel8, "그게 테스트할 때 더 유리하다고 들었어요.");
                messageService.createMessage(discordItUser8, channel8, "아~ 이제 확실히 이해됐습니다. 감사합니다!");
            }
            // 어플리케이션에 필요한 변수 간단히 생성
            Scanner sc = new Scanner(System.in);

            String userId;
            String password;

            User user;
            User receiver = null;

            Channel userChannel;
            UUID channelId;
            List<User> channelMember;

            boolean login = false;
            int choice; // switch 이동시 사용

            outer:
            while (true) {
                Printer.printLine();
                System.out.println("메뉴");
                System.out.println("1. 로그인 2. 계정 생성 3. 어플리케이션 종료");
                System.out.print("입력: ");
                choice = sc.nextInt();
                sc.nextLine();
                switch (choice) {
                    case 1:
                        System.out.println("로그인을 해주세요");
                        System.out.print("아이디: ");
                        userId = sc.nextLine();
                        System.out.print("비밀번호: ");
                        password = sc.nextLine();

                        System.out.println("아이디: " + userId);
                        System.out.println("비밀번호: " + password);

                        User loginUser = userService.login(userId, password);

                        if (loginUser == null) {
                            System.out.println("아이디 또는 비밀번호가 틀렸습니다. 처음 메뉴로 돌아갑니다.");
                            continue;
                        } else {
                            user = loginUser; // 로그인된 유저 정보 저장
                            login = true; // 로그인 여부 저장
                        }

                        while (login) { // 로그아웃 전까지 반복
                            Printer.printLine();
                            System.out.printf("환영합니다. %s님\n", user.getNickName());
                            System.out.println("1. 채팅 2. 채널 3. 내 정보 출력 4. 내 정보 수정 5. 계정 삭제 6. 로그아웃");
                            System.out.print("입력: ");
                            choice = sc.nextInt();
                            sc.nextLine();

                            switch (choice) {
                                case 1:
                                    System.out.println("채팅을 할 닉네임을 골라주세요.");
                                    List<User> users = userService.getAllUsers();
                                    users.remove(user); //내 정보 리스트에서 삭제
                                    for (int i = 0; i <= users.size(); i++) {
                                        if (i == users.size()) {
                                            System.out.printf("%d. 나가기\n", i + 1);
                                            break;
                                        }

                                        System.out.printf("%d. ", i + 1);
                                        Printer.printChatLatest(messageService, user, users.get(i)); // 제일 최근 메시지도 닉네임과 함께 출력
                                    }
                                    System.out.println();
                                    System.out.print("입력: ");
                                    choice = sc.nextInt();
                                    sc.nextLine(); // 개행 제거
                                    if (choice == (users.size() + 1)) continue; // 나가기 선택시 선택 창으로 이동

                                    receiver = users.get(choice - 1);
                                    Printer.printLine();

                                    List<Message> msgs = messageService.getMessagesBetween(user, receiver);
                                    Printer.printChatHistory(userService, user, msgs); // 이전 채팅 내용 출력
                                    String input = null;
                                    while (true) {
                                        Printer.printHalfLine();
                                        System.out.println("채팅 입력중(채팅방을 갱신 하려면 1, 채팅방에서 나가려면 -1을 입력하세요.)");
                                        System.out.print("입력: ");
                                        input = sc.nextLine();


                                        if (input.equals("1")) {
                                            msgs = messageService.getMessagesBetween(user, receiver);
                                            Printer.printChatHistory(userService, user, msgs);
                                        } else if (input.equals("-1")) break;
                                        else {
                                            messageService.createMessage(user, receiver, input);
                                        }
                                    }
                                    break;
                                case 2:
                                    outer3:
                                    while (true) {
                                        System.out.println("1. 채널 입장 2. 채널 생성 3. 이전 메뉴");
                                        choice = sc.nextInt();
                                        sc.nextLine();
                                        switch (choice) {
                                            case 1:
                                                List<Channel> userChannels = channelService.getChannelByUser(user);
                                                if (userChannels.isEmpty()) {
                                                    System.out.println("현재 속해있는 채널이 없습니다.");
                                                    break;
                                                }

                                                System.out.println("입장을 원하시는 채널을 선택해주세요.");
                                                Channel c;
                                                for (int i = 0; i <= userChannels.size(); i++) {
                                                    if (i == userChannels.size()) {
                                                        System.out.printf("%d. 이전 메뉴\n", i + 1);
                                                        break;
                                                    }
                                                    c = userChannels.get(i);
                                                    System.out.printf("%d. %s(%s)\n", i + 1, c.getChannelName(), c.getChannelType());
                                                }

                                                System.out.print("입력: ");
                                                choice = sc.nextInt();
                                                sc.nextLine();

                                                if (choice == (userChannels.size() + 1)) break;

                                                channelId = userChannels.get(choice - 1).getId();
                                                userChannel = channelService.getChannel(channelId); //선택된 채널 불러오기

                                                outer2:
                                                while (true) {
                                                    Printer.printLine();
                                                    System.out.printf("%s에 입장하였습니다.\n", userChannel.getChannelName());
                                                    boolean isAdmin = userChannel.getAdmin().equals(user); // 로그인 유저가 채널 관리자인지 확인

                                                    // 채널 타입에 따라 출력
                                                    if (userChannel.getChannelType() == Channel.ChannelType.MESSAGE) {
                                                        System.out.print("1. 채널 채팅 입장 ");
                                                    } else if (userChannel.getChannelType() == Channel.ChannelType.VOICE) {
                                                        System.out.print("1. 통화방 입장 ");
                                                    }

                                                    System.out.print("2. 채널 멤버 조회 ");

                                                    if (isAdmin) { // 관리자 전용 메뉴 출력
                                                        System.out.println("3. 채널 이름 변경 4. 채널 관리자 변경 5. 채널 내 멤버 삭제 6. 채널 삭제 7. 이전 메뉴");
                                                    } else {
                                                        System.out.println("3. 채널 나가기 4. 이전 메뉴");
                                                    }
                                                    System.out.print("입력: ");
                                                    choice = sc.nextInt();
                                                    sc.nextLine();

                                                    if (!isAdmin && choice == 3) choice = 8; //관리자가 아닌 유저가 채널 나가기 선택시 번호 변경
                                                    if (!isAdmin && choice == 4) choice = 7; //관리자 아닌 유저가 이전 메뉴 선택시 번호 변경

                                                    switch (choice) {
                                                        case 1:
                                                            //채팅방과 통화방 나누기
                                                            if (userChannel.getChannelType() == Channel.ChannelType.MESSAGE) {
                                                                Printer.printLine();
                                                                System.out.println("채널 채팅 입장");
                                                                Printer.printLine();

                                                                List<Message> channelMessages = messageService.getAllByChannel(userChannel);
                                                                Printer.printChatHistory(userService, user, channelMessages);
                                                                while (true) {
                                                                    Printer.printHalfLine();
                                                                    System.out.println("채팅 입력중(채팅방을 갱신 하려면 1, 채팅방에서 나가려면 -1을 입력하세요.)");
                                                                    System.out.print("입력: ");
                                                                    input = sc.nextLine();

                                                                    if (input.equals("1")) {
                                                                        channelMessages = messageService.getAllByChannel(userChannel);
                                                                        Printer.printChatHistory(userService, user, channelMessages);
                                                                    } else if (input.equals("-1")) break;
                                                                    else {
                                                                        messageService.createMessage(user, userChannel, input);
                                                                    }
                                                                }

                                                            } else if (userChannel.getChannelType() == Channel.ChannelType.VOICE) {
                                                                Printer.printLine();
                                                                System.out.println("전화를 진행합니다. 끊으시려면 -1을 입력하세요");
                                                                choice = sc.nextInt();
                                                                if (choice == -1) break;
                                                            }
                                                            break;
                                                        case 2:
                                                            Printer.printHalfLine();
                                                            System.out.println("채널 멤버 조회");
                                                            channelMember = userChannel.getMembers();
                                                            for (int i = 0; i < channelMember.size(); i++) {
                                                                System.out.printf("%d. %s\n", i + 1, channelMember.get(i).getNickName());
                                                            }
                                                            Printer.printHalfLine();
                                                            break;
                                                        case 3:
                                                            System.out.println("채널 이름 변경을 선택하였습니다. 변경 이름 작성해주세요. 이전 메뉴로 가시려면 -1을 입력하세요");
                                                            System.out.print("변경할 채널 이름: ");
                                                            String newChannelName = sc.nextLine();
                                                            if(!newChannelName.equals("-1")) {
                                                                channelService.updateName(userChannel.getId(), newChannelName);
                                                            }
                                                            break;
                                                        case 4:
                                                            System.out.println("채널 관리자 변경을 선택하였습니다. 변경을 원하는 멤버를 선택해주세요.");
                                                            List<User> members = userChannel.getMembers();
                                                            members.remove(user);

                                                            for(int i = 0; i <= members.size(); i++) {
                                                                if (i == members.size()) {
                                                                    System.out.printf("%d. 이전 메뉴\n", i);
                                                                    break;
                                                                }
                                                                System.out.printf("%d. %s\n", i+1, members.get(i).getNickName());
                                                            }

                                                            System.out.print("입력: ");
                                                            choice = sc.nextInt();
                                                            sc.nextLine();

                                                            if(choice < members.size()){
                                                                User newAdmin = members.get(choice-1);
                                                                channelService.updateAdmin(userChannel.getId(), newAdmin);
                                                            } else if(choice > members.size()){
                                                                System.out.println("올바르지 않은 입력입니다. 다시시도해주세요. 이전 메뉴로 돌아갑니다.");
                                                            }
                                                            break;
                                                        case 5:
                                                            System.out.println("삭제할 멤버를 선택해주세요");
                                                            channelMember = userChannel.getMembers();

                                                            if (channelMember.size() == 1) {
                                                                System.out.println("채널에 혼자만 존재합니다. 채널 나가기를 진행해주세요");
                                                                break;
                                                            }

                                                            for (int i = 0; i < channelMember.size(); i++) {
                                                                if (user.getNickName().equals(channelMember.get(i).getNickName()))
                                                                    continue; //로그인 된 유저의 정보는 제외
                                                                System.out.printf("%d. %s\n", i + 1, channelMember.get(i).getNickName());
                                                            }
                                                            System.out.print("입력: ");
                                                            choice = sc.nextInt();
                                                            sc.nextLine();

                                                            User target = channelMember.get(choice - 1); //삭제되는 멤버 정보 저장
                                                            System.out.println("채널에서 멤버를 삭제하려면 멤버 닉네임을 입력해주세요");
                                                            System.out.print("입력: ");
                                                            String userName = sc.nextLine();

                                                            if (userName.equals(target.getNickName())) {
                                                                channelService.deleteChannelMember(userChannel.getId(), user, target);
                                                                System.out.println("선택된 멤버가 삭제되었습니다.");
                                                            } else {
                                                                System.out.println("닉네임이 정확하지 않습니다. 메뉴로 돌아갑니다.");
                                                            }
                                                            break;
                                                        case 6:
                                                            //채널 삭제시 연동되는 것들까지 삭제하기
                                                            System.out.println("정말로 채널을 삭제하시겠습니까?");
                                                            System.out.println("삭제를 원하시면 채널 이름을 정확히 입력해주세요");
                                                            System.out.printf("채널 이름: %s\n", userChannel.getChannelName());
                                                            System.out.print("입력: ");
                                                            if (sc.nextLine().equals(userChannel.getChannelName())) {
                                                                channelService.deleteChannel(userChannel.getId(), user);
                                                                System.out.println("채널이 삭제되었습니다. 이전 메뉴로 돌아갑니다.");
                                                                Printer.printLine();
                                                            } else {
                                                                System.out.println("정확히 입력되지 않았습니다. 이전 메뉴로 돌아갑니다.");
                                                                break;
                                                            }
                                                            break outer2;
                                                        case 7:
                                                            System.out.println("이전 메뉴로 돌아갑니다.");
                                                            break outer2;
                                                        case 8:
                                                            System.out.printf("정말로 %s을 나가시길 원하시면 1을 눌러주세요.\n", userChannel.getChannelName());
                                                            System.out.print("입력: ");
                                                            choice = sc.nextInt();

                                                            if (choice == 1)
                                                                channelService.deleteChannelMember(userChannel.getId(), user, user);
                                                            break outer2;
                                                    }
                                                }
                                                break;
                                            case 2:
                                                System.out.println("새로운 채널을 생성합니다. 정보를 입력해주세요");
                                                System.out.println("채널 타입 : 1. 메시지 2. 음성");
                                                System.out.print("입력: ");
                                                choice = sc.nextInt();
                                                sc.nextLine();
                                                System.out.print("채널 이름: ");
                                                String newChannelName = sc.nextLine();
                                                switch (choice) {
                                                    case 1:
                                                        channelService.createChannel(Channel.ChannelType.MESSAGE, newChannelName, user);
                                                        break;
                                                    case 2:
                                                        channelService.createChannel(Channel.ChannelType.VOICE, newChannelName, user);
                                                        break;
                                                    default:
                                                        System.out.println("채널 타입을 정확이 골라주세요 이전 메뉴로 돌아갑니다.");
                                                        break;
                                                }
                                                break;
                                            case 3:
                                                System.out.println("이전 메뉴로 돌아갑니다.");
                                                break outer3;
                                        }
                                    }
                                    break;
                                case 3:
                                    Printer.printLine();
                                    Printer.printInfo(user);
                                    Printer.printLine();
                                    break;
                                case 4:
                                    System.out.println("내 정보 수정");
                                    System.out.println("1. 이름 2. 닉네임 3. 이메일 4. 전화번호 5. 비밀번호 6. 나가기");
                                    choice = sc.nextInt();
                                    sc.nextLine();
                                    switch (choice) {
                                        case 1:
                                            System.out.println("현재 이름: " + user.getUserName());
                                            System.out.print("변경: ");
                                            user.setUserName(sc.nextLine());
                                            break;
                                        case 2:
                                            System.out.println("현재 닉네임: " + user.getNickName());
                                            System.out.print("변경: ");
                                            user.setNickName(sc.nextLine());
                                            break;
                                        case 3:
                                            System.out.println("현재 이메일: " + user.getEmail());
                                            System.out.print("변경: ");
                                            user.setEmail(sc.nextLine());
                                            break;
                                        case 4:
                                            System.out.println("현재 전화번호: " + user.getPhoneNum());
                                            System.out.print("변경: ");
                                            user.setPhoneNum(sc.nextLine());
                                            break;
                                        case 5:
                                            String nowPassword, newPassword, newPassword2;

                                            System.out.print("현재 비밀번호 입력: ");
                                            nowPassword = sc.nextLine();
                                            if (user.getPassword().equals(nowPassword)) {

                                                System.out.print("새로운 비밀번호 입력: ");
                                                newPassword = sc.nextLine();

                                                System.out.print("새로운 비밀번호 한번 더 입력: ");
                                                newPassword2 = sc.nextLine();

                                                if (newPassword.equals(newPassword2)) {
                                                    user.setPassword(newPassword);
                                                    login = false;
                                                    System.out.println("비밀번호가 변경되었습니다. 다시 로그인 해주세요.");
                                                }
                                            } else {
                                                System.out.println("비밀번호를 틀렸습니다. 다시 입력하세요.");
                                            }
                                            break;
                                        case 6:
                                            continue;
                                    }
                                    userService.updateUser(user);
                                    break;
                                case 5:
                                    System.out.println("진짜로 계정을 삭제하시겠습니까?");
                                    System.out.println("삭제를 원하시면 아이디와 비밀번호를 정확히 입력해주세요");

                                    System.out.print("아이디: ");
                                    userId = sc.nextLine();
                                    System.out.print("비밀번호: ");
                                    password = sc.nextLine();

                                    if (user.getUserId().equals(userId) && user.getPassword().equals(password)) {
                                        userService.deleteUser(user.getId());
                                        System.out.println("계정이 삭제되었습니다. 처음 메뉴로 돌아갑니다.");
                                        login = false;
                                    } else {
                                        System.out.println("정확히 입력되지 않았습니다.");
                                    }
                                    break;
                                case 6:
                                    System.out.println("계정을 로그아웃 합니다. 처음 메뉴로 돌아갑니다.");
                                    login = false;
                                    break;
                            }
                        }
                        break;
                    case 2:
                        String name, nickName, email, phoneNum, newUserId, newPassword;
                        System.out.println("계정을 생성합니다. 정보를 입력해주세요.");
                        System.out.print("이름: ");
                        name = sc.nextLine();
                        System.out.print("닉네임: ");
                        nickName = sc.nextLine();
                        System.out.print("이메일: ");
                        email = sc.nextLine();
                        System.out.print("전화번호: ");
                        phoneNum = sc.nextLine();
                        System.out.print("아이디: ");
                        newUserId = sc.nextLine();
                        System.out.print("비밀번호: ");
                        newPassword = sc.nextLine();

                        userService.createUser(name, nickName, email, phoneNum, newUserId, newPassword);
                        System.out.println("계정이 생성되었습니다. 로그인 해주세요.");
                        break;
                    case 3:
                        System.out.println("디스코드잇 어플리케이션을 종료합니다. 안녕히 가세요.");
                        break outer;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
