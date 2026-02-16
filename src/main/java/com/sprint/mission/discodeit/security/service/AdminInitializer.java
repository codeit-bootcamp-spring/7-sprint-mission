package com.sprint.mission.discodeit.security.service;

import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.Role;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer {

    private final UserService userService;
    private final UserRepository userRepository;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @EventListener(ApplicationReadyEvent.class)
    public void initialize(){

        if(userRepository.findByRole(Role.ADMIN).isPresent()) return;
        UserCreateRequestDto requestDto = new UserCreateRequestDto(
                "ADMIN",
                adminEmail,
                adminPassword

        );
        userService.createAdmin(requestDto);
        log.info("Admin has been created : {}", requestDto);

    }
}
