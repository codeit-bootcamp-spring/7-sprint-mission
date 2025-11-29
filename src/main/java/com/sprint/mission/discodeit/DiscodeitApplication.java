package com.sprint.mission.discodeit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * 참고사항: 유저와 채널 엔티티 사이에서는 M:N 관계를 형성하였지만, 메세지는 따로 아직 하지 않았습니다. 채널을 삭제하면, 해당 채널에 메세지 내역은 어떻게 할 것인가
 * 유저를 삭제하면, 해당 메세지는 어떻게 할 것인가 채널은 해당 채널에 대한 메세지를 들고 있을 것인가 -> 들고 있지 않기로 결정 (채널마다 메세지 리스트를 가지고 있으면,
 * 성능 저하)
 */
@EnableJpaAuditing
@SpringBootApplication
public class DiscodeitApplication {
  
  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class,
        args);
  }

}
