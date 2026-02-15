package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.auth.TokenResult;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

   // TODO: refreshService 필요? 아니면 인메모리로 뭔가 그냥 가능?

    TokenResult refreshAccessToken(String refreshToken);

    void logout(String refreshToken);
}
