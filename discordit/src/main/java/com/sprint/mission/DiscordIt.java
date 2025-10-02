package com.sprint.mission;

import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.User;
import com.sprint.mission.service.jcf.JCFChannelService;
import com.sprint.mission.service.jcf.JCFMessageService;
import com.sprint.mission.service.jcf.JCFUserService;

import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

public class DiscordIt {

    public static final JCFMessageService<Channel> channelMessageService = new JCFMessageService<>();
    public static final JCFMessageService<User> userMessageService = new JCFMessageService<>();
    public static final JCFUserService userService = new JCFUserService();
    public static final JCFChannelService channelService = new JCFChannelService();

    private Scanner scanner = new Scanner(System.in);
    private int userCommand = 0;
    private boolean exitFlag = false;

    private String userId; // 현재 로그인한 유저 아이디

    public void start() {
        // 초기에 사용할 데이터들을 모아놓는 곳
        System.out.println("=========== discordit ==============");
        run();
    }

    public void run() {
        init(); // 초기 데이터 설정

        while (!exitFlag) {
            if (userId == null)
                userLogin(); // 시작시 로그인 함
            menu();
        }
    }


    private void menu() {
        printLine();
        System.out.println("[메뉴]");
        System.out.println("1. 채널 2. 회원 3. 로그아웃");
        getInput(3);
        switch (userCommand) {
            case 1 -> channel();
            case 2 -> user();
            case 3 -> logout();
        }
    }


    private void user() {
        printLine();
        System.out.println("[회원]");
        System.out.println("1. 내 정보 조회/수정 2. 온라인 회원 조회 3. 메세지 보내기 4. 전체 회원 조회");
        getInput(3);
        switch (userCommand) {
            case 1 -> manageMyProfile();
            case 2 -> printOnlineUsers();
            case 3 -> sendDirectMessage();
            case 4 -> printAllUsers();
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
            System.out.printf("-%d.\t\t[%s]%s(%s) %s\n",
                    i + 1,
                    nowId,
                    userService.getDisplayName(nowId),
                    userService.getOnlineStatus(nowId),
                    userService.getBio(nowId));
        }
    }

    private void sendDirectMessage() {
        List<String> onlineUsers = userService.getOnlineUsers();
        System.out.println("현재 접속 중인 유저들 : ");
        printUserDetails(onlineUsers);
        System.out.print("몇 번 유저에게 메세지를 보내시겠습니까? >> ");

        getInput(onlineUsers.size());


        String message;
        while(true){
            System.out.print("메세지 (종료는 0) : ");
            message = scanner.next();

            if(message.equals("0"))
                return;

            userMessageService.sendMessage(
                    userService.getUserById(userId),
                    userService.getUserById(onlineUsers.get(userCommand - 1)),
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
                userId,
                userService.getDisplayName(userId),
                userService.getBio(userId),
                userService.getOnlineStatus(userId));
        System.out.println();

        System.out.printf("1. 프로필 수정하기 0. 돌아가기 >> ");
        getInput(0, 1);
        if (userCommand == 0)
            return;
        System.out.println("변경할 것을 선택해주세요.");
        System.out.print("1. 비밀번호 2. 닉네임 3. 한마디 4. 현재상태 >>");
        getInput(5);



        switch (userCommand) {
            case 1 -> {
                System.out.print("현재 비밀번호를 입력해주세요 >> ");
                String passWd = scanner.next();

                int tried = 1;
                while (userService.validatePasswd(passWd)) {
                    System.out.println("비밀번호가 올바르지 않습니다.");
                    tried++;

                    if(tried >= 3) {
                        System.out.println("3번 연속으로 실패하였으므로 이전으로 돌아갑니다.");
                        return;
                    }

                    System.out.print("현재 비밀번호를 입력해주세요 >> ");
                    passWd = scanner.next();
                }
                System.out.print("새 비밀번호를 입력해주세요 >> ");
                passWd = scanner.next();
                userService.setPasswd(userId, passWd);
            }
            case 2 -> {
                System.out.print("변경 닉네임 >>");
                userService.setDisplayName(userId, scanner.next());
            }
            case 3 -> {
                System.out.print("변경 한마디 >>");
                userService.setBio(userId, scanner.next());
            }
            case 4 -> {
                System.out.print("변경할 상태를 선택해주세요. ");
                User.Status[] statuses = User.Status.values();
                for (int i = 0; i < statuses.length; i++) {
                    System.out.printf("\t%d. \t\t%s ", i + 1, statuses[i]);
                }
                getInput(statuses.length + 1);
                userService.setOnlineStatus(userId, statuses[userCommand - 1]);
            }
        }
        System.out.println("정상적으로 변경되었습니다.");
    }

    private void logout() {
        System.out.println("로그아웃 합니다...");
        userService.setOnlineStatus(userId, User.Status.OFFLINE);
        userId = null;
    }

    private void channel() {
        do {
            printLine();
            System.out.println("채널 조회");
            System.out.print("1. 가입 채널 목록 2. 전체 채널 목록 3. 채널 접속하기 4. 채널 등록하기 5. 채널 만들기 0. 돌아가기 \n>>");
            getInput(0, 5);
            switch (userCommand) {
                case 1 -> printRegisteredChannel();
                case 2 -> printAllChannel();
                case 3 -> enterChannel();
                case 4 -> registerChannel();
                case 5 -> createChannel();
            }
        } while (userCommand != 0);
    }

    private void enterChannel() {
        printLine();
        List<UUID> channels = channelService.getRegisteredChannels(userService.getUserById(userId));
        if (channels.isEmpty()) {
            System.out.println("가입된 채널이 없습니다.");
            return;
        }

        System.out.print("접속할 채널을 선택해주세요. (0 : 취소) >>");
        getInput(0, channels.size());

        channelMessageSend(channels.get(userCommand - 1));
    }

    private void channelMessageSend(UUID uuid) {
        User sender = userService.getUserById(userId);
        Channel receiver = channelService.getChannelById(uuid);
        String message;

        while (true) {
            System.out.print("메세지 입력 (0 : 종료) : ");
            message = scanner.next();

            if (message.equals("0"))
                return;
            channelMessageService.sendMessage(sender, receiver, message);
        }
    }

    private void printRegisteredChannel() {
        printLine();
        List<UUID> channels = channelService.getRegisteredChannels(userService.getUserById(userId));

        if (channels.isEmpty()) {
            System.out.println("가입된 채널이 없습니다.");
            return;
        }

        System.out.println("현재 가입된 채널 : ");
        printChannel(channels);
    }

    private void registerChannel() {
        List<UUID> channels = channelService.getNotRegisteredChannels(userService.getUserById(userId));

        if (channels.isEmpty()) {
            System.out.println("이미 모든 채널에 가입되어있습니다.");
            return;
        }

        System.out.println("가입되지 않은 채널 : ");
        printChannel(channels);

        System.out.print("몇 번째 채널에 가입하시겠습니까? (0 : 취소) \n>>");
        getInput(0, channels.size());
        if (userCommand == 0)
            return;

        UUID registered = channels.get(userCommand - 1);
        channelService.addMember(registered, userService.getUserById(userId));
        System.out.printf("채널 [%s]에 가입되었습니다!\n", channelService.getChannelById(registered).getDisplayName());
    }

    private void userLogin() {
        while (userId == null) {
            System.out.print("1. 로그인 2. 회원가입\n >> ");
            getInput(2);
            printLine();

            switch (userCommand) {
                case 1 -> login();
                case 2 -> signIn();
            }
        }

        printLine();
        System.out.printf("%s님, 환영합니다! \n", userService.getDisplayName(userId));
    }

    private void createChannel() {
        System.out.println("아직안만듦");
    }

    private void printAllChannel() {
        printLine();
        List<UUID> allChannel = channelService.getAllChannels();
        if (allChannel.isEmpty()) {
            System.out.println("현재 개설된 채널이 없습니다.");
        }
        System.out.println("전체 채널 목록 : ");
        for (UUID uuid : allChannel) {
            Channel channel = channelService.getChannelById(uuid);
            System.out.printf("\t- %s \t\t---\t%s\n", channel.getDisplayName(), channel.getType());
        }
    }

    /**
     * 주어진 채널 uuid 목록으로 각 채널에 대한 정보를 간단하게 출력함
     * 예시 : 1. 모각코     --- VOICE
     * @param uuids 채널 고유 uuid
     */
    private void printChannel(List<UUID> uuids) {
        printLine();

        for (int i = 0; i < uuids.size(); i++) {
            Channel channel = channelService.getChannelById(uuids.get(i));
            System.out.printf("\t%d. \t\t%s \t(%s)\n", i + 1, channel.getDisplayName(), channel.getType());
        }
    }


    private void enterChannel(UUID uuid) {
        printLine();
        Channel channel = channelService.getChannelById(uuid);
        while (true) {
            System.out.printf("채널 [%s]에 접속하였습니다.\n" +
                            "1. 전체 사용자 보기 2. 메세지 보내기 3. 뒤로 돌아가기 >>",
                    channel.getDisplayName());
            getInput(3);

            switch (userCommand) {
                case 1 -> printAllRegisteredUser(uuid);
                case 2 -> sendChannelMessage(uuid);
                case 3 -> {
                    return;
                }
            }
        }

    }

    private void sendChannelMessage(UUID uuid) {
        User user = userService.getUserById(userId);
        Channel channel = channelService.getChannelById(uuid);
        System.out.print("메세지 입력(종료하려면 -를 입력) : ");
        while (true) {
            String message = scanner.next();
            if (message.equals("-"))
                return;
            channelMessageService.sendMessage(user, channel, message);
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
            userId = id;
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
        String bio;

        while (true) {
            System.out.print("아이디를 입력해주세요 >> ");
            id = scanner.next();
            try {
                userService.isCreatableId(id);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                continue;
            }
            break;
        }

        while (true) {
            System.out.print("비밀번호를 입력해주세요 >> ");
            passwd = scanner.next();
            try {
                userService.validatePasswd(passwd);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                continue;
            }
            break;
        }

        while (true) {
            System.out.print("닉네임을 설정해주세요 >> ");
            displayName = scanner.next();
            if (displayName.isEmpty()) {
                System.out.println("닉네임은 비어있을 수 없습니다.");
                continue;
            }
            break;
        }

        userService.signIn(id, passwd, displayName);

        printLine();
        System.out.println("정상적으로 가입되었습니다. ");
        System.out.printf("아이디\t : %s\n" +
                        "비밀번호\t : %s\n" +
                        "닉네임\t : %s\n",
                id, passwd, displayName);

        userId = id;
    }

    public static void printLine() {
        System.out.println("=====================================");
    }


    /**
     * 유저 입력을 안전하게 받기 위한 메소드
     * @param start
     * @param end
     */
    private void getInput(int start, int end) {
        userCommand = 0;
        while (true) {
            try {
                String input = scanner.next();
                userCommand = Integer.parseInt(input);
                if (userCommand >= start && userCommand <= end) {
                    return;
                }
                System.out.printf("%d~%d 사이의 정수를 입력해주세요. >> ", 1, end);
            } catch (Exception e) {
                System.out.print("숫자로 입력해주세요. >> ");
            }
        }
    }

    private void getInput(int end) {
        userCommand = 0;
        while (true) {
            try {
                String input = scanner.next();
                userCommand = Integer.parseInt(input);
                if (userCommand >= 1 && userCommand <= end) {
                    return;
                }
                System.out.printf("%d~%d 사이의 정수를 입력해주세요. >> ", 1, end);
            } catch (Exception e) {
                System.out.print("숫자로 입력해주세요. >> ");
            }
        }
    }

    private void init() {
        userService.signIn("happy", "pancake", "heeyeon");
        userService.signIn("jung123", "greeny", "garden");
        userService.signIn("npnp9671", "something", "drawing");

        channelService.createChannel("게임해요", Channel.ChannelType.VOICE, userService.getUserById("happy"));
        channelService.createChannel("사진 모으기", Channel.ChannelType.TEXT, userService.getUserById("jung123"));
    }
}
