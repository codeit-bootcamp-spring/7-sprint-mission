package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.service.basic.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final SessionRegistry sessionRegistry;
}
