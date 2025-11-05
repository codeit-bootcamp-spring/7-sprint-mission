package com.sprint.mission.discodeit.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    @Bean
    public Scanner sc(){
        return new Scanner(System.in);
    }

}
