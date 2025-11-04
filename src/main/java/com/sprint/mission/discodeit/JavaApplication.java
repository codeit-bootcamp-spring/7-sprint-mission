package com.sprint.mission.discodeit;

public class JavaApplication {
/*
    static User setupUser(UserService userService) {
        User user = userService.create("신제원", "wwewey@codeit.com", "wo1234","nick");
        return user;
    }


    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        Message message = messageService.create("안녕하세요.", channel.getId(), author.getId());
        System.out.println("메시지 생성: " + message.getId());
    }*/

    public static void main(String[] args) {
        // 이놈이 생성되어야 서비스가 받을수있고
     /*   UserRepository userRepository = new FileUserRepository();
        ChannelRepository channelRepository = new FileChannelRepository();
        MessageRepository messageRepository = new FileMessageRepository();

        // 이놈이 생성되어야 위에 스태틱조건에 매개값이 들어올수가 있다
        UserService userService = new BasicUserService(userRepository);
        ChannelService channelService = new BasicChannelService(channelRepository);
        MessageService messageService = new BasicMessageService(messageRepository, channelRepository, userRepository);

        // 셋업
        User user = setupUser(userService);
        Channel channel = channelService.create(user.getId(),"공지 채널입니다.");
        // 테스트
        messageCreateTest(messageService, channel, user);*/
    }
}
