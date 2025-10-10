package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.vo.Message;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Controller {
    private final ChannelService channelService;
    private final UserService userService;
    private final MessageRoomService messageRoomService;
    private final FriendRequestService friendRequestService;
    private final FriendShipService friendShipService;


    public Controller(ChannelService channelService,
                      UserService userService,
                      MessageRoomService messageRoomService,
                      FriendRequestService friendRequestService,
                      FriendShipService friendShipService) {
        this.channelService = channelService;
        this.userService = userService;
        this.messageRoomService = messageRoomService;
        this.friendRequestService = friendRequestService;
        this.friendShipService=friendShipService;
    }

    private User currentUser = null; // 로그인 상태 관리
    private final Scanner scanner = new Scanner(System.in);

    private int getInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("숫자를 입력하세요: ");
            scanner.next();
        }
        int input = scanner.nextInt();
        scanner.nextLine();
        return input;
    }

    public void start() {
        boolean running = true;

        while (running) {
            if (currentUser == null) {
                // ================= 로그인 전 메뉴 =================
                printMenuLoggedOut();
                int choice = getInput();

                switch (choice) {
                    case 1 -> signUp();
                    case 2 -> login();
                    case 0 -> {
                        running = false;
                        System.out.println("프로그램 종료");
                    }
                    default -> System.out.println("잘못된 선택");
                }

            } else {
                // ================= 로그인 후 메뉴 =================
                printMenuLoggedIn();
                int choice = getInput();

                switch (choice) {
                    case 1 -> logout();
                    case 2 -> showServers();
                    case 3 -> showFriend();
                    case 0 -> {
                        running = false;
                        System.out.println("프로그램 종료");
                    }
                    default -> System.out.println("잘못된 선택\n");
                }
            }
        }

        scanner.close();
    }

    // ================= 메뉴 출력 =================
    private void printMenuLoggedOut() {
        System.out.println("\n=====<메인 메뉴>=====");
        System.out.println("1. 회원가입");
        System.out.println("2. 로그인");
        System.out.println("0. 종료");
        System.out.print("선택> ");
    }

    private void printMenuLoggedIn() {
        System.out.println("\n=====<메인 메뉴>=====");
        System.out.println("1. 로그아웃");
        System.out.println("2. 내 서버 보기");
        System.out.println("3. 내 친구 보기");
        System.out.println("0. 종료");
        System.out.println("(로그인 중: " + currentUser.getUsername() + ")");
        System.out.print("선택> ");
    }


    // ================= 회원가입 / 로그인 =================
    private void signUp() {
        boolean completed = false;

        while (!completed) {
            System.out.print("사용자 이름: ");
            String username = scanner.nextLine();
            System.out.print("비밀번호(4글자 이상): ");
            String password = scanner.nextLine();
            System.out.print("이메일: ");
            String email = scanner.nextLine();
            System.out.print("전화번호 : ");
            String phoneNumber = scanner.nextLine();
            User user = new User(username, password, email, phoneNumber);
            try {
                userService.save(user);
                System.out.println(username + "님 회원가입 완료!\n");
                currentUser = user;
                completed = true;

            } catch (Exception e) {
                System.out.println("[회원가입 실패]> " + e.getMessage());
                System.out.println("다시 입력해주세요.\n");
            }
        }


    }

    //로그인
    private void login() {
        boolean logIn = false;

        while (!logIn) {
            System.out.println("\n[로그인] (취소-> 0 입력)");
            System.out.print("이메일: ");
            String email = scanner.nextLine();
            if (email.equals("0")) break;
            User user = userService.findByEmail(email);
            if (user == null) {
                System.out.println("해당 이메일을 가진 사용자를 찾을 수 없습니다. 다시 시도해주세요.\n");
                continue;
            }
            System.out.print("비밀번호: ");
            String password = scanner.nextLine();
            if (password.equals("0")) break;
            if (user.getPassword().equals(password)) {
                currentUser = user;
                logIn = true;
                System.out.println(user.getUsername() + "님 로그인 성공!\n");
            } else {
                System.out.println("비밀번호가 올바르지 않습니다. 다시 시도해주세요.\n");
            }
        }
    }

    private void logout() {
        System.out.println(currentUser.getUsername() + "님 로그아웃 완료");
        currentUser = null;
    }

    // ================= 서버 메뉴 =================
    private void showServers() {

        boolean back = false;
        while (!back) {
            List<UUID> myChannels = currentUser.getMyChannels();
            List<Channel> channels = myChannels.stream().map(channelService::findById).toList();

            System.out.println("\n=== 서버 목록 ===");

            for (int i = 0; i < channels.size(); i++) {
                System.out.println((i + 1) + ". " + channels.get(i).getServerName());
            }
            System.out.println(channels.size()+1+". 채널 생성하기");
            System.out.println("0. 뒤로 가기");
            System.out.print("선택> ");

            int choice = getInput();

            if (choice == 0) back = true;
            else if (choice >= 1 && choice <= channels.size()) {
                showChannelMenu(channels.get(choice - 1));
            } else if (choice==channels.size()+1){
                openChannel();
            } else {
                System.out.println("잘못된 선택\n");
            }
        }
    }

    private void showChannelMenu(Channel channel) {
        boolean back = false;
        while (!back) {

            System.out.println("\n=== " + channel.getServerName() + " ===");
            System.out.println("1. 채팅방");
            System.out.println("2. 멤버");
            System.out.println("3. 채널 생성 하기");
            System.out.println("0. 뒤로 가기");
            System.out.print("선택> ");
            int choice = getInput();

            switch (choice) {
                case 1 -> showMessageRoomList(channel);
                case 2 -> showMemberList(channel);
                case 3 -> makeMessageRoom(channel);
                case 0 -> back = true;
                default -> System.out.println("잘못된 선택\n");
            }
        }
    }

    private void showMessageRoomList(Channel channel) {
        List<UUID> channelMessageRoomsId = channelService.getChannelMessageRoomsId(channel);

        if (channelMessageRoomsId.isEmpty()) {
            System.out.println("채널이 없습니다.\n");
            return;
        }
        boolean back = false;
        while (!back) {

            System.out.println("\n=== 채널 목록 ===");
            for (int i = 0; i < channelMessageRoomsId.size(); i++) {
                MessageRoom messageRoom = messageRoomService.findById(channelMessageRoomsId.get(i));
                System.out.println((i + 1) + ". " + messageRoom.getMessageRoomName());
            }
            System.out.println("0. 뒤로 가기");

            System.out.print("선택> ");
            int choice = getInput();

            if (choice == 0) {
                back = true; // 뒤로 가기
            } else if (choice < 1 || choice > channelMessageRoomsId.size()) {
                System.out.println("잘못된 선택입니다. 다시 입력해주세요.\n");
            } else {
                // 선택한 메시지룸 들어가기
                UUID roomId = channelMessageRoomsId.get(choice - 1);
                MessageRoom selectedRoom = messageRoomService.findById(roomId);
                enterMessageRoom(selectedRoom);
            }
        }
    }


    //채팅방 들어가기
    private void enterMessageRoom(MessageRoom room) {

        System.out.println("\n=== " + room.getMessageRoomName() + " ===");

        boolean inRoom = true;
        while (inRoom) {
            // 채팅 기록 보여주기
            List<Message> history = room.getHistory();// MessageRoom 안에 List<String> 있다고 가정
            if (history.isEmpty()) {
                System.out.println("(채팅 기록 없음)");
            } else {

                if (history.size() > 10) {
                    for (int i = history.size() - 10; i < history.size(); i++) {
                        Message msg = history.get(i);
                        System.out.println("(" + userService.findById(msg.getSenderId()).getUsername() + ")" + msg.getContent());
                    }
                } else {
                    for (Message msg : history) {
                        System.out.println("(" + userService.findById(msg.getSenderId()).getUsername() + ")" + msg.getContent());
                    }
                }
            }

            System.out.print("\n메시지를 입력하세요 (0 입력 시 뒤로 가기): ");
            String input = scanner.nextLine();

            if (input.equals("0")) {
                inRoom = false;
            } else {
                Message message = new Message(currentUser.getId(), input);
                room.addHistory(message);
            }
        }
    }


    private void showMemberList(Channel server) {
        System.out.println("\n=== 멤버 목록 ===");
        List<UUID> membersId = server.getMembers();

        for (UUID memberId : membersId) {
            User user = userService.findById(memberId);
            System.out.println("- " + user.getUsername());
        }
        System.out.println();
    }


    private void makeMessageRoom(Channel channel) {
        System.out.print("\n새 채널 이름 입력: ");
        String MessageRoomName = scanner.nextLine();

        if (MessageRoomName.isBlank()) {
            System.out.println("채널 이름은 비어있을 수 없습니다.\n");
            return;
        }


        MessageRoom messageRoom = new MessageRoom();
        messageRoom.setMessageRoomType(MessageRoomType.SERVER_MESSAGE_ROOM);
        messageRoom.setMessageRoomName(MessageRoomName);
        List<UUID> members = channel.getMembers();
        for (UUID member : members) {
            messageRoom.addParticipants(member);
        }
        channel.addMessageRoom(messageRoom.getId());
        messageRoomService.save(messageRoom);
        System.out.println("'" + MessageRoomName + "' 채널이 생성되었습니다!\n");
    }

    //채널 생성
    public void openChannel() {
        System.out.println("\n=== 서버 생성하기 ===");

        System.out.print("서버 이름을 입력하세요: ");
        String channelName = scanner.nextLine();

        if (channelName.isBlank()) {
            System.out.println("채널 이름은 비어있을 수 없습니다.\n");
            return;
        }
        System.out.print("서버레벨을 입력하세요(숫자만): ");
        Long serverLevel = (long) scanner.nextInt();
        scanner.nextLine();

        Channel channel = new Channel();
        channel.setServerLevel(serverLevel); // 일단 기본값
        channel.setServerName(channelName);
        channel.addMember(currentUser.getId());
        channelService.save(channel);

        System.out.println("'" + channelName + "'서버가 성공적으로 생성되었습니다!\n");
    }


    // ================= 친구 메뉴 =================
    private void showFriend() {
        boolean back = false;
        while (!back) {
            // 현재 친구 목록 가져오기
            List<UUID> friendIds = currentUser.getFriends();

            System.out.println("\n=== 친구 목록 ===");
            if (friendIds.isEmpty()) {
                System.out.println("친구가 없습니다.");
            } else {
                for (int i = 0; i < friendIds.size(); i++) {
                    User friend = userService.findById(friendIds.get(i));
                    System.out.println((i + 1) + ". " + friend.getUsername());
                }
            }

            // 친구 관리용 메뉴
            System.out.println((friendIds.size() + 1) + ". 친구 추가 요청 보내기");
            System.out.println((friendIds.size() + 2) + ". 친구 요청 확인");
            System.out.println("0. 뒤로 가기");
            System.out.print("선택> ");

            int choice = getInput();

            if (choice == 0) {
                back = true; // 뒤로가기
            } else if (choice >= 1 && choice <= friendIds.size()) {
                // 친구 선택 → 개인 메시지룸 열기
                UUID friendId = friendIds.get(choice - 1);
                User friend = userService.findById(friendId);
//                openDirectMessage(friend);
            } else if (choice == friendIds.size() + 1) {
                // 친구 추가 요청 보내기
                sendFriendRequest();
            } else if (choice == friendIds.size() + 2) {
                // 친구 요청 확인
                seeFriendRequests();
            } else {
                System.out.println("잘못된 선택입니다.");
            }
        }
    }
    private void sendFriendRequest() {
        System.out.print("친구 이메일 입력: ");
        String receiverName = scanner.nextLine();
        //유저 베이스에 데이터가 없을 경우 여기선 오류가 발생하지 않고 null이 담김
        User toUser = userService.findByEmail(receiverName);

        if (toUser == null) {
            System.out.println("사용자를 찾을 수 없습니다.");
            return;
        }

        FriendRequest friendRequest = new FriendRequest(currentUser.getId(), toUser.getId());

        toUser.addFriendRequest(friendRequest.getId());
        friendRequestService.save(friendRequest);
        System.out.println("친구 요청 보냄: " + toUser.getUsername());
    }

    private void seeFriendRequests() {

        boolean back=false;
        while (!back) {

            List<UUID> myFriendRequest = currentUser.getMyFriendRequest();
            if (myFriendRequest.isEmpty()) {
                System.out.println("대기 중인 친구 요청이 없습니다.");
                return;
            }

            System.out.println("\n=== 대기 중인 친구 요청 ===");
            int n=0;
            for (UUID uuid : myFriendRequest) {
                n++;
                FriendRequest request = friendRequestService.findById(uuid);
                User sender = userService.findById(request.getSenderId());
                System.out.println(n+". " + sender.getUsername() + " 요청");
            }
            System.out.println("0. 뒤로 가기");
            System.out.print("선택> ");
            int choice = getInput();

            if (choice == 0) {
                back = true; // 뒤로가기
            } else if (choice >= 1 && choice <= myFriendRequest.size()) {
                UUID requestId = myFriendRequest.get(choice - 1);
                FriendRequest request = friendRequestService.findById(requestId);
                acceptFriendRequest(request);
                back=true;
            } else {
                System.out.println("잘못된 선택입니다.");
            }

        }
    }

    public void acceptFriendRequest(FriendRequest request){
        UUID senderId = request.getSenderId();
        UUID receiverId = request.getReceiverId();
        FriendShip friendShip = new FriendShip(senderId, receiverId);
        User userA = userService.findById(senderId);
        User userB = userService.findById(receiverId);
        userA.addFriend(receiverId);
        userB.addFriend(senderId);
        friendShipService.save(friendShip);
    }

}
