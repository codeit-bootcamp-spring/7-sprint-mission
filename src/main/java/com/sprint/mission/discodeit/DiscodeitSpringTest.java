package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.auth.request.LoginUserDto;
import com.sprint.mission.discodeit.dto.channel.request.*;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.response.PrivateChannelResponseDto;
import com.sprint.mission.discodeit.dto.message.request.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageRequestDto;
import com.sprint.mission.discodeit.dto.user.request.CreateUserRequestDto;
import com.sprint.mission.discodeit.dto.user.request.UpdateUserRequestDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.global.utils.Printer;
import com.sprint.mission.discodeit.global.utils.TestDataInitializer;
import com.sprint.mission.discodeit.global.utils.TimeConvert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DiscodeitSpringTest {
    private final UserService userService;
    private final ChannelService channelService;
    private final MessageService messageService;
    private final AuthService authService;

    private static Scanner sc = new Scanner(System.in);
    private UserResponseDto loginUser;
    boolean login = false;

    public void run() {
        //테스트를 위한 유저, 채널, 메시지 데이터 생성
        //File I/O 서비스 사용시 파일이 없다면 생성
        String filePath = "C:\\Users\\user\\Workspace\\codeit-sprint-bootcamp\\7-sprint-mission\\data";

        if (!new File(filePath + "\\user.sav").exists()) {
            TestDataInitializer.initialize(userService, channelService, messageService);
        }

        while(true) {
            try {
                Printer.printLine();
                System.out.println("메뉴");
                System.out.println("1. 로그인 2. 계정 생성 3. 어플리케이션 종료");
                System.out.print("입력: ");
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1 -> login();
                    case 2 -> createUser();
                    case 3 -> exit();
                    default -> System.out.println("메뉴에 있는 숫자만 입력하세요");
                }
            } catch (InputMismatchException e) {
                System.out.println("숫자를 입력해주세요.");
                sc.nextLine();
            }
        }
    }

    // 프로그램 종료
    private static void exit() {
        System.out.println("테스트를 종료합니다.");
        sc.close();
        System.exit(0);
    }

    private void createUser() {
        try {
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
            userService.create(new CreateUserRequestDto(name, nickName, email, phoneNum, newUserId, newPassword), null);
            System.out.println("계정이 생성되었습니다. 로그인 해주세요.");
        } catch (IllegalArgumentException e) { // 닉네임, 이메일, 전화번호, 비밀번호 필터링시 발생
            System.out.println(e.getMessage());
            System.out.println("메뉴로 돌아갑니다.");
        }
    }

    private void login() {
        System.out.println("로그인을 해주세요");
        System.out.print("아이디: ");
        String userId = sc.nextLine();
        System.out.print("비밀번호: ");
        String password = sc.nextLine();

        try {
            loginUser = authService.login(new LoginUserDto(userId, password));
            login = true;
            userMenu();
        } catch (IllegalArgumentException e) { // 잘못된 아이디 또는 비밀번호 입력시 발생
            System.out.println(e.getMessage());
            System.out.println("메뉴로 돌아갑니다.");
        }
    }

    private void userMenu() {
        while(login) { // 로그인 되어 있는 경우
            try {
                Printer.printLine();
                System.out.printf("환영합니다. %s님\n", loginUser.getNickName());
                System.out.println("1. 채팅 2. 채널 3. 내 정보 출력 4. 내 정보 수정 5. 계정 삭제 6. 로그아웃");
                System.out.print("입력: ");
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1 -> chatMenu();
                    case 2 -> channelMenu();
                    case 3 -> {
                        Printer.printLine();
                        Printer.printInfo(loginUser);
                        Printer.printLine();
                    }
                    case 4 -> updateInfo();
                    case 5 -> deleteAccount();
                    case 6 -> {
                        System.out.println("계정을 로그아웃합니다.");
                        login = false;
                    }
                    default -> System.out.println("메뉴에 있는 숫자만 입력해주세요.");
                }
            } catch (InputMismatchException e) {
                System.out.println("숫자를 입력해주세요.");
                sc.nextLine(); // 잘못된 입력 버퍼 비우기
            }
        }
    }

    private void chatMenu() {
        List<UserResponseDto> users = userService.findAll();
        users.removeIf(u -> loginUser.getId().equals(u.getId())); // 닉네임 출력을 위해 내 정보 리스트에서 삭제
        int choice;

        try {
            System.out.println("채팅을 할 닉네임을 골라주세요.");
            for (int i = 0; i < users.size(); i++) {
                System.out.printf("%d. ", i + 1);
                Printer.printChatLatest(messageService, loginUser, users.get(i)); // 제일 최근 메시지도 닉네임과 함께 출력
            }
            System.out.printf("%d. 이전 메뉴\n", users.size() + 1);

            System.out.println();
            System.out.print("입력: ");
            choice = sc.nextInt();
            sc.nextLine(); // 개행 제거

            if(choice < 1 || choice > (users.size() + 1)) {
                System.out.println("메뉴 내 숫자만 입력해주세요.\n이전 메뉴로 돌아갑니다.");
                return;
            } else if (choice == (users.size() + 1)) {
                return;
            }

            UserResponseDto receiver = users.get(choice - 1); // 선택된 유저 정보 저장
            Printer.printLine();

            List<Message> msgs = messageService.findBetweenUsers(loginUser.getId(), receiver.getId());
            Printer.printChatHistory(userService, loginUser, msgs); // 이전 채팅 내용 출력
            String input;

            while (true) {
                Printer.printHalfLine();
                System.out.println("채팅 입력중(채팅방을 갱신 하려면 1, 메시지 수정/삭제 메뉴는 2, 채팅방에서 나가려면 -1을 입력하세요.)");
                System.out.print("입력: ");
                input = sc.nextLine();

                if (input.equals("1") || input.equals("2")) {
                    if(input.equals("2")) { // 메시지 수정 또는 삭제 이후 채팅방 갱신
                        handleUserMessageAction(loginUser.getId(), receiver.getId());
                    }

                    msgs = messageService.findBetweenUsers(loginUser.getId(), receiver.getId());
                    Printer.printChatHistory(userService, loginUser, msgs);
                } else if (input.equals("-1")) break;
                else {
                    messageService.create(new CreateMessageRequestDto(loginUser.getId(), receiver.getId(), input, ReceiveType.USER), null);
                }
            }

        } catch (InputMismatchException e) {
            System.out.println("숫자를 입력해주세요.\n메뉴로 돌아갑니다.");
            sc.nextLine(); // 잘못된 입력 버퍼 비우기
        }
    }

    private void channelMenu() {
        int choice;

        while(true) {
            try {
                Printer.printLine();
                System.out.println("1. 채널 입장 2. 채널 생성 3. 이전 메뉴");
                System.out.print("입력: ");
                choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1 -> channelChoice();
                    case 2 -> createChannel();
                    case 3 -> { return; }
                    default -> System.out.println("메뉴에 있는 숫자만 입력해주세요.");
                }
            } catch (InputMismatchException e) {
                System.out.println("숫자를 입력해주세요.");
                sc.nextLine(); // 잘못된 입력 버퍼 비우기
            }
        }
    }

    private void channelChoice() {
        ChannelResponseDto channel;
        List<ChannelResponseDto> userChannels;
        int choice;

        while(true) {
            Printer.printLine();
            System.out.println("1. 모든 채널 조회 2. 비공개 채널 조회 3. 이전 메뉴");
            System.out.printf("입력: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch(choice) {
                case 1 -> {
                    userChannels = channelService.findAllByUserId(loginUser.getId());
                }
                case 2 -> userChannels = channelService.findPrivateByUserId(loginUser.getId());
                case 3 -> { return; }
                default -> {
                    System.out.println("메뉴 내 숫자만 입력해주세요.");
                    continue;
                }
            }

            if (userChannels.isEmpty()) { // 참여하고 있는 채널 여부 확인
                System.out.println("현재 속해있는 채널이 없습니다.");
                return;
            }

            Printer.printHalfLine();
            System.out.println("입장을 원하시는 채널을 선택해주세요.");

            for (int i = 0; i < userChannels.size(); i++) {
                channel = userChannels.get(i);
                Printer.printChannelInfo(channel, i);
            }
            System.out.printf("%d. 이전 메뉴\n", userChannels.size() + 1);

            try {
                System.out.print("입력: ");
                choice = sc.nextInt();
                sc.nextLine();

                if (choice < 1 || choice > (userChannels.size() + 1)) {
                    System.out.println("메뉴 내 숫자를 입력해주세요.");
                    continue;
                } else if (choice == (userChannels.size() + 1)) {
                    return; // 이전 메뉴로 돌아가기
                }

                UUID channelId = userChannels.get(choice - 1).getId();
                channelActionMenu(channelId);

            } catch (InputMismatchException e) {
                System.out.println("숫자를 입력해주세요.");
                sc.nextLine(); // 잘못된 입력 버퍼 비우기
            } catch (IllegalArgumentException e) { // channelService.getChannel에 잘못된 UUID 입력시 발생
                System.out.println(e.getMessage());
            }
        }
    }

    private void channelActionMenu(UUID channelId) {
        ChannelResponseDto channel;
        while (true) {
             channel = channelService.find(channelId);

            Printer.printLine();
            System.out.printf("%s에 입장하였습니다.\n", channel.getChannelName());
            boolean isAdmin = channel.getAdminId().equals(loginUser.getId()); // 로그인 유저가 채널 관리자인지 확인

            // 채널 타입에 따라 출력
            if (channel.getChannelType() == ChannelType.MESSAGE) {
                System.out.print("1. 채널 채팅 입장 ");
            } else if (channel.getChannelType() == ChannelType.VOICE) {
                System.out.print("1. 통화방 입장 ");
            }

            if(channel.getVisibility() == ChannelVisibility.PRIVATE) { // 비공개 채널의 경우
                System.out.print("2. 채널 멤버 조회 ");

                if (isAdmin) { // 관리자 전용 메뉴 출력
                    System.out.println("3. 채널 이름 변경 4. 채널 관리자 변경 5. 채널 내 멤버 삭제 6. 채널 삭제 7. 이전 메뉴");
                } else {
                    System.out.println("3. 채널 나가기 4. 이전 메뉴");
                }
            } else if(channel.getVisibility() == ChannelVisibility.PUBLIC){ // 공개 채널의 경우
                if (isAdmin) { // 관리자 전용 메뉴 출력
                    System.out.println("2. 채널 이름 변경 3. 채널 삭제 4. 이전 메뉴");
                } else {
                    System.out.println("2. 채널 나가기 3. 이전 메뉴");
                }
            }

            System.out.print("입력: ");

            try {
                int choice = sc.nextInt();
                sc.nextLine();

                // 공개채널과 비공개 채널 선택 숫자 동일하게 바꾸기 위함
                if(channel.getVisibility() == ChannelVisibility.PUBLIC && choice > 1) {
                    choice += 1;
                    if (isAdmin && choice >= 4) choice += 2;
                }

                if ((isAdmin && (choice < 1 || choice > 7)) || (!isAdmin && (choice < 1 || choice > 4))) {
                    System.out.println("메뉴 내 숫자를 입력해주세요.");
                    continue;
                }

                if (!isAdmin && choice == 3) choice = 8; //관리자가 아닌 유저가 채널 나가기 선택시 번호 변경
                else if (!isAdmin && choice == 4) choice = 7; //관리자 아닌 유저가 이전 메뉴 선택시 번호 변경

                switch (choice) {
                    case 1 -> joinChannelRoom(channel);
                    case 2 -> {
                        Printer.printHalfLine();
                        Printer.printChannelMember(userService, channelService, channel.getId());
                        Printer.printHalfLine();
                    }
                    case 3 -> renameChannel(channel.getId());
                    case 4 -> updateChannelAdmin(channel);
                    case 5 -> deleteChannelMember(channel);
                    case 6, 8 -> {
                        if (choice == 6) deleteChannel(channel); // 관리자가 채널 삭제시 메서드 실행
                        else leaveChannel(channel); // 관리자가 아닌 유저가 채널 나가기 선택시 메서드 실행

                        // 채널이 삭제되거나 채널을 나가게 되면 채널 선택 메뉴로 이동
                        if (channelService.isChannelUnavailableForUser(loginUser.getId(), channel.getId())){
                            System.out.println("채널 선택 메뉴로 돌아갑니다.");
                            return;
                        }
                    }
                    case 7 -> {
                        System.out.println("채널 선택 메뉴로 돌아갑니다.");
                        return;
                    }
                }

            } catch (InputMismatchException e) {
                System.out.println("숫자를 입력해주세요.");
                sc.nextLine(); // 잘못된 입력 버퍼 비우기
            }
        }
    }

    private void joinChannelRoom(ChannelResponseDto channel) {
        String input;

        //채팅방과 통화방 나누기
        if (channel.getChannelType() == ChannelType.MESSAGE) {
            Printer.printLine();
            System.out.println("채널 채팅 입장");
            Printer.printLine();

            UUID channelId = channel.getId();
            List<Message> channelMessages = messageService.findAllByChannelId(channelId);
            Printer.printChatHistory(userService, loginUser, channelMessages);
            while (true) {
                Printer.printHalfLine();
                System.out.println("채팅 입력중(채팅방을 갱신 하려면 1, 메시지 수정/삭제 메뉴는 2, 채팅방에서 나가려면 -1을 입력하세요.)");
                System.out.print("입력: ");
                input = sc.nextLine();

                if (input.equals("1") || input.equals("2")) {
                    if (input.equals("2")) { // 수정 또는 삭제 이후 채팅방 갱신
                        handleUserMessageAction(loginUser.getId(), channelId);
                    }

                    channelMessages = messageService.findAllByChannelId(channel.getId());
                    Printer.printChatHistory(userService, loginUser, channelMessages);
                } else if (input.equals("-1")) return;
                else {
                    messageService.create(new CreateMessageRequestDto(loginUser.getId(), channel.getId(), input, ReceiveType.CHANNEL), null);
                }
            }

        } else if (channel.getChannelType() == ChannelType.VOICE) {
            Printer.printLine();
            System.out.println("전화를 진행합니다. 끊으시려면 -1을 입력하세요");
            while(true) {
                System.out.print("입력: ");
                input = sc.nextLine();
                if (input.equals("-1")) return;
            }
        }
    }

    private void renameChannel(UUID channelId) {
        while (true) {
            try {
                System.out.println("채널 이름 변경을 선택하였습니다. 변경 이름 작성해주세요. 이전 메뉴로 가시려면 -1을 입력하세요");
                System.out.print("변경할 채널 이름: ");
                String newChannelName = sc.nextLine();
                if (!newChannelName.equals("-1")) {
                    channelService.updateName(channelId, new UpdateChannelNameRequestDto(loginUser.getId(), newChannelName));
                    System.out.printf("%s로 채널 이름이 변경되었습니다.\n", newChannelName);
                }
                System.out.println("이전 메뉴로 돌아갑니다.");
                break;
            } catch (IllegalArgumentException e) { // 동일한 채널 이름이 존재하거나 변경할 채널이 존재하지 않으면 예외 발생
                System.out.println(e.getMessage());
            }
        }
    }

    private void updateChannelAdmin(ChannelResponseDto channel) {
        PrivateChannelResponseDto privateChannel;

        while (true) {
            try {
                privateChannel = (PrivateChannelResponseDto) channel;
                List<UserResponseDto> members = privateChannel.getMemberIds().stream()
                        .map(m -> userService.find(m))
                        .collect(Collectors.toList());
                members.removeIf(u -> u.getId().equals(loginUser.getId()));

                Printer.printLine();
                System.out.println("채널 관리자 변경을 선택하였습니다. 변경을 원하는 멤버를 선택해주세요.");
                for (int i = 0; i < members.size(); i++) {
                    System.out.printf("%d. %s\n", i + 1, members.get(i).getNickName());
                }
                System.out.printf("%d. 이전 메뉴\n", members.size() + 1);

                System.out.print("입력: ");
                int choice = sc.nextInt();
                sc.nextLine();

                if (choice >= 1 && choice <= members.size()) {
                    UserResponseDto newAdmin = members.get(choice - 1);
                    channelService.updateAdmin(privateChannel.getId(), new UpdateChannelAdminRequestDto(loginUser.getId(), newAdmin.getId()));
                    System.out.println("관리자가 변경되었습니다. 이전 메뉴로 돌아갑니다.");
                    return;
                } else if (choice == members.size() + 1) {
                    System.out.println("이전 메뉴로 돌아갑니다.");
                    return;
                } else {
                    System.out.println("올바르지 않은 입력입니다.");
                }
            } catch(ClassCastException e) {
                System.out.println("공개 채널은 관리자를 변경할 수 없습니다. 이전 메뉴로 돌아갑니다.");
                return;
            } catch (InputMismatchException e) {
                System.out.println("숫자만 입력해주세요.");
                sc.nextLine();
            } catch (IllegalArgumentException e) {
                // 1. 채널이 없다면 예외 발생
                // 2. 관리자가 될 유저가 채널에 속해있지 않다면 예외 발생
                // 3. 선택된 멤버가 이미 채널 관리자라면 예외 발생
                System.out.println(e.getMessage());
            }
        }
    }

    private void deleteChannelMember(ChannelResponseDto channel) {
        PrivateChannelResponseDto privateChannel;

        while(true) {
            try {
                privateChannel = (PrivateChannelResponseDto) channel;
                List<UserResponseDto> channelMember = privateChannel.getMemberIds().stream()
                        .map(m -> userService.find(m))
                        .collect(Collectors.toList());
                channelMember.removeIf(u -> u.getId().equals(loginUser.getId())); //로그인 된 유저의 정보는 제외

                Printer.printLine();
                System.out.println("삭제할 멤버를 선택해주세요");
                for (int i = 0; i < channelMember.size(); i++) {
                    System.out.printf("%d. %s\n", i + 1, channelMember.get(i).getNickName());
                }
                System.out.printf("%d. 이전 메뉴\n", channelMember.size() + 1);

                System.out.print("입력: ");
                int choice = sc.nextInt();
                sc.nextLine();

                if(choice == channelMember.size() + 1) return;
                else if (choice < 0 && choice > (channelMember.size() + 1)) {
                    System.out.println("메뉴 내 숫자만 입력해주세요.");
                    continue;
                }

                UserResponseDto target = channelMember.get(choice - 1); //삭제되는 멤버 정보 저장
                System.out.println("채널에서 멤버를 삭제하려면 멤버 닉네임을 입력해주세요");
                System.out.print("입력: ");
                String userName = sc.nextLine();

                if (target.getNickName().equals(userName)) {
                    channelService.deleteChannelMember(privateChannel.getId(), loginUser.getId(), target.getId());
                    System.out.println("선택된 멤버가 삭제되었습니다.\n이전메뉴로 돌아갑니다.");
                    return;
                } else {
                    System.out.println("닉네임이 정확하지 않습니다. 멤버를 다시 선택해주세요.");
                }
            } catch (ClassCastException e) {
                System.out.println("공개채널은 채널 멤버를 삭제할 수 없습니다. 이전 메뉴로 돌아갑니다.");
                return;
            } catch (InputMismatchException e) {
                System.out.println("숫자만 입력해주세요.");
                sc.nextLine();
            } catch (IllegalArgumentException e) {
                // 예외 발생
                // 1. channelService.deleteChannelMember
                //    - 존재하지 않는 채널을 삭제 요청(channelRepository.findById)
                //    - 관리자가 아닌 유저가 유저를 삭제 요청
                //    - 채널에 없는 유저를 삭제 요청
                //    - 관리자가 본인을 삭제 요청
                // 2. channelRepoisitory.deleteChannelIdForUser
                //    - 삭제될 유저가 존재하지 않는 경우
                //    - 삭제될 유저가 속한 채널이 없는 경우
                System.out.println(e.getMessage());
                return;
            }
        }
    }

    private void deleteChannel(ChannelResponseDto channel) {
        try {
            System.out.println("정말로 채널을 삭제하시겠습니까?");
            System.out.println("삭제를 원하시면 채널 이름을 정확히 입력해주세요");
            System.out.printf("채널 이름: %s\n", channel.getChannelName());
            System.out.print("입력: ");
            if (channel.getChannelName().equals(sc.nextLine())) {
                channelService.delete(channel.getId(), new DeleteChannelRequestDto(loginUser.getId()));
                System.out.println("채널이 삭제되었습니다.");
            } else {
                System.out.println("정확히 입력되지 않았습니다. 이전 메뉴로 돌아갑니다.");
            }
        } catch (IllegalArgumentException e) {
            // 예외 발생
            // 1. channelService.deleteChannel
            //    - 존재하지 않는 채널을 삭제 요청(channelRepository.findById)
            //    - 관리자가 아닌 유저가 채널을 삭제 요청
            // 2. channelRepoisitory.deleteChannelIdForUser
            //    - 삭제될 유저가 존재하지 않는 경우
            //    - 삭제될 유저가 속한 채널이 없는 경우
            System.out.println(e.getMessage());
        }
    }

    private void leaveChannel(ChannelResponseDto channel) {
        try {
            if (loginUser.getId().equals(channel.getAdminId())) {
                System.out.println("관리자는 채널을 나갈 수 없습니다.");
                System.out.println("채널 관리자를 변경하거나 채널 삭제를 선택해주세요.");
                return;
            }

            System.out.printf("정말로 %s을 나가시길 원하시면 1을 눌러주세요.\n", channel.getChannelName());
            System.out.print("입력: ");
            int choice = sc.nextInt();

            if (choice == 1) {
                channelService.deleteChannelMember(channel.getId(), loginUser.getId(), loginUser.getId());
            } else {
                System.out.println("1이 아닌 숫자를 입력하였습니다.\n이전 메뉴로 돌아갑니다.");
            }
        } catch (InputMismatchException e) {
            System.out.println("숫자만 입력해주세요.\n이전 메뉴로 돌아갑니다.");
            sc.nextLine();
        } catch (IllegalArgumentException e) {
            // 예외 발생
            // 1. channelService.deleteChannelMember
            //    - 존재하지 않는 채널을 삭제 요청(channelRepository.findById)
            //    - 관리자가 아닌 유저가 유저를 삭제 요청
            //    - 채널에 없는 유저를 삭제 요청
            //    - 관리자가 본인을 삭제 요청
            // 2. channelRepoisitory.deleteChannelIdForUser
            //    - 삭제될 유저가 존재하지 않는 경우
            //    - 삭제될 유저가 속한 채널이 없는 경우
            System.out.println(e.getMessage());
        }
    }

    private void createChannel() {
        try {
            System.out.println("새로운 채널을 생성합니다. 정보를 입력해주세요");
            System.out.println("채널 타입 및 공개 여부 : 1. 메시지(공개) 2. 메시지(비공개) 3. 음성(공개) 4. 음성(비공개) 5. 이전 메뉴");
            System.out.print("입력: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1, 2, 3, 4 -> {
                    System.out.print("채널 이름: ");
                    String newChannelName = sc.nextLine();

                    if(choice == 1) channelService.create(loginUser.getId(), new CreateChannelRequestDto(ChannelType.MESSAGE, ChannelVisibility.PUBLIC, newChannelName));
                    else if(choice == 2) channelService.create(loginUser.getId(), new CreateChannelRequestDto(ChannelType.MESSAGE, ChannelVisibility.PRIVATE, newChannelName));
                    else if(choice == 3) channelService.create(loginUser.getId(), new CreateChannelRequestDto(ChannelType.VOICE, ChannelVisibility.PUBLIC, newChannelName));
                    else channelService.create(loginUser.getId(), new CreateChannelRequestDto(ChannelType.VOICE, ChannelVisibility.PRIVATE, newChannelName));
                }
                case 5 -> { return; }
                default -> {
                    System.out.println("메뉴에서 정확히 골라주세요. 이전 메뉴로 돌아갑니다.");
                    return;
                }
            }
            System.out.println("채널이 생성되었습니다.");
        } catch (InputMismatchException e) {
            System.out.println("채널 타입은 숫자만 입력해주세요. 이전 메뉴로 돌아갑니다.");
            sc.nextLine();
        } catch (IllegalArgumentException e) {
            // 중복되는 채널 이름 생성 요청시 예외 발생
            System.out.println(e.getMessage());
        }
    }

    private void updateInfo() {
        String newRealName = null;
        String newNickName = null;
        String newEmail = null;
        String newPhoneNum = null;
        String newUsername = null;
        String newPassword = null;

        try {
            System.out.println("내 정보 수정");
            System.out.println("1. 이름 2. 닉네임 3. 이메일 4. 전화번호 5. 비밀번호 6. 나가기");
            System.out.print("입력: ");
            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1, 2, 3, 4, 5 -> {
                    if (choice == 1) {
                        System.out.println("현재 이름: " + loginUser.getRealName());
                        System.out.print("변경: ");
                        newRealName = sc.nextLine();
                    } else if(choice == 2){
                        System.out.println("현재 닉네임: " + loginUser.getNickName());
                        System.out.print("변경: ");
                        newNickName = sc.nextLine();
                    } else if(choice == 3){
                        System.out.println("현재 이메일: " + loginUser.getEmail());
                        System.out.print("변경: ");
                        newEmail = sc.nextLine();
                    } else if(choice == 4){
                        System.out.println("현재 전화번호: " + loginUser.getPhoneNum());
                        System.out.print("변경: ");
                        newPhoneNum = sc.nextLine();
                    } else {
                        String nowPassword, newPassword2;

                        System.out.print("현재 비밀번호 입력: ");
                        nowPassword = sc.nextLine();

                        if(userService.isPasswordMatch(loginUser.getId(), nowPassword)){
                            System.out.print("새로운 비밀번호 입력: ");
                            newPassword = sc.nextLine();

                            System.out.print("새로운 비밀번호 한번 더 입력: ");
                            newPassword2 = sc.nextLine();

                            if (newPassword.equals(newPassword2)) {
                                login = false;
                                System.out.println("비밀번호가 변경되었습니다. 다시 로그인 해주세요.");
                            } else {
                                System.out.println("새로운 비밀번호가 동일하게 입력되지 않았습니다. 이전 메뉴로 돌아갑니다.");
                                return;
                            }
                        } else {
                            System.out.println("비밀번호가 틀렸습니다. 이전 메뉴로 돌아갑니다.");
                            return;
                        }
                    }
                    userService.update(loginUser.getId(), new UpdateUserRequestDto(newRealName, newNickName, newEmail, newPhoneNum, newUsername, newPassword), null);
                }
                case 6 -> { return; }
                default -> {
                    System.out.println("메뉴 내 숫자를 입력해주세요.");
                }
            }
            loginUser = userService.find(loginUser.getId()); //수정된 정보 저장
        } catch (InputMismatchException e) {
            System.out.println("숫자만 입력해주세요.");
            sc.nextLine();
        } catch (IllegalArgumentException e) {
            // 수정 요청된 유저가 존재하지 않는 경우 예외 발생
            System.out.println(e.getMessage());
        }
    }

    private void deleteAccount() {
        System.out.println("진짜로 계정을 삭제하시겠습니까?");
        System.out.println("삭제를 원하시면 아이디와 비밀번호를 정확히 입력해주세요");

        System.out.print("아이디: ");
        String loginId = sc.nextLine();
        System.out.print("비밀번호: ");
        String password = sc.nextLine();

        try {
            if (authService.checkLoginInfo(loginId, password)) {
                userService.delete(loginUser.getId());
                System.out.println("계정이 삭제되었습니다. 처음 메뉴로 돌아갑니다.");
                login = false;
            } else {
                System.out.println("정확히 입력되지 않았습니다.");
            }
        } catch (IllegalArgumentException e) {
            // 삭제할 유저가 존재하지 않는 경우 예외 발생
            System.out.println(e.getMessage());
        }
    }

    // 특정 유저가 보낸 메시지 수정/삭제를 위한 메서드
    private void handleUserMessageAction(UUID userId, UUID targetId) {
        String KST, date = null, time;

        try {
            Printer.printLine();
            List<Message> messages = messageService.findAllSentBetweenUsers(userId, targetId);

            if(messages.isEmpty()) {
                System.out.println("수정/삭제를 할 메시지가 없습니다. 채팅방으로 이동합니다.");
                return;
            }

            System.out.println("수정/삭제를 원하는 메시지를 선택해주세요.");
            for(int i = 0; i < messages.size(); i++) {
                Message msg = messages.get(i);
                KST = TimeConvert.time(msg.getCreatedAt()); // Instant 값을 "yyyy-MM-dd HH:mm:ss" 형태로 변환
                time = KST.split(" ")[1];
                System.out.printf("%d. %s 나: %s\n", i+1, time, msg.getContent());
            }
            System.out.printf("%d. 채팅방으로 이동\n", messages.size() + 1);
            System.out.print("입력: ");
            int choice = sc.nextInt();

            if(choice < 0 || choice >= messages.size() + 1) {
                System.out.println("메시지를 선택하지 않아 채팅방으로 이동합니다.");
                sc.nextLine();
                return; // 이전메뉴로 이동
            }

            Message choiceMsg = messages.get(choice-1);

            System.out.println("번호를 입력해 수정/삭제를 선택하세요");
            System.out.println("1. 수정 2. 삭제 3. 채팅방으로 이동");
            System.out.print("입력: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.println("내용을 입력해 수정하세요.");
                    System.out.printf("기존 메시지 : %s\n", choiceMsg.getContent());
                    System.out.printf("입력 : ");
                    messageService.update(loginUser.getId(), choiceMsg.getId(), new UpdateMessageRequestDto(sc.nextLine()));
                    System.out.println("메시지가 수정되었습니다.");
                }
                case 2 -> {
                    messageService.delete(loginUser.getId(), choiceMsg.getId());
                    System.out.println("메시지가 삭제되었습니다.");
                }
                default -> System.out.println("수정/삭제를 선택하지 않아 채팅방으로 이동합니다.");
            }

        } catch (InputMismatchException e) {
            System.out.println("숫자만 입력하세요. 입력으로 돌아갑니다.");
            sc.nextLine();
        }
    }
}
