package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;

public class JavaApplication {

    public static void main(String[] args) {

//       //JCF 테스트
//       JCFUserService userService = JCFUserService.getInstance();
//       JCFChannelService channelService = JCFChannelService.getInstance();
//       JCFMessageService messageService = JCFMessageService.getInstance();

        // File 테스트
        FileUserService userService = new FileUserService();
        FileChannelService channelService = new FileChannelService();
        FileMessageService messageService = new FileMessageService();

        System.out.println("==== User 테스트 ====");

        // 유저 생성
        System.out.println("== 유저 생성==");
        User user1 = userService.createUser("김철수","바보");
        User user2 = userService.createUser("이영희", "멍청");
        User user3 = userService.createUser("아무나", "호호");
        System.out.println("생성: " + user1);
        System.out.println("생성: " + user2);
        System.out.println("생성: " + user3);

        // 유저 조회
        System.out.println("== 유저 조회==");
        User found = userService.findUser(user1.getId());
        System.out.println("조회: " + found);

        // 유저 전체 조회
        System.out.println("== 유저 전체 조회==");
        List<User> allUser =  userService.findAllUsers();
        System.out.println("전체 유저 수: " + allUser.size() + "명");
        for (User user : allUser) {
            System.out.println(user);
        }

        // 유저 업데이트
        System.out.println("== 유저 업데이트==");
        User update = userService.updateUser("진우", user1.getId(), "자바");
        System.out.println("업데이트: " + update);

        // 유저 삭제
        System.out.println("== 유저 삭제==");
        System.out.println("전체 유저 수: " + allUser.size() + "명");
        userService.deleteUser(user3.getId());
        System.out.println("삭제 완료");
        allUser = userService.findAllUsers();
        System.out.println("삭제 후 유저 수: " + allUser.size() + "명");
        System.out.println();

        System.out.println("==== Channel 테스트 ====");
        //채널 생성
        System.out.println("== 채널 생성==");
        Channel channel1 = channelService.createChannel("자유채널");
        Channel channel2 = channelService.createChannel("음성채널");
        System.out.println("생성 완료: " + channel1.getChannelName());
        System.out.println("생성 완료: " + channel2.getChannelName());

        //채널 조회
        System.out.println("== 채널 조회==");
        Channel channelFind = channelService.findByChannelName("자유채널");
        System.out.println("조회 : " + channelFind);

        // 채널 멤버 추가
        System.out.println("== 채널 멤버 추가==");
        channelService.addMember(channel1.getId(), user1.getId());
        channelService.addMember(channel1.getId(), user2.getId());
        System.out.println("추가 : " + user1.getUsername());
        System.out.println("추가 : " + user2.getUsername());

        // 채널 확인
        System.out.println("== 채널 확인==");
        Channel checkChannel = channelService.findByChannelName("자유채널");
        System.out.println("확인 : " + checkChannel);


        //채널 업데이트
        System.out.println("== 채널 업데이트==");
        Channel updateChannel = channelService.updateChannel(channel2.getId(), "채팅채널");
        System.out.println("채널명: " + updateChannel.getChannelName());

        //채널 전체 조회
        System.out.println("== 채널 전체 조회==");
        List<Channel> allChannels = channelService.findAllChannels();
        for(Channel channel : allChannels){
            System.out.println(channel.getChannelName());
        }

        //채널 삭제
        System.out.println("== 채널 삭제==");
        System.out.println("전체 채널 갯수: " + channelService.findAllChannels().size() + "개");
        channelService.deleteChannel(channel2.getId());
        System.out.println("삭제 완료");
        allChannels = channelService.findAllChannels();
        System.out.println("삭제 후 채널 갯수: " + allChannels.size() + "개");
        System.out.println();


        System.out.println("==== Message 테스트 ====");
        //메시지 생성
        System.out.println("== 메시지 생성==");
        Message msg1 = messageService.createMessage(user1.getId(), user2.getId(), "안녕하세요");
        Message msg2 = messageService.createMessage(user2.getId(), user1.getId(), "반가워요");
        Message msg3 = messageService.createMessage(user1.getId(), user2.getId(), "잘지냇ㅂ(오타)");

        System.out.println("생성 완료: " + msg1.getContent() + " 보낸 유저 : " + user1.getUsername());
        System.out.println("생성 완료: " + msg2.getContent()+ " 보낸 유저 : " + user2.getUsername());
        System.out.println("생성 완료: " + msg3.getContent() + " 보낸 유저 : " + user1.getUsername());

        //메시지 조회
        System.out.println("== 메시지 조회==");
        Message foundMsg = messageService.findMessage(msg1.getId());
        System.out.println("조회 : " + foundMsg.getContent());

        //메시지 전체 조회
        System.out.println("== 메시지 전체 조회==");
        List<Message> allMessages = messageService.findAllMessages();
        for (Message msg : allMessages) {
            System.out.println(msg.getContent());
        }

        //메시지 수정
        System.out.println("== 메시지 수정==");
        Message updateMsg = messageService.updateMessage(msg3.getId(),"잘 지내봐요");
        System.out.println(updateMsg.getContent() + " (수정됨)");

        //메시지 삭제
        System.out.println("== 메시지 삭제==");
        System.out.println("전체 메시지 갯수: " + allMessages.size() + "개");
        messageService.deleteMessage(msg1.getId());
        System.out.println("삭제 완료");
        allMessages = messageService.findAllMessages();
        System.out.println("삭제 후 메시지 갯수: " + allMessages.size() + "개");


    }
}
