package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.auth.response.VerifyCodeRes;

public interface AuthService {

  void sendEmailCode(String email);

  VerifyCodeRes verifyEmailCode(String email, String code);
}
