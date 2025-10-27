package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiscodeitApplication {
	public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);

        //요구사항을 이용한 간단한 프로그램 실행
        DiscodeitSpringTest discodeitSpringTest = new DiscodeitSpringTest(userService, channelService, messageService);
        discodeitSpringTest.run();

        // ===========================================
        // 테스트용 기본 유저 데이터 (테스트/샘플용)
        // ===========================================
        // 이름      | 아이디      | 비밀번호
        // -------------------------------------------
        // 테스트    | test1234   | test1234
        // 박지훈    | idjihun    | securePass1!
        // 이수빈    | idsubin    | myPassword2@
        // 최하늘    | idhaneul   | skyPass123!
        // 이영희    | idyounghee | pw5678
        // 박민수    | idminsu    | securepw1
        // 최지현    | idjihyun   | mypassword2
        // 오세훈    | idsehun    | hello4321
        // ===========================================
        // 실제 데이터 생성은 TestDataInitializer 클래스에서 수행
        // 기존 File I/O 서비스 사용으로 파일 내 유저 데이터 삭제시 유저 일부가 삭제되었을 수 있다
	}
}
