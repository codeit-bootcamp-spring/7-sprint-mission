package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;

// 아래 두 개는 셋업/테스트 호출부에서 사용
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.Channel;

@Slf4j
@SpringBootApplication
public class DiscodeitApplication {

	public static void main(String[] args) {
		try (ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args)) {

		// 1) Spring Context에서 Service 빈 조회 (요구사항 #3)
		log.info("✅ Starting Discodeit application test sequence...");

		// 2) 셋업 & 테스트 호출부 (요구사항 #1, #4)
		// ↳ 아래의 헬퍼 메소드들을 이 파일에 복사한 뒤, 주석을 해제하세요.
		UserService userService = context.getBean(UserService.class);
		ChannelService channelService = context.getBean(ChannelService.class);
		MessageService messageService = context.getBean(MessageService.class);

			User user = setupUser(userService);
			Channel channel = setupChannel(channelService);
			messageCreateTest(messageService, channel, user);

			log.info("✅ Test sequence completed successfully.");
		}
	}

	// -----------------------------------------------------------------------------

	private static User setupUser(UserService userService) {
		User user = userService.create("woody", "woody@codeit.com", "woody1234");
		log.info("👤 사용자 등록: {}", user.getEmail());
		return user;
	}

	private static Channel setupChannel(ChannelService channelService) {
		Channel channel = channelService.create(ChannelType.PUBLIC, "공지", "공지 채널입니다.");
		log.info("📢 채널 생성: {}", channel.getName());
		return channel;
	}

	private static void messageCreateTest(MessageService messageService, Channel channel, User author) {
		Message message = messageService.create("안녕하세요.", channel.getId(), author.getId());
		log.info("💬 메시지 생성: {} at {}", message.getId(), message.getCreatedAt());
	}
}