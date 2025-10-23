package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.module.Configuration;

@SpringBootApplication
public class DiscodeitApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(DiscodeitApplication.class, args);

        User user  =  ctx.getBean(User.class);


	}

}
