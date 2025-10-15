package com.sprint.mission;

import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.Channel.ChannelType;
import com.sprint.mission.entity.Message;
import com.sprint.mission.entity.Receivable;
import com.sprint.mission.entity.User;
import com.sprint.mission.service.file.FileChannelService;
import com.sprint.mission.service.file.FileMessageService;
import com.sprint.mission.service.file.FileUserService;
import com.sprint.mission.service.jcf.JCFChannelService;
import com.sprint.mission.service.jcf.JCFMessageService;
import com.sprint.mission.service.jcf.JCFUserService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class DiscordIt {

//    private static final JCFMessageService messageService = JCFMessageService.getInstance();
//    private static final JCFUserService userService = JCFUserService.getInstance();
//    private static final JCFChannelService channelService = JCFChannelService.getInstance();
    private static final FileMessageService messageService = FileMessageService.getInstance();
    private static final FileUserService userService = FileUserService.getInstance();
    private static final FileChannelService channelService = FileChannelService.getInstance();

    private Scanner scanner = new Scanner(System.in);
    private boolean exitFlag = false;

    private String loginId; // 현재 로그인한 유저 아이디

    public void start() {
        // 초기에 사용할 데이터들을 모아놓는 곳
        System.out.println("=========== discordit ==============");
        run();
    }

    public void run() {
        init(); // 초기 데이터 설정

        while (!exitFlag) {
            if (loginId == null)
                userLogin(); // 시작시 로그인 함
            menu();
        }
    }

    private void menu() {
        printLine();
        System.out.println("[메뉴]");
        System.out.println("1. 채널 2. 회원 3. 로그아웃 (4. 관리자 모드)");
        switch (getInput(4)) {
            case 1 -> channel();
            case 2 -> user();
            case 3 -> logout();
            case 4 -> admin();
        }
    }

    private void admin() {
        printLine();
        System.out.println("[관리자 모드]");
        System.out.println("1. 메세지");
        messageSearch();
    }

    private void messageSearch() {
        printLine();
        List<Message<Receivable>> messageList = new ArrayList<>();
        System.out.println("[메세지 조회]");
        System.out.println("1. 발신자로 메세지 조회");
        System.out.println("2. 발신자와 수신자로 메세지 조회");
        System.out.println("3. 채널 메세지 조회");
        switch (getInput(3)) {
            case 1 -> {
                System.out.print("아이디를 입력하세요 : ");
                String userId = scanner.next();
                messageList = messageService.getBySender(userService.getById(userId));
            }
            case 2 ->{
                System.out.print("발신자 아이디를 입력하세요 : ");
                String senderId = scanner.next();
                System.out.print("수신자 아이디를 입력하세요 : ");
                String receiverId = scanner.next();
                messageList = messageService.getBySenderAndReceiver(
                        userService.getById(senderId),
                        userService.getById(receiverId)
                );
            }
            case 3 -> {
                UUID receiverId = displayChannelAndSelect(channelService.getAllChannelIds());
                messageList = messageService.getByReceiver(channelService.getChannelById(receiverId));
            }
        }
        for (Message<Receivable> message : messageList) {
            display(message);
        }
    }

    private<T extends Receivable> void display(Message<T> message) {
        System.out.printf("[%s] -> [%s] \n%s (%s)\n",
                message.getSender().getUserId(),
                message.getReceiver().getDisplayName(),
                message.getMessage(),
                message.getCreatedTime());
    }

    private void user() {
        printLine();
        System.out.println("[회원]");
        while (true) {
            System.out.println("1. 내 정보 조회/수정 2. 온라인 회원 조회 3. 메세지 보내기 4. 전체 회원 조회 0. 종료");
            switch (getInput(0, 4)) {
                case 1 -> manageMyProfile();
                case 2 -> printOnlineUsers();
                case 3 -> sendDirectMessage();
                case 4 -> printAllUsers();
                case 0 -> {
                    return;
                }
            }
        }
    }

    private void printAllUsers() {
        printUserDetails(userService.getAllUsers());
    }

    private void printOnlineUsers() {
        List<String> onlineUsers = userService.getOnlineUsers();
        System.out.println("현재 접속 중인 유저들 : ");
        printUserDetails(onlineUsers);
    }

    private void printUserDetails(List<String> userIds) {
        for (int i = 0; i < userIds.size(); i++) {
            String nowId = userIds.get(i);
            System.out.printf("-\t%d.[%s]\t%s(%s) \"%s\"\n",
                    i + 1,
                    nowId,
                    userService.getDisplayName(nowId),
                    userService.getOnlineStatus(nowId),
                    userService.getBio(nowId) == null ? "" : userService.getBio(nowId));
        }
    }

    private void sendDirectMessage() {
        List<String> onlineUsers = userService.getOnlineUsers();
        System.out.println("현재 접속 중인 유저들 : ");
        printUserDetails(onlineUsers);
        System.out.print("몇 번 유저에게 메세지를 보내시겠습니까? >> ");

        String receiveId = onlineUsers.get(getInput(onlineUsers.size()) - 1);

        String message;
        while (true) {
            System.out.print("메세지 (종료는 0) : ");
            message = scanner.next();

            if (message.equals("0"))
                return;

            messageService.sendMessage(
                    userService.getById(loginId),
                    userService.getById(receiveId),
                    message
            );
        }
    }

    private void manageMyProfile() {
        printLine();
        System.out.println("[내 정보]");
        System.out.printf("아이디 : %s\n" +
                        "닉네임 : %s\n" +
                        "한마디 : %s\n" +
                        "현재 상태 : %s\n",
                loginId,
                userService.getDisplayName(loginId),
                userService.getBio(loginId),
                userService.getOnlineStatus(loginId));
        System.out.println();

        System.out.printf("1. 프로필 수정하기 0. 돌아가기 >> ");
        if (getInput(0, 1) == 0)
            return;
        System.out.println("변경할 것을 선택해주세요.");
        System.out.print("1. 비밀번호 2. 닉네임 3. 한마디 4. 현재상태 >>");

        switch (getInput(4)) {
            case 1 -> {
                System.out.print("현재 비밀번호를 입력해주세요 >> ");
                String passWd = scanner.next();

                int tried = 1;
                while (userService.login(loginId, passWd)) {
                    System.out.println("비밀번호가 올바르지 않습니다.");
                    tried++;

                    if (tried >= 3) {
                        System.out.println("3번 연속으로 실패하였으므로 이전으로 돌아갑니다.");
                        return;
                    }

                    System.out.print("현재 비밀번호를 입력해주세요 >> ");
                    passWd = scanner.next();
                }
                System.out.print("새 비밀번호를 입력해주세요 >> ");
                passWd = scanner.next();
                userService.setPasswd(loginId, passWd);
            }
            case 2 -> {
                System.out.print("변경 닉네임 >>");
                userService.setDisplayName(loginId, scanner.next());
            }
            case 3 -> {
                System.out.print("변경 한마디 >>");
                userService.setBio(loginId, scanner.next());
            }
            case 4 -> {
                System.out.print("변경할 상태를 선택해주세요. ");
                User.Status[] statuses = User.Status.values();
                for (int i = 0; i < statuses.length; i++) {
                    System.out.printf("\t%d. \t\t%s ", i + 1, statuses[i]);
                }
                userService.setOnlineStatus(loginId, statuses[getInput(statuses.length + 1) - 1]);
            }
        }
        System.out.println("정상적으로 변경되었습니다.");
    }

    private void logout() {
        System.out.println("로그아웃 합니다...");
        userService.setOnlineStatus(loginId, User.Status.OFFLINE);
        loginId = null;
    }

    private void channel() {
        int userCommand;
        do {
            printLine();
            System.out.println("채널 조회");
            System.out.print("1. 가입 채널 목록 2. 전체 채널 목록 3. 채널 접속하기 \n4. 채널 등록하기 5. 채널 나오기 \n6. 채널 만들기 0. 돌아가기 \n>>");
             userCommand = getInput(0, 6);
            switch (userCommand) {
                case 1 -> printRegisteredChannel();
                case 2 -> {
                    List<UUID> allChannel = channelService.getAllChannelIds();
                    if (allChannel.isEmpty()) {
                        System.out.println("현재 개설된 채널이 없습니다.");
                    }
                    System.out.println("전체 채널 목록 : ");
                    printChannelDetails(allChannel);
                }
                case 3 -> enterChannel();
                case 4 -> registerChannel();
                case 5 -> leaveChannel();
                case 6 -> createChannel();
            }
        } while (userCommand != 0);
    }

    private void leaveChannel() {

        printLine();
        List<UUID> registeredChannels = channelService.getRegisteredChannels(userService.getById(loginId));
        System.out.print("나갈 채널을 선택해주세요. >>");
        UUID channel = displayChannelAndSelect(registeredChannels);
        channelService.deleteMember(channel, userService.getById(loginId));
        System.out.println("채널에서 나왔습니다.");
    }

    private void enterChannel() {
        printLine();
        List<UUID> channels = channelService.getRegisteredChannels(userService.getById(loginId));
        if (channels.isEmpty()) {
            System.out.println("가입된 채널이 없습니다.");
            return;
        }

        printNumberedChannels(channels);
        System.out.print("접속할 채널을 선택해주세요. (0 : 취소) >>");
        channelMessageSend(channels.get(getInput(0, channels.size()) - 1));
    }

    private void channelMessageSend(UUID uuid) {
        User sender = userService.getById(loginId);
        Channel receiver = channelService.getChannelById(uuid);
        String message;

        while (true) {
            System.out.print("메세지 입력 (0 : 종료) : ");
            message = scanner.next();

            if (message.equals("0"))
                return;
            messageService.sendMessage(sender, receiver, message);
        }
    }

    private void printRegisteredChannel() {
        printLine();
        List<UUID> channels = channelService.getRegisteredChannels(userService.getById(loginId));

        if (channels.isEmpty()) {
            System.out.println("가입된 채널이 없습니다.");
            return;
        }

        printNumberedChannels("현재 가입된 채널 : ", channels);
    }

    private void registerChannel() {
        List<UUID> channels = channelService.getNotRegisteredChannels(userService.getById(loginId));

        if (channels.isEmpty()) {
            System.out.println("이미 모든 채널에 가입되어있습니다.");
            return;
        }

        UUID selectedChannel = displayChannelAndSelect("가입되지 않은 채널 : ", channels);
        channelService.addMember(selectedChannel, userService.getById(loginId));
        System.out.printf("채널 [%s]에 가입되었습니다!\n", channelService.getChannelById(selectedChannel).getDisplayName());
    }

    private void userLogin() {
        while (loginId == null) {
            printLine();
            System.out.print("1. 로그인 2. 회원가입\n >> ");

            switch (getInput(2)) {
                case 1 -> login();
                case 2 -> signIn();
            }
        }

        printLine();
        System.out.printf("%s님, 환영합니다! \n", userService.getDisplayName(loginId));
    }

    private void createChannel() {
        printLine();

        System.out.print("생성할 채널의 이름을 입력해주세요. >> ");
        String channelName = scanner.next();
        System.out.println("채널의 타입을 입력해주세요.");
        ChannelType[] types = ChannelType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.printf("%d. %s", i + 1, types[i]);
        }
        System.out.println();
        System.out.print(">> ");
        int type = getInput(types.length) - 1;
        channelService.createChannel(
                channelName,
                types[type],
                userService.getById(loginId),
                userService.getById(loginId)
        );
        System.out.println("채널이 생성되었습니다.");
    }

    private void printChannelDetails(List<UUID> uuids) {
        printLine();

        for (UUID uuid : uuids) {
            Channel channel = channelService.getChannelById(uuid);
            System.out.printf("\t- %s \t\t---\t%s\n", channel.getDisplayName(), channel.getType());
        }
    }

    /**
     * 주어진 채널 uuid 목록으로 각 채널에 대한 정보를 간단하게 출력함
     * 예시 : 1. 모각코     --- VOICE
     *
     * @param uuids 채널 고유 uuid
     */
    private void printNumberedChannels(List<UUID> uuids) {
        for (int i = 0; i < uuids.size(); i++) {
            Channel channel = channelService.getChannelById(uuids.get(i));
            System.out.printf("\t%d. \t\t%s \t(%s)\n", i + 1, channel.getDisplayName(), channel.getType());
        }
    }

    private void printNumberedChannels(String message, List<UUID> uuids) {
        System.out.println(message);
        printNumberedChannels(uuids);
    }

    private UUID displayChannelAndSelect(List<UUID> uuids) {
        printNumberedChannels(uuids);
        System.out.print("채널 번호를 선택해주세요 : ");
        return uuids.get(getInput(uuids.size()) - 1);
    }

    private UUID displayChannelAndSelect(String message, List<UUID> uuids){
        System.out.println(message);
        return displayChannelAndSelect(uuids);
    }

    private void enterChannel(UUID uuid) {
        printLine();
        Channel channel = channelService.getChannelById(uuid);
        while (true) {
            System.out.printf("채널 [%s]에 접속하였습니다.\n" +
                            "1. 전체 사용자 보기 2. 메세지 보내기 3. 뒤로 돌아가기 >>",
                    channel.getDisplayName());
            switch (getInput(3)) {
                case 1 -> printAllRegisteredUser(uuid);
                case 2 -> sendChannelMessage(uuid);
                case 3 -> {
                    return;
                }
            }
        }
    }

    private void sendChannelMessage(UUID uuid) {
        User user = userService.getById(loginId);
        Channel channel = channelService.getChannelById(uuid);
        System.out.print("메세지 입력(종료하려면 -를 입력) : ");
        while (true) {
            String message = scanner.next();
            if (message.equals("-"))
                return;
            messageService.sendMessage(user, channel, message);
        }
    }

    private void printAllRegisteredUser(UUID uuid) {
        Set<User> users = channelService.getAllMembers(uuid);
        for (User user : users) {
            System.out.printf("\t- %s(%s)\n", user.getDisplayName(), user.getOnlineStatus());
        }
    }

    // 로그인
    public void login() {
        String id;
        String passwd;

        try {
            System.out.print("아이디를 입력해주세요 >> ");
            id = scanner.next();
            System.out.print("비밀번호를 입력해주세요 >> ");
            passwd = scanner.next();
            userService.login(id, passwd);
            loginId = id;
            return;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    // 회원가입
    public void signIn() {
        printLine();

        String id;
        String passwd;
        String displayName;

        while (true) {
            System.out.print("아이디를 입력해주세요 >> ");
            id = scanner.next();
            if (id.isEmpty()) {
                System.out.println("아이디는 비어있을 수 없습니다.");
                continue;
            }

            System.out.print("비밀번호를 입력해주세요 >> ");
            passwd = scanner.next();
            if (passwd.isEmpty()) {
                System.out.println("비밀번호는 비어있을 수 없습니다.");
                continue;
            }

            System.out.print("닉네임을 설정해주세요 >> ");
            displayName = scanner.next();
            if (displayName.isEmpty()) {
                System.out.println("닉네임은 비어있을 수 없습니다.");
                continue;
            }

            try {
                userService.signIn(id, passwd, displayName);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                continue;
            }
            break;
        }

        printLine();
        System.out.println("정상적으로 가입되었습니다. ");
        System.out.printf("아이디\t : %s\n" +
                        "비밀번호\t : %s\n" +
                        "닉네임\t : %s\n",
                id, passwd, displayName);

        loginId = id;
    }

    public static void printLine() {
        System.out.println("=====================================");
    }

    /**
     * start부터 end사이의 정수를 입력받아 반환합니다.
     * @param start 시작값
     * @param end 끝 값
     * @return start 이상 end 이하의 사용자 입력 정수값
     */
    private int getInput(int start, int end) {
        int userCommand;
        while (true) {
            try {
                String input = scanner.next();
                userCommand = Integer.parseInt(input);
                if (userCommand >= start && userCommand <= end) {
                    return userCommand;
                }
                System.out.printf("%d~%d 사이의 정수를 입력해주세요. >> ", 1, end);
            } catch (Exception e) {
                System.out.print("숫자로 입력해주세요. >> ");
            }
        }
    }

    /**
     * 1부터 end사이의 정수를 입력받아 반환합니다.
     * @param end 마지막 정수값
     * @return 1 이상 end 이하의 사용자 입력 정수값
     */
    private int getInput(int end) {
        int userCommand;
        while (true) {
            try {
                String input = scanner.next();
                userCommand = Integer.parseInt(input);
                if (userCommand >= 1 && userCommand <= end) {
                    return userCommand;
                }
                System.out.printf("%d~%d 사이의 정수를 입력해주세요. >> ", 1, end);
            } catch (Exception e) {
                System.out.print("숫자로 입력해주세요. >> ");
            }
        }
    }


    private void init() {
//        // 1. 사용자 생성
//        userService.signIn("happy", "pancake", "heeyeon");
//        userService.signIn("jung123", "greeny", "garden");
//        userService.signIn("npnp9671", "something", "drawing");
//        userService.signIn("alice", "pass1234", "Alice");
//        userService.signIn("bob1234", "pass1234", "Bob");
//        userService.signIn("charlie", "pass1234", "Charlie");
//        userService.signIn("david12", "pass1234", "David");
//        userService.signIn("emma123", "pass1234", "Emma");
//
//        // 사용자 프로필 설정 (Bio 및 온라인 상태)
//        userService.setBio("happy", "게임 좋아하는 사람");
//        userService.setBio("jung123", "사진 찍는 걸 좋아해요");
//        userService.setBio("npnp9671", "그림 그리는 중");
//        userService.setBio("alice", "코딩하는 앨리스");
//        userService.setBio("bob1234", "음악 듣는 걸 좋아합니다");
//        userService.setBio("charlie", "운동 매니아");
//
//        // 일부 사용자를 온라인 상태로 변경
//        userService.setOnlineStatus("happy", User.Status.ONLINE);
//        userService.setOnlineStatus("alice", User.Status.ONLINE);
//        userService.setOnlineStatus("bob1234", User.Status.AWAY);
//        userService.setOnlineStatus("charlie", User.Status.DO_NOT_DISTURB);
//
//        // 2. 채널 생성
//        UUID gameChannel = channelService.createChannel("게임해요", ChannelType.VOICE, userService.getById("happy"));
//        UUID photoChannel = channelService.createChannel("사진 모으기", ChannelType.TEXT, userService.getById("jung123"));
//        UUID studyChannel = channelService.createChannel("스터디", ChannelType.TEXT, userService.getById("alice"));
//        UUID musicChannel = channelService.createChannel("음악 공유", ChannelType.VOICE, userService.getById("bob1234"));
//        UUID generalChannel = channelService.createChannel("일반 채팅", ChannelType.TEXT,
//                userService.getById("happy"), userService.getById("alice"));
//
//        // 3. 사용자를 채널에 등록
//        // 게임해요 채널
//        channelService.addMember(gameChannel, userService.getById("happy"));
//        channelService.addMember(gameChannel, userService.getById("alice"));
//        channelService.addMember(gameChannel, userService.getById("bob1234"));
//        channelService.addMember(gameChannel, userService.getById("charlie"));
//
//        // 사진 모으기 채널
//        channelService.addMember(photoChannel, userService.getById("jung123"));
//        channelService.addMember(photoChannel, userService.getById("npnp9671"));
//        channelService.addMember(photoChannel, userService.getById("emma123"));
//        channelService.addMember(photoChannel, userService.getById("alice"));
//
//        // 스터디 채널
//        channelService.addMember(studyChannel, userService.getById("alice"));
//        channelService.addMember(studyChannel, userService.getById("bob1234"));
//        channelService.addMember(studyChannel, userService.getById("charlie"));
//        channelService.addMember(studyChannel, userService.getById("david12"));
//
//        // 음악 공유 채널
//        channelService.addMember(musicChannel, userService.getById("bob1234"));
//        channelService.addMember(musicChannel, userService.getById("alice"));
//        channelService.addMember(musicChannel, userService.getById("happy"));
//
//        // 일반 채팅 채널 (모든 사용자)
//        channelService.addMember(generalChannel, userService.getById("happy"));
//        channelService.addMember(generalChannel, userService.getById("alice"));
//        channelService.addMember(generalChannel, userService.getById("jung123"));
//        channelService.addMember(generalChannel, userService.getById("npnp9671"));
//        channelService.addMember(generalChannel, userService.getById("bob1234"));
//        channelService.addMember(generalChannel, userService.getById("charlie"));
//        channelService.addMember(generalChannel, userService.getById("david12"));
//        channelService.addMember(generalChannel, userService.getById("emma123"));

//        // 4. 사용자 간 다이렉트 메시지 생성 (다양한 시간대)
//        // 3일 전 오전 9시
//        messageService.sendMessage(
//                userService.getById("alice"),
//                userService.getById("bob1234"),
//                "안녕하세요 Bob! 오늘 스터디 참여하시나요?"
//        );
//        messageService.getLastMessage().setCreatedAt(
//                LocalDateTime.now().minusDays(3).withHour(9).withMinute(0)
//                        .atZone(ZoneId.systemDefault()).toEpochSecond()
//        );
//
//        // 3일 전 오전 9시 15분
//        messageService.sendMessage(
//                userService.getById("bob1234"),
//                userService.getById("alice"),
//                "네! 3시에 뵙겠습니다."
//        );
//        messageService.getLastMessage().setCreatedAt(
//                LocalDateTime.now().minusDays(3).withHour(9).withMinute(15)
//                        .atZone(ZoneId.systemDefault()).toEpochSecond()
//        );
//
//        // 2일 전 저녁 6시
//        messageService.sendMessage(
//                userService.getById("happy"),
//                userService.getById("charlie"),
//                "오늘 저녁에 게임 한판 할래요?"
//        );
//        messageService.getLastMessage().setCreatedAt(
//                LocalDateTime.now().minusDays(2).withHour(18).withMinute(30)
//                        .atZone(ZoneId.systemDefault()).toEpochSecond()
//        );
//
//        // 2일 전 저녁 6시 45분
//        messageService.sendMessage(
//                userService.getById("charlie"),
//                userService.getById("happy"),
//                "좋아요! 8시에 들어갈게요."
//        );
//        messageService.getLastMessage().setCreatedAt(
//                LocalDateTime.now().minusDays(2).withHour(18).withMinute(45)
//                        .atZone(ZoneId.systemDefault()).toEpochSecond()
//        );
//
//        // 1일 전 오후 2시
//        messageService.sendMessage(
//                userService.getById("jung123"),
//                userService.getById("npnp9671"),
//                "어제 찍은 사진 정말 예뻤어요!"
//        );
//        messageService.getLastMessage().setCreatedAt(
//                LocalDateTime.now().minusDays(1).withHour(14).withMinute(20)
//                        .atZone(ZoneId.systemDefault()).toEpochSecond()
//        );
//
//        // 오늘 오전 10시
//        messageService.sendMessage(
//                userService.getById("alice"),
//                userService.getById("david12"),
//                "이번 주 과제 진행 상황 어떠세요?"
//        );
//        messageService.getLastMessage().setCreatedAt(
//                LocalDateTime.now().withHour(10).withMinute(30)
//                        .atZone(ZoneId.systemDefault()).toEpochSecond()
//        );
//
//        // 오늘 오전 11시
//        messageService.sendMessage(
//                userService.getById("david12"),
//                userService.getById("alice"),
//                "거의 다 끝났어요. 내일 리뷰 부탁드려요."
//        );
//        messageService.getLastMessage().setCreatedAt(
//                LocalDateTime.now().withHour(11).withMinute(15)
//                        .atZone(ZoneId.systemDefault()).toEpochSecond()
//        );
//
//        // 5. 채널 메시지 생성 (다양한 시간대)
//        // 게임해요 채널 - 어제 저녁
//        messageService.sendMessage(
//                userService.getById("happy"),
//                channelService.getChannelById(gameChannel),
//                "안녕하세요! 이번 주말에 같이 게임 할 분 계신가요?"
//        );
//        messageService.getLastMessage().setCreatedAt(
//                LocalDateTime.now().minusDays(1).withHour(20).withMinute(0)
//                        .atZone(ZoneId.systemDefault()).toEpochSecond()
//        );
//
//        messageService.sendMessage(
//                userService.getById("alice"),
//                channelService.getChannelById(gameChannel),
//                "저 참여하고 싶어요!"
//        );
//        messageService.getLastMessage().setCreatedAt(
//                LocalDateTime.now().minusDays(1).withHour(20).withMinute(5)
//                        .atZone(ZoneId.systemDefault()).toEpochSecond()
//        );
//
//        messageService.sendMessage(
//                userService.getById("charlie"),
//                channelService.getChannelById(gameChannel),
//                "저도요! 몇 시에 시작하나요?"
//        );
//        messageService.getLastMessage().setCreatedAt(
//                LocalDateTime.now().minusDays(1).withHour(20).withMinute(12)
//                        .atZone(ZoneId.systemDefault()).toEpochSecond()
//        );
//
//        // 사진 모으기 채널 - 3일 전 오후
//        messageService.sendMessage(
//                userService.getById("jung123"),
//                channelService.getChannelById(photoChannel),
//                "오늘 찍은 풍경 사진 공유합니다~"
//        );
//        messageService.getLastMessage().setCreatedAt(
//                LocalDateTime.now().minusDays(3).withHour(15).withMinute(30)
//                        .atZone(ZoneId.systemDefault()).toEpochSecond()
//        );
//
//        messageService.sendMessage(
//                userService.getById("npnp9671"),
//                channelService.getChannelById(photoChannel),
//                "와 너무 예뻐요! 어디서 찍으셨나요?"
//        );
//        messageService.getLastMessage().setCreatedAt(
//                LocalDateTime.now().minusDays(3).withHour(15).withMinute(45)
//                        .atZone(ZoneId.systemDefault()).toEpochSecond()
//        );
//
//        messageService.sendMessage(
//                userService.getById("emma123"),
//                channelService.getChannelById(photoChannel),
//                "저도 내일 사진 찍으러 가려고요!"
//        );
//        messageService.getLastMessage().setCreatedAt(
//                LocalDateTime.now().minusDays(3).withHour(16).withMinute(10)
//                        .atZone(ZoneId.systemDefault()).toEpochSecond()
//        );
//
//        // 스터디 채널 - 어제 오후
//        messageService.sendMessage(
//                userService.getById("alice"),
//                channelService.getChannelById(studyChannel),
//                "이번 주 스터디 주제는 제네릭입니다."
//        );
//        messageService.getLastMessage().setCreatedAt(
//                LocalDateTime.now().minusDays(1).withHour(14).withMinute(0)
//                        .atZone(ZoneId.systemDefault()).toEpochSecond()
//        );
//
//        messageService.sendMessage(
//                userService.getById("bob1234"),
//                channelService.getChannelById(studyChannel),
//                "자료 준비해서 공유하겠습니다."
//        );
//        messageService.getLastMessage().setCreatedAt(
//                LocalDateTime.now().minusDays(1).withHour(14).withMinute(20)
//                        .atZone(ZoneId.systemDefault()).toEpochSecond()
//        );
//
//        messageService.sendMessage(
//                userService.getById("david12"),
//                channelService.getChannelById(studyChannel),
//                "질문 있으면 언제든지 물어보세요!"
//        );
//        messageService.getLastMessage().setCreatedAt(
//                LocalDateTime.now().minusDays(1).withHour(14).withMinute(35)
//                        .atZone(ZoneId.systemDefault()).toEpochSecond()
//        );
//
//        // 음악 공유 채널 - 2일 전 아침
//        messageService.sendMessage(
//                userService.getById("bob1234"),
//                channelService.getChannelById(musicChannel),
//                "요즘 듣는 노래 추천해드릴게요!"
//        );
//        messageService.getLastMessage().setCreatedAt(
//                LocalDateTime.now().minusDays(2).withHour(8).withMinute(30)
//                        .atZone(ZoneId.systemDefault()).toEpochSecond()
//        );
//
//        messageService.sendMessage(
//                userService.getById("happy"),
//                channelService.getChannelById(musicChannel),
//                "감사합니다! 들어볼게요~"
//        );
//        messageService.getLastMessage().setCreatedAt(
//                LocalDateTime.now().minusDays(2).withHour(9).withMinute(15)
//                        .atZone(ZoneId.systemDefault()).toEpochSecond()
//        );
//
//        // 일반 채팅 채널 - 오늘 다양한 시간
//        messageService.sendMessage(
//                userService.getById("happy"),
//                channelService.getChannelById(generalChannel),
//                "모두들 안녕하세요! 반갑습니다."
//        );
//        messageService.getLastMessage().setCreatedAt(
//                LocalDateTime.now().withHour(8).withMinute(0)
//                        .atZone(ZoneId.systemDefault()).toEpochSecond()
//        );
//
//        messageService.sendMessage(
//                userService.getById("alice"),
//                channelService.getChannelById(generalChannel),
//                "환영합니다! 즐거운 시간 되세요~"
//        );
//        messageService.getLastMessage().setCreatedAt(
//                LocalDateTime.now().withHour(9).withMinute(30)
//                        .atZone(ZoneId.systemDefault()).toEpochSecond()
//        );
//
//        messageService.sendMessage(
//                userService.getById("charlie"),
//                channelService.getChannelById(generalChannel),
//                "오늘 날씨 정말 좋네요!"
//        );
//        messageService.getLastMessage().setCreatedAt(
//                LocalDateTime.now().withHour(12).withMinute(45)
//                        .atZone(ZoneId.systemDefault()).toEpochSecond()
//        );
//
//        messageService.sendMessage(
//                userService.getById("emma123"),
//                channelService.getChannelById(generalChannel),
//                "네! 산책하기 좋은 날씨에요."
//        );
//        messageService.getLastMessage().setCreatedAt(
//                LocalDateTime.now().withHour(13).withMinute(10)
//                        .atZone(ZoneId.systemDefault()).toEpochSecond()
//        );
    }

}
