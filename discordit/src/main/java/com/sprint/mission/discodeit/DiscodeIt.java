package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.channel.response.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.message.GetMessageDto;
import com.sprint.mission.discodeit.dto.message.request.SendMessageDto;
import com.sprint.mission.discodeit.dto.message.response.MessageResponse;
import com.sprint.mission.discodeit.dto.user.request.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.request.UserUpdateRequestDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.enums.ChannelScope;
import com.sprint.mission.discodeit.enums.ChannelType;
import com.sprint.mission.discodeit.enums.OnlineStatus;
import com.sprint.mission.discodeit.enums.ReceiverType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class DiscodeIt {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    private final UserService userService;
    private final ChannelService channelService;
    private final MessageService messageService;
    private final BinaryContentService binaryContentService;

    private final Scanner scanner = new Scanner(System.in);
    private boolean exitFlag = false;

    private String loginId; // 현재 로그인한 유저 아이디

    public void start() {
        System.out.println("=========== discordit ==============");
        run();
    }

    public void run() {
        while (!exitFlag) {
            if (loginId == null) userLogin();
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
        System.out.println("1. 메세지 조회");
        messageSearch();
    }

    private void messageSearch() {
        printLine();
        List<MessageResponse> messageList = new ArrayList<>();
        System.out.println("[메세지 조회]");
        System.out.println("1. 발신자 기준");
        System.out.println("2. 발신자+수신자 기준");
        System.out.println("3. 채널 기준");
        switch (getInput(4)) {
            case 1 -> {
                System.out.print("발신자 아이디: ");
                String userId = scanner.next();
                messageList = messageService.get(new GetMessageDto(userId, null, null));
            }
            case 2 -> {
                System.out.print("발신자 아이디: ");
                String senderId = scanner.next();
                System.out.print("수신 타입(1:USER, 2:CHANNEL): ");
                int t = getInput(1, 2);
                ReceiverType type = (t == 1) ? ReceiverType.USER : ReceiverType.CHANNEL;
                System.out.print("수신자 아이디(채널은 UUID): ");
                String receiverId = scanner.next();
                messageList = messageService.get(new GetMessageDto(senderId, type, receiverId));
            }
            case 3 -> {
                List<UUID> uuids = channelRepository.findAll().stream()
                        .map(Channel::getUuid)
                        .toList();
                if (uuids.isEmpty()) {
                    System.out.println("채널이 없습니다.");
                    return;
                }
                UUID receiverId = displayChannelAndSelect(uuids);
                messageList = messageService.get(new GetMessageDto(null, ReceiverType.CHANNEL, receiverId.toString()));
            }
            case 4 -> {
                System.out.print("수신자 아이디: ");
                String recv = scanner.next();
                messageList = messageService.get(new GetMessageDto(null, ReceiverType.USER, recv));
            }
        }
        for (MessageResponse message : messageList) {
            display(message);
        }
    }

    private void display(Message message) {
        System.out.printf("[%s] -> [%s] \n%s (%s)\n",
                message.getSender().getUserId(),
                message.getReceiver().getDisplayName(),
                message.getMessage(),
                message.getFormattedCreationTime());
    }

    private void display(MessageResponse message) {
        System.out.printf("[%s] -> [%s] \n%s\n",
                message.senderId(),
                message.receiverId(),
                message.message());
    }

    private void user() {
        printLine();
        System.out.println("[회원]");
        while (true) {
            System.out.println("1. 내정보 조회/수정 2. 온라인 회원 조회 3. 메세지 보내기 4. 전체 회원 조회 0. 종료");
            switch (getInput(0, 7)) {
                case 1 -> manageMyProfile();
                case 2 -> printOnlineUsers();
                case 3 -> sendDirectMessage();
                case 4 -> printAllUsers();
                case 5 -> deleteMyAccount();
                case 6 -> changeMyEmail();
                case 7 -> listMyBinaryContents();
                case 0 -> { return; }
            }
        }
    }

    private void printAllUsers() {
        printUserDetails(userService.getAllUsers());
    }

    private void printOnlineUsers() {
        List<UserResponseDto> onlineUsers = userService.getOnlineUsers();
        System.out.println("현재 접속 중인 유저들: ");
        printUserDetails(onlineUsers);
    }

    private void printUserDetails(List<UserResponseDto> users) {
        for (int i = 0; i < users.size(); i++) {
            UserResponseDto u = users.get(i);
            System.out.printf("-\t%d.[%s]\t%s(%s) \"%s\"\n",
                    i + 1,
                    u.userId(),
                    u.displayName(),
                    u.onlineStatus(),
                    u.bio() == null ? "" : u.bio());
        }
    }

    private void sendDirectMessage() {
        List<UserResponseDto> onlineUsers = userService.getOnlineUsers();
        if (onlineUsers.isEmpty()) {
            System.out.println("온라인 사용자가 없습니다.");
            return;
        }
        System.out.println("현재 접속 중인 유저들: ");
        printUserDetails(onlineUsers);
        System.out.print("누구에게 메세지를 보낼까요? >> ");

        UserResponseDto receiver = onlineUsers.get(getInput(onlineUsers.size()) - 1);

        String msg;
        while (true) {
            System.out.print("메세지 (종료는 0) : ");
            msg = scanner.next();
            if ("0".equals(msg)) return;
            List<String> files = promptFileUrls();
            messageService.send(new SendMessageDto(
                    loginId,
                    ReceiverType.USER,
                    receiver.userId(),
                    msg,
                    files
            ));
            display(messageService.getLastMessage());
        }
    }

    private void manageMyProfile() {
        printLine();
        System.out.println("[내정보]");
        UserResponseDto me = userService.getByUserId(loginId);
        System.out.printf("아이디: %s\n닉네임: %s\n소개말: %s\n현재 상태: %s\n",
                me.userId(), me.displayName(), me.bio(), me.onlineStatus());
        System.out.println();

        System.out.print("1. 프로필 수정하기 0. 돌아가기 >> ");
        if (getInput(0, 1) == 0) return;
        System.out.println("변경할 것을 선택해주세요.");
        System.out.print("1. 비밀번호 2. 닉네임 3. 소개말 4. 상태 >> ");

        switch (getInput(4)) {
            case 1 -> {
                System.out.print("현재 비밀번호 >> ");
                String passWd = scanner.next();
                try {
                    userService.login(loginId, passWd); // 검증
                } catch (Exception e) {
                    System.out.println("비밀번호가 올바르지 않습니다.");
                    return;
                }
                System.out.print("새 비밀번호 >> ");
                String newPw = scanner.next();
                userService.update(new UserUpdateRequestDto(loginId, newPw, null, null, null, null, null));
            }
            case 2 -> {
                System.out.print("변경할 닉네임 >> ");
                userService.update(new UserUpdateRequestDto(loginId, null, scanner.next(), null, null, null, null));
            }
            case 3 -> {
                System.out.print("변경할 소개말 >> ");
                userService.update(new UserUpdateRequestDto(loginId, null, null, null, scanner.next(), null, null));
            }
            case 4 -> {
                System.out.print("변경할 상태를 선택해주세요. ");
                OnlineStatus[] userStatuses = OnlineStatus.values();
                for (int i = 0; i < userStatuses.length; i++) {
                    System.out.printf("\t%d. \t\t%s ", i + 1, userStatuses[i]);
                }
                OnlineStatus st = userStatuses[getInput(userStatuses.length) - 1];
                userService.update(new UserUpdateRequestDto(loginId, null, null, null, null, st, null));
            }
        }
        System.out.println("정상적으로 변경되었습니다.");
    }

    private void logout() {
        System.out.println("로그아웃 합니다..");
        userService.update(new UserUpdateRequestDto(loginId, null, null, null, null, OnlineStatus.OFFLINE, null));
        loginId = null;
    }

    private void channel() {
        int userCommand;
        do {
            printLine();
            System.out.println("채널 메뉴");
            System.out.print("1. 가입채널 목록 2. 전체 채널 목록 3. 채널 접속하기 \n4. 채널 가입 5. 채널 나가기\n6. 채널 만들기 0. 돌아가기\n>> ");
            userCommand = getInput(0, 8);
            switch (userCommand) {
                case 1 -> printRegisteredChannel();
                case 2 -> printAllChannels();
                case 3 -> enterChannel();
                case 4 -> registerChannel();
                case 5 -> leaveChannel();
                case 6 -> createChannel();
                case 7 -> manageChannel();
                case 8 -> createPrivateChannel();
            }
        } while (userCommand != 0);
    }

    private void printAllChannels() {
        List<UUID> allChannel = channelRepository.findAll().stream()
                .map(Channel::getUuid)
                .toList();
        if (allChannel.isEmpty()) {
            System.out.println("현재 개설된 채널이 없습니다.");
            return;
        }
        System.out.println("전체 채널 목록 : ");
        printChannelDetails(allChannel);
    }

    private void leaveChannel() {
        printLine();
        List<UUID> registeredChannels = channelRepository.findAll().stream()
                .filter(c -> c.getMembers().stream().anyMatch(u -> u.getUserId().equals(loginId)))
                .map(Channel::getUuid)
                .toList();
        if (registeredChannels.isEmpty()) {
            System.out.println("가입된 채널이 없습니다.");
            return;
        }
        System.out.print("나갈 채널을 선택해주세요. >> ");
        UUID channel = displayChannelAndSelect(registeredChannels);
        channelService.deleteMember(channel, userRepository.findByUserId(loginId));
        System.out.println("채널에서 나왔습니다.");
    }

    private void enterChannel() {
        printLine();
        List<UUID> channels = channelRepository.findAll().stream()
                .filter(c -> c.getMembers().stream().anyMatch(u -> u.getUserId().equals(loginId)))
                .map(Channel::getUuid)
                .toList();
        if (channels.isEmpty()) {
            System.out.println("가입된 채널이 없습니다.");
            return;
        }
        printNumberedChannels(channels);
        System.out.print("접속할 채널을 선택해주세요. (0 : 취소) >> ");
        UUID selected = channels.get(getInput(0, channels.size()) - 1);
        enterChannelInternal(selected);
    }

    private void enterChannelInternal(UUID uuid) {
        printLine();
        ChannelResponseDto channel = channelService.getById(uuid);
        while (true) {
            System.out.printf("채널 [%s]에 접속했습니다\n1. 사용자 보기 2. 메세지 보내기 3. 뒤로가기 >> ", channel.channelName());
            switch (getInput(3)) {
                case 1 -> printAllRegisteredUser(uuid);
                case 2 -> sendChannelMessage(uuid);
                case 3 -> { return; }
            }
        }
    }

    private void sendChannelMessage(UUID uuid) {
        System.out.print("메세지 입력(종료는 -) : ");
        while (true) {
            String message = scanner.next();
            if ("-".equals(message)) return;
            List<String> files = promptFileUrls();
            messageService.send(new SendMessageDto(
                    loginId,
                    ReceiverType.CHANNEL,
                    uuid.toString(),
                    message,
                    files
            ));
            display(messageService.getLastMessage());
        }
    }

    private void printRegisteredChannel() {
        printLine();
        List<UUID> channels = channelRepository.findAll().stream()
                .filter(c -> c.getMembers().stream().anyMatch(u -> u.getUserId().equals(loginId)))
                .map(Channel::getUuid)
                .toList();
        if (channels.isEmpty()) {
            System.out.println("가입된 채널이 없습니다.");
            return;
        }
        printNumberedChannels("현재 가입된 채널 : ", channels);
    }

    private void registerChannel() {
        List<UUID> notJoined = channelRepository.findAll().stream()
            .filter(c -> c.getMembers().stream().noneMatch(u -> u.getUserId().equals(loginId)))
            .map(Channel::getUuid)
            .toList();
        if (notJoined.isEmpty()) {
            System.out.println("가입할 수 있는 채널이 없습니다.");
            return;
        }
        UUID selectedChannel = displayChannelAndSelect("가입되지 않은 채널 : ", notJoined);
        channelService.addMember(selectedChannel, userRepository.findByUserId(loginId));
        ChannelResponseDto dto = channelService.getById(selectedChannel);
        System.out.printf("채널 [%s]이(가) 가입되었습니다!\n", dto.channelName());
    }

    private void createChannel() {
        printLine();
        System.out.print("채널 이름 >> ");
        String channelName = scanner.next();
        System.out.print("채널 타입(1:TEXT, 2:VOICE 등) >> ");
        ChannelType[] types = ChannelType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.printf("%d. %s ", i + 1, types[i]);
        }
        System.out.println();
        ChannelType type = types[getInput(types.length) - 1];

        channelService.createPublicChannel(new com.sprint.mission.discodeit.dto.channel.request.PublicChannelCreateRequestDto(
                channelName,
                ChannelScope.PUBLIC,
                type,
                null,
                Set.of(loginId),
                Set.of(loginId)
        ));
        System.out.println("채널이 생성되었습니다.");
    }

    private void printChannelDetails(List<UUID> uuids) {
        printLine();
        for (UUID uuid : uuids) {
            ChannelResponseDto channel = channelService.getById(uuid);
            System.out.printf("\t- %s \t\t---\t%s\n", channel.channelName(), channel.type());
        }
    }

    private void printAllRegisteredUser(UUID uuid) {
        List<UserResponseDto> users = channelService.getAllMembers(uuid);
        for (UserResponseDto u : users) {
            System.out.printf("\t- %s(%s)\n", u.displayName(), u.onlineStatus());
        }
    }

    private void printNumberedChannels(List<UUID> uuids) {
        for (int i = 0; i < uuids.size(); i++) {
            ChannelResponseDto channel = channelService.getById(uuids.get(i));
            System.out.printf("\t%d. \t\t%s \t(%s)\n", i + 1, channel.channelName(), channel.type());
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

    private UUID displayChannelAndSelect(String message, List<UUID> uuids) {
        System.out.println(message);
        return displayChannelAndSelect(uuids);
    }

    private void deleteMyAccount() {
        System.out.print("정말 삭제하시겠습니까? (y/N) >> ");
        String ans = scanner.next();
        if (!ans.equalsIgnoreCase("y")) return;
        userService.deleteByUserId(loginId);
        System.out.println("계정이 삭제되었습니다. 로그아웃합니다.");
        loginId = null;
    }

    private void changeMyEmail() {
        System.out.print("새 이메일 >> ");
        String email = scanner.next();
        userService.update(new UserUpdateRequestDto(loginId, null, null, email, null, null, null));
        System.out.println("이메일이 변경되었습니다.");
    }

    private void listMyBinaryContents() {
        List<BinaryContentResponse> list = binaryContentService.getAllByUserID(loginId);
        if (list.isEmpty()) {
            System.out.println("등록된 첨부파일이 없습니다.");
            return;
        }
        for (BinaryContentResponse b : list) {
            System.out.printf("- %s (%s)\n", b.fileUrl(), b.id());
        }
    }

    private List<String> promptFileUrls() {
        System.out.print("첨부 URL들(쉼표 구분, 없으면 -) >> ");
        String line = scanner.next();
        if ("-".equals(line)) return null;
        String[] arr = line.split(",");
        List<String> list = new ArrayList<>();
        for (String s : arr) {
            String t = s.trim();
            if (!t.isEmpty()) list.add(t);
        }
        return list.isEmpty() ? null : list;
    }

    private Set<String> parseIdsOrDefault(String line, String def) {
        if (line == null || line.isBlank() || "-".equals(line)) return Set.of(def);
        String[] arr = line.split(",");
        Set<String> out = new HashSet<>();
        for (String s : arr) {
            String t = s.trim();
            if (!t.isEmpty()) out.add(t);
        }
        if (out.isEmpty()) out.add(def);
        return out;
    }

    private void createPrivateChannel() {
        printLine();
        System.out.print("채널 타입(1:TEXT, 2:VOICE 등) >> ");
        ChannelType[] types = ChannelType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.printf("%d. %s ", i + 1, types[i]);
        }
        System.out.println();
        ChannelType type = types[getInput(types.length) - 1];
        System.out.print("멤버 아이디들(쉼표 구분, 비우면 본인만) >> ");
        String membersLine = scanner.next();
        Set<String> members = parseIdsOrDefault(membersLine, loginId);
        System.out.print("관리자 아이디들(쉼표 구분, 비우면 본인만) >> ");
        String moderatorsLine = scanner.next();
        Set<String> moderators = parseIdsOrDefault(moderatorsLine, loginId);
        channelService.createPrivateChannel(new com.sprint.mission.discodeit.dto.channel.request.PrivateChannelCreateRequestDto(
                ChannelScope.PRIVATE,
                type,
                moderators,
                members
        ));
        System.out.println("비공개 채널이 생성되었습니다.");
    }

    private void manageChannel() {
        List<UUID> channels = channelRepository.findAll().stream()
                .filter(c -> c.getModerators().stream().anyMatch(u -> u.getUserId().equals(loginId))
                        || c.getMembers().stream().anyMatch(u -> u.getUserId().equals(loginId)))
                .map(Channel::getUuid)
                .toList();
        if (channels.isEmpty()) {
            System.out.println("관리 가능한 채널이 없습니다.");
            return;
        }
        UUID chId = displayChannelAndSelect("관리할 채널을 선택하세요:", channels);
        while (true) {
            System.out.print("1. 이름/설명 수정 2. 멤버 추가 3. 멤버 제거 4. 관리자 추가 5. 관리자 제거 6. 채널 삭제 0. 뒤로 >> ");
            switch (getInput(0, 6)) {
                case 1 -> {
                    System.out.print("새 이름(건너뛰기: -) >> ");
                    String name = scanner.next();
                    System.out.print("새 설명(건너뛰기: -) >> ");
                    String desc = scanner.next();
                    String newName = "-".equals(name) ? null : name;
                    String newDesc = "-".equals(desc) ? null : desc;
                    channelService.update(new com.sprint.mission.discodeit.dto.channel.request.ChannelUpdateRequestDto(chId, newName, newDesc));
                    System.out.println("수정했습니다.");
                }
                case 2 -> {
                    System.out.print("추가할 멤버 아이디 >> ");
                    String uid = scanner.next();
                    channelService.addMember(chId, userRepository.findByUserId(uid));
                    System.out.println("추가했습니다.");
                }
                case 3 -> {
                    System.out.print("제거할 멤버 아이디 >> ");
                    String uid = scanner.next();
                    channelService.deleteMember(chId, userRepository.findByUserId(uid));
                    System.out.println("제거했습니다.");
                }
                case 4 -> {
                    System.out.print("추가할 관리자 아이디 >> ");
                    String uid = scanner.next();
                    channelService.addModerator(chId, userRepository.findByUserId(uid));
                    System.out.println("추가했습니다.");
                }
                case 5 -> {
                    System.out.print("제거할 관리자 아이디 >> ");
                    String uid = scanner.next();
                    channelService.deleteModerator(chId, userRepository.findByUserId(uid));
                    System.out.println("제거했습니다.");
                }
                case 6 -> {
                    System.out.print("정말 삭제하시겠습니까? (y/N) >> ");
                    String ans = scanner.next();
                    if (ans.equalsIgnoreCase("y")) {
                        channelService.delete(chId);
                        System.out.println("삭제했습니다.");
                        return;
                    }
                }
                case 0 -> { return; }
            }
        }
    }

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
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void signIn() {
        printLine();
        String id;
        String passwd;
        String email;
        String displayName;

        while (true) {
            System.out.print("아이디를 입력해주세요 >> ");
            id = scanner.next();
            if (id.isEmpty()) { System.out.println("아이디는 비어있을 수 없습니다."); continue; }

            System.out.print("비밀번호를 입력해주세요 >> ");
            passwd = scanner.next();
            if (passwd.isEmpty()) { System.out.println("비밀번호는 비어있을 수 없습니다."); continue; }

            System.out.print("이메일을 입력해주세요 >> ");
            email = scanner.next();
            if (email.isEmpty()) { System.out.println("이메일은 비어있을 수 없습니다."); continue; }

            System.out.print("닉네임을 설정해주세요 >> ");
            displayName = scanner.next();
            if (displayName.isEmpty()) { System.out.println("닉네임은 비어있을 수 없습니다."); continue; }

            try {
                userService.signIn(new UserCreateRequestDto(id, passwd, email, displayName, null));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                continue;
            }
            break;
        }

        printLine();
        System.out.println("정상적으로 가입되었습니다. ");
        System.out.printf("아이디\t: %s\n비밀번호\t: %s\n닉네임\t: %s\n", id, passwd, displayName);
        loginId = id;
    }

    private void userLogin() {
        while (loginId == null) {
            printLine();
            System.out.print("1. 로그인 2. 회원가입 >> ");
            switch (getInput(2)) {
                case 1 -> login();
                case 2 -> signIn();
            }
        }
        printLine();
        System.out.printf("%s님 환영합니다 \n", userService.getByUserId(loginId).displayName());
    }

    public static void printLine() {
        System.out.println("=====================================");
    }

    private int getInput(int start, int end) {
        int userCommand;
        while (true) {
            try {
                String input = scanner.next();
                userCommand = Integer.parseInt(input);
                if (userCommand >= start && userCommand <= end) {
                    return userCommand;
                }
                System.out.printf("%d~%d 사이의 수를 입력해주세요. >> ", start, end);
            } catch (Exception e) {
                System.out.print("숫자를 입력해주세요. >> ");
            }
        }
    }

    private int getInput(int end) {
        return getInput(1, end);
    }
}
