package com.sprint.mission.discodeit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing   // ⭐ 꼭 필요함
@SpringBootApplication
public class DiscodeitApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscodeitApplication.class, args);
    }
}
