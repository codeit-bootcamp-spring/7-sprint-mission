package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.dto.Dto_AuthService;
import com.sprint.mission.discodeit.entity.dto.Res_UserLogin;
import com.sprint.mission.discodeit.repository.InterfaceUserRepository;
import com.sprint.mission.discodeit.service.InterfaceAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements InterfaceAuthService {
    private final InterfaceUserRepository userRepository;

    @Override
    public Res_UserLogin isLogin(Dto_AuthService authService) {
//    [ ] username, password과 일치하는 유저가 있는지 확인합니다.
//    [ ] 일치하는 유저가 있는 경우: 유저 정보 반환
//    [ ] 일치하는 유저가 없는 경우: 예외 발생
//    [ ] DTO를 활용해 파라미터를 그룹화합니다.
        return userRepository.isLogin(authService.userName(), authService.password());
    }
}
