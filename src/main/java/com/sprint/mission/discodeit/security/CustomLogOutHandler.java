package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.security.repository.JpaPersistenceTokenRepository;
import com.sprint.mission.discodeit.security.service.CustomPersistentTokenRememberMe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomLogOutHandler implements LogoutHandler {

    private final JpaPersistenceTokenRepository tokenRepository;


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        if (authentication != null) {
            DiscodeitUserDetails userDetails = (DiscodeitUserDetails) authentication.getPrincipal();
            UUID userId = userDetails.getUserDto().id();

            tokenRepository.removeUserTokensByUserId(userId); // DB 삭제
        }
    }
}

