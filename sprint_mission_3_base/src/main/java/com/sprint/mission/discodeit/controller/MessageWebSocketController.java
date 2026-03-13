package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageWebSocketController {

    private final MessageService messageService;

    @MessageMapping("/messages")
    public void send(@Valid MessageCreateRequest request, Principal principal) {
        if (principal instanceof UsernamePasswordAuthenticationToken authentication) {
            Object userPrincipal = authentication.getPrincipal();
            if (userPrincipal instanceof DiscodeitUserDetails details) {
                messageService.createWithAuthorId(details.getId(), request);
                return;
            }
        }

        if (principal instanceof DiscodeitUserDetails details) {
            messageService.createWithAuthorId(details.getId(), request);
        }
    }
}