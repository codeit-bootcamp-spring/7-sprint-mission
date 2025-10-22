import com.sprint.mssion.discodeit.entity.Channel;
import com.sprint.mssion.discodeit.entity.Message;
import com.sprint.mssion.discodeit.entity.User;
import com.sprint.mssion.discodeit.repository.ChannelRepository;
import com.sprint.mssion.discodeit.repository.MessageRepository;
import com.sprint.mssion.discodeit.repository.UserRepository;
import com.sprint.mssion.discodeit.repository.file.FileChannelRepository;
import com.sprint.mssion.discodeit.repository.file.FileManager;
import com.sprint.mssion.discodeit.repository.file.FileMessageRepository;
import com.sprint.mssion.discodeit.repository.file.FileUserRepository;
import com.sprint.mssion.discodeit.service.ChannelService;
import com.sprint.mssion.discodeit.service.MessageService;
import com.sprint.mssion.discodeit.service.UserService;
import com.sprint.mssion.discodeit.service.file.FileChannelService;
import com.sprint.mssion.discodeit.service.file.FileFacadeService;
import com.sprint.mssion.discodeit.service.file.FileMessageService;
import com.sprint.mssion.discodeit.service.file.FileUserService;
import com.sprint.mssion.discodeit.service.jcf.JCFFacadeService;
import com.sprint.mssion.discodeit.service.jcf.JCFChannelService;
import com.sprint.mssion.discodeit.service.jcf.JCFUserService;
import com.sprint.mssion.discodeit.service.jcf.JCFMessageService;

import java.nio.file.Paths;
import java.util.List;
import java.util.Random;


public class JavaApplication {
    public static void main(String[] args) {

        //정훈이의 고민: https://github.com/woowacourse/retrospective/discussions/15
        // -> 파사드 패턴?

        // Repository init
        UserRepository userRepository = new FileUserRepository(new FileManager<>(Paths.get("src", "main", "resources", "Users.ser")));
        ChannelRepository channelRepository = new FileChannelRepository(new FileManager<>(Paths.get("src", "main", "resources", "Channels.ser")));
        MessageRepository messageRepository = new FileMessageRepository(new FileManager<>(Paths.get("src", "main", "resources", "Messages.ser")));

        // Service init
        UserService userService = new FileUserService(userRepository);
        ChannelService channelService = new FileChannelService(channelRepository);
        MessageService messageService = new FileMessageService(messageRepository);
        FileFacadeService facadeService = new FileFacadeService(userService, channelService, messageService);
//        FileFacadeService facadeService = new FileFacadeService(userService, channelService, messageService);

        // 유저 생성
        User user1 = userService.createUser("dog", "dog@codeit.com", "1111", "111");
        User user2 = userService.createUser("cat", "cat@codeit.com", "2222", "222");
        User user3 = userService.createUser("cow", "cow@codeit.com","3333","333");
        User user4 = userService.createUser("pig", "pig@codeit.com", "4444", "444");
        User user5 = userService.createUser("aaaa", "aaa@codeit.com", "4444", "2442");

        // 유저 업데이트
        userService.updateUser(user2.getCommon().getId(), "cat", "cat@codeit.com", "2222", "222_Update");

        // 채널 생성
        Channel notice = channelService.createChannel(Channel.ChannelType.PUBLIC, "공지", "공지 채널입니다.");
        Channel general = channelService.createChannel(Channel.ChannelType.PUBLIC, "일반", "일반 채널입니다.");
        Channel random = channelService.createChannel(Channel.ChannelType.PUBLIC, "랜덤", "랜덤 채널입니다.");

        facadeService.addChannelToUserWithRelation(user1.getCommon().getId(), notice.getCommon().getId());
        facadeService.addChannelToUserWithRelation(user2.getCommon().getId(), notice.getCommon().getId());
        facadeService.addChannelToUserWithRelation(user3.getCommon().getId(), general.getCommon().getId());
        facadeService.addChannelToUserWithRelation(user4.getCommon().getId(), random.getCommon().getId());

        // 유저 리스트 확인(채널 추가 확인)
        System.out.println("========== 유저 리스트");
        for(User user : userRepository.findAll()){
            System.out.println(user);
        }

        //메세지 생성
        facadeService.createMessageWithRelation(user1.getCommon().getId(), general.getCommon().getId(), "안녕하세요");
        facadeService.createMessageWithRelation(user2.getCommon().getId(), general.getCommon().getId(), "반갑다");
        facadeService.createMessageWithRelation(user1.getCommon().getId(), general.getCommon().getId(), "이름 무너ㅑ");
        facadeService.createMessageWithRelation(user2.getCommon().getId(), general.getCommon().getId(), "gdgdgdd");
        facadeService.createMessageWithRelation(user3.getCommon().getId(), general.getCommon().getId(), "나도 안녕");

        // 여기서는 따로 addChannelToUser를 호출하지 않았지만, 자체 함수에서 채널에 없으면, 해당 유저를 채널에 참여
        facadeService.createMessageWithRelation(user1.getCommon().getId(), random.getCommon().getId(), "ㅎㅇ1");
        facadeService.createMessageWithRelation(user5.getCommon().getId(), random.getCommon().getId(), "ㅎㅇ22222");

        System.out.println("========== 유저 리스트");
        for(User user : userRepository.findAll()){
            System.out.println(user);
        }

        System.out.println("========== 채널 리스트");
        for(Channel channel  : channelRepository.findAll()){
            System.out.println(channel);
        }

        System.out.println("========== 메세지 리스트");
        for(Message message : messageRepository.findAll()){
            System.out.println(message);
        }

        // 채널 삭제
        facadeService.deleteChannelWithRelation(random.getCommon().getId());
        // 1. 기댓값은 해당 채널이 유저 채널리스트에서 없어야함.
        System.out.println("============ 채널을 삭제 시 유저에 참여채널 확인 ==================");
        for(User user : userRepository.findAll()){
            System.out.println(user.getUsername() + "참여 채널: " + user.getJoinChannels());
        }

        // 2. 기댓값은 해당 채널에 속하던 메세지는 메세지 인스턴스를 삭제해야함.
        System.out.println("============ 채널을 삭제 시 메세지 확인 ==================");
        for(Message message : messageRepository.findAll()){
            System.out.println(message);
        }

        // 유저 삭제
        facadeService.deleteUserWithRelation(user1.getCommon().getId());
        System.out.println("========== 유저 삭제 시 유저 리스트");
        for(User user : userRepository.findAll()){
            System.out.println(user);
        }
        System.out.println("============ 유저 삭제 시 메세지 확인 ==================");
        for(Message message : messageRepository.findAll()){
            System.out.println(message);
        }


        //유저 삭제(같은거 삭제 해서 에러 나는지 테스트)
        try {
            facadeService.deleteUserWithRelation(user1.getCommon().getId());
        } catch (Exception e){
            System.out.println("이미 삭제된 유저");
        }

        // 메세지 생성 및 삭제 테스트
        List<Message> messages = messageService.getAllMessages();
        Message testMessage = messages.get(0);
        System.out.println("삭제 대상 메세지: " + testMessage);
        messageService.deleteMessage(messages.get(0).getCommon().getId());
        System.out.println("========== 메세지 리스트");
        for(Message message : messageRepository.findAll()){
            System.out.println(message);
        }
    }
}
