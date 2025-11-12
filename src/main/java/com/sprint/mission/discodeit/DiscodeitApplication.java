package com.sprint.mission.discodeit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class DiscodeitApplication {

  /*  static User setupUser(UserService userService) {
        return JavaApplication.setupUser(userService);
    }


    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        JavaApplication.messageService(messageService, channel, author);
    }*/

	public static void main(String[] args) {
        // 레포지토리 초기화




         SpringApplication.run(DiscodeitApplication.class, args);

     /*   BasicUserService userService = context.getBean(BasicUserService.class);
        BasicChannelService channelService = context.getBean(BasicChannelService.class);
        BasicMessageService messageService = context.getBean(BasicMessageService.class);
*/
        //기본유저
      //  User user = setupUser(userService);

        //유저가있어야 채널이 가능하다
       // Channel channel = channelService.create(user.getId(),"그냥채널");

        //채널 유저가 있어야 메시지 가능하다
     //   messageCreateTest(messageService, channel, user);
        //JavaApplication: 개발자가 직접 객체를 만들고 의존성을 연결해야 하는 방식
        //
        //DiscodeitApplication: 스프링 IoC Container가 Bean을 생성하고 의존성을 자동으로 주입해주기 때문에, 개발자는 완성된 서비스 Bean만 꺼내 쓰면 됨

    }

}
