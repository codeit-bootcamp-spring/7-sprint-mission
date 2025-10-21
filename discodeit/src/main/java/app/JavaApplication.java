package app;

import entity.User;
import entity.Channel;
import entity.Message;

import service.jcf.JCFUserService;
import service.jcf.JCFChannelService;
import service.jcf.JCFMessageService;

import java.util.List;
import java.util.UUID;

public class JavaApplication {

    public static void main(String[] args) {

        // 1️⃣ 서비스 객체 생성 (싱글톤)
        JCFUserService userService = JCFUserService.getInstance();
        JCFChannelService channelService = JCFChannelService.getInstance();
        JCFMessageService messageService = JCFMessageService.getInstance();

        // 2️⃣ 유저 등록
        User user1 = new User("멍", "1234", "dog@mail.com", "010-1111-2222", "Doggy", "Hello!");
        userService.create(user1);

        User user2 = new User("냥", "abcd", "cat@mail.com", "010-3333-4444", "Kitty", "Hi!");
        userService.create(user2);

        // 3️⃣ 채널 등록
        Channel channel1 = new Channel("General", user1.getId(), false);
        channelService.create(channel1);

        // 4️⃣ 메시지 등록
        Message message1 = new Message(user1.getId(), channel1.getId(), "Welcome to General!");
        messageService.create(message1);

        // 5️⃣ 단건 조회
        System.out.println("유저1: " + userService.findById(user1.getId()).getNickname());
        System.out.println("채널1: " + channelService.findById(channel1.getId()).getTopic());
        System.out.println("메시지1: " + messageService.findById(message1.getId()).getContent());

        // 6️⃣ 전체 조회
        List<User> allUsers = userService.findAll();
        System.out.println("모든 유저: " + allUsers.size());

        List<Channel> allChannels = channelService.findAll();
        System.out.println("모든 채널: " + allChannels.size());

        List<Message> allMessages = messageService.findAll();
        System.out.println("모든 메시지: " + allMessages.size());

        // 7️⃣ 수정
        user1.changeNickname("김철수");
        userService.update(user1);

        channel1.changeTopic("공지사항");
        channelService.update(channel1);

        // 8️⃣ 수정된 데이터 조회
        System.out.println("유저1 닉네임 업데이트: " + userService.findById(user1.getId()).getNickname());
        System.out.println("채널1 주제 업데이트: " + channelService.findById(channel1.getId()).getTopic());

        // 9️⃣ 삭제
        userService.delete(user2.getId());
        channelService.delete(channel1.getId());

        // 1️⃣0️⃣ 삭제 확인
        System.out.println("삭제 후 모든 유저: " + userService.findAll().size());
        System.out.println("삭제 후 모든 채널: " + channelService.findAll().size());
    }
}
