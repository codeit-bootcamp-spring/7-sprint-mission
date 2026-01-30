package com.sprint.mission.discodeit.service;

public interface AuthService {

    /**
     * 로그인
     * @return 로그인 성공 시 User, 실패 시 예외 발생
     */
    boolean checkLoginInfo(String loginId, String password);
}
