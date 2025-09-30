import com.sprint.mssion.discodeit.entity.Channel;
import com.sprint.mssion.discodeit.entity.Message;
import com.sprint.mssion.discodeit.entity.User;
import com.sprint.mssion.discodeit.service.ChannelService;
import com.sprint.mssion.discodeit.service.MessageService;
import com.sprint.mssion.discodeit.service.UserService;
import com.sprint.mssion.discodeit.service.jcf.JCFChannelService;
import com.sprint.mssion.discodeit.service.jcf.JCFUserservice;
import com.sprint.mssion.discodeit.service.jcf.JCfMessageService;

import java.util.List;
import java.util.UUID;

public class JavaApplication {
    static User setupUser(UserService userService) {
        return userService.create("woody", "woody1234", "woody@codeit.com",
                "010-1234-5678", "ㅇㅇ");
    }

    static Channel setupChannel(ChannelService channelService) {
        Channel channel = channelService.create(Channel.ChannelType.PUBLIC, "공지", "공지 채널입니다.");
        return channel;
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        Message message = messageService.create("안녕하세요.", channel.getCommon().getId(), author.getCommon().getId());
        System.out.println("메시지 생성: " + message.getCommon().getId());
    }

    public static void main(String[] args) {
        // 서비스 초기화
        // TODO Basic*Service 구현체를 초기화하세요.
        UserService userService = new JCFUserservice();
        ChannelService channelService = new JCFChannelService();
        MessageService messageService = new JCfMessageService();

        // 셋업
        User user1 = userService.create("woody", "woody1234", "woody@codeit.com",
                "010-1234-5678", "ㅇㅇ");
        User user2 = userService.create("buzz", "buzz9999", "buzz@codeit.com",
                "010-1111-2222", "ㅇㅇ");
        User user3 = userService.create("jessie", "jessie555", "jessie@codeit.com",
                "010-3333-4444", "ㅇㅇ");
        User user4 = userService.create("rex", "rex8888", "rex@codeit.com",
                "010-5555-6666", "ㅇㅇ");
        List<User> users = userService.readAll();
        for(User u : users) System.out.println(u.toString());

        userService.update(user1.getCommon().getId(),"woody_update", "woody1234", "woody@codeit.com",
                "010-1234-5678", "ㅇㅇ_update");
        userService.read(user1.getCommon().getId());
        userService.delete(user4.getCommon().getId());


        System.out.println("=============================================================");
        Channel channel1 = channelService.create(Channel.ChannelType.PUBLIC, "공지", "공지 채널입니다.");
        Channel channel2 = channelService.create(Channel.ChannelType.PRIVATE, "일반", "일반 채널입니다.");
        Channel channel3 = channelService.create(Channel.ChannelType.PRIVATE, "팀채널", "팀 채널입니다.");
        List<Channel> channels = channelService.readAll();
        for(Channel ch : channels) System.out.println(ch.toString());

        System.out.println("=============================================================");
        Message message1= messageService.create("안녕하세요. User1", channel1.getCommon().getId(), user1.getCommon().getId());
        Message message2= messageService.create("안녕하세요.22 User2", channel1.getCommon().getId(), user2.getCommon().getId());
        Message message3= messageService.create("오냐", channel1.getCommon().getId(), user1.getCommon().getId());
        Message message4= messageService.create("안녕 채널 2", channel2.getCommon().getId(), user1.getCommon().getId());

        List<Message> messages = messageService.readAll();
        for(Message m : messages) System.out.println(m.toString());

//        System.out.println("=============== 에러 테스트 ============================================");
//        Message errorMsg = messageService.create("에러 메세지", UUID.randomUUID(), user1.getCommon().getId()); // 없는 유저
//        Message errorMsg2 = messageService.create("에러 메세지2", channel1.getCommon().getId(), UUID.randomUUID()); // 없는 유저

        System.out.println("================== 채널에 참여한 유저 및 메시지 리스트 확인");
        channels = channelService.readAll();
        for(Channel ch : channels) System.out.println(ch.toString());

        System.out.println("===== User1 참여 채널 리스트 확인===");
        System.out.println(user1.getJoinChannels());

        System.out.println("================== 삭제 후 채널에 참여한 유저 및 메시지 리스트 확인");
        channelService.delete(channel1.getCommon().getId()); // channel1 삭제
        channels = channelService.readAll();
        for(Channel ch : channels) {
            System.out.println(ch.toString());
        }

        System.out.println("user1이 참여 중인 채널: " + user1.getJoinChannels());

        System.out.println("==================== 유저 삭제 하였을 때 메세지 및 채널 리스트 확인");
        userService.delete(user1.getCommon().getId());
        channels = channelService.readAll();
        for(Channel ch : channels) {
            System.out.println(ch.toString());
        }
        messages = messageService.readAll();
        for(Message m : messages) System.out.println(m.toString());

//        유저 삭제로 인해 에러가 나야 하는 코드
//        userService.update(user1.getCommon().getId(), "woody", "woody1234", "woody@codeit.com",
//                "010-1234-5678", "ㅇㅇ");

        System.out.println("=========================채널 수정");
        channelService.update(channel3.getCommon().getId(),Channel.ChannelType.PRIVATE, "팀채널_수정", "팀 채널입니다." );
        channels = channelService.readAll();
        for(Channel ch : channels) {
            System.out.println(ch.toString());
        }


    }
}
