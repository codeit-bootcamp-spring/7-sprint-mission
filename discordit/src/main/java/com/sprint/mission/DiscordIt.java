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

    private String userId; // 현재 로그인한 유저 아이디

    public void start() {
        // 초기에 사용할 데이터들을 모아놓는 곳
        System.out.println("=========== discordit ==============");
        run();
    }

    public void run() {
        init();

        userLogin();

        channel();

    }

    private void channel() {
        do {
            System.out.println("채널 조회");
            System.out.print("1. 가입 채널 목록 2. 전체 채널 목록 \n3. 채널 들어가기 4. 채널 만들기 0. 돌아가기 >>");
            getInput(0, 3);
            switch (userCommand) {
                case 1 -> printRegisteredChannel();
                case 2 -> printAllChannel();
                case 3 -> registerChannel();
                case 4 -> createChannel();
            }
        } while (userCommand != 0);
    }

    private void printRegisteredChannel() {
        List<UUID> channels = channelService.getRegisteredChannels(userService.getUserById(userId));

        if(channels.isEmpty()){
            System.out.println("가입된 채널이 없습니다.");
            return;
        }

        System.out.println("현재 가입된 채널 : ");
        printChannel(channels);
    }

    private void registerChannel() {
        List<UUID> channels = channelService.getNotRegisteredChannels(userService.getUserById(userId));

        if(channels.isEmpty()){
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
        System.out.printf("채널 [%s]에 가입되었습니다!", channelService.getChannelById(registered).getDisplayName());
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
        if (allChannel.isEmpty()){
            System.out.println("현재 개설된 채널이 없습니다.");
        }
        System.out.println("전체 채널 목록 : ");
        for (UUID uuid : allChannel) {
            Channel channel = channelService.getChannelById(uuid);
            System.out.printf("\t- %s \t\t---\t%s\n", channel.getDisplayName(), channel.getType());
        }
    }

    private void printChannel(List<UUID> uuids) {
        printLine();

        for (int i = 0; i < uuids.size(); i++) {
            Channel channel = channelService.getChannelById(uuids.get(i));
            System.out.printf("\t%d. \t\t%s \t(%s)\n", i, channel.getDisplayName(), channel.getType());
        }

//        System.out.print("몇 번째 채널에 접속하시겠습니까? >>");
//        getInput(0, registeredChannel.size());
//        if (userCommand == 0) {
//            System.out.println("이전으로 돌아갑니다.");
//            return;
//        } else {
//            enterChannel(registeredChannel.get(userCommand));
//        }
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
                case 3 ->
                {
                    return;
                }
            }
        }

    }

    private void sendChannelMessage(UUID uuid) {
        User user = userService.getUserById(userId);
        Channel channel = channelService.getChannelById(uuid);
        System.out.print("메세지 입력(종료하려면 -를 입력) : ");
        while(true) {
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


    private void getInput(int start, int end) {
        userCommand = 0;
        while (true) {
            try {
                String input = scanner.next();
                userCommand = Integer.parseInt(input);
                if(userCommand >= start && userCommand <= end){
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
                if(userCommand >= 1 && userCommand <= end){
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
