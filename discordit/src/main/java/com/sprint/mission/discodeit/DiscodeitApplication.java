package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.repository.file.DataLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiscodeitApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

		// 파일 레포지토리 초기화
		// 원래는 *Repository에 File*Repository가 주입되었는지 확인하고 써야하는데 (instance of)
		// 어차피 JCF는 더 안 쓸 거고 곧 데이터베이스 연결할 것 같아서 그냥 이렇게 사용함
		// context로 가져온 이유는 미션 요구사항이라서
		context.getBean("dataLoader", DataLoader.class).loadAll();

		context.getBean("discodeIt", DiscodeIt.class).start();
	}

}
