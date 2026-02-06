package com.sprint.mission.discodeit.service.basic;

//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.exception.ErrorCode;
//import com.sprint.mission.discodeit.exception.UserException;
//import com.sprint.mission.discodeit.exception.UserNotFoundException;
//import com.sprint.mission.discodeit.mapper.dto.LoginRequest;
//import com.sprint.mission.discodeit.mapper.UserMapper;
//import com.sprint.mission.discodeit.mapper.dto.UserDto;
//import com.sprint.mission.discodeit.repository.jpa.UsersRepository;
//import com.sprint.mission.discodeit.service.InterfaceAuthService;
//import java.util.Map;
//import org.springframework.transaction.annotation.Transactional;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
import com.sprint.mission.discodeit.dto.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.mapper.dto.UserDto;
import com.sprint.mission.discodeit.repository.jpa.UsersRepository;
import com.sprint.mission.discodeit.service.InterfaceAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//🚨✅로그인 처리는 SecurityFilterChain에서 모두 처리

@Slf4j
@Service
//@Transactional // 영속성 컨텍스트
@RequiredArgsConstructor
public class AuthService implements InterfaceAuthService {
    private final UsersRepository userRepository;
    private final UserMapper userMapper;

//    @Transactional(readOnly = true)
//    @Override
//    public UserDto login(LoginRequest loginRequest) {
//        User user = userRepository
//          .findUserByUsername(loginRequest.username())
//            .orElseThrow(() -> new UserNotFoundException(loginRequest.username()));
//
//        boolean equals = user.getPassword().equals(loginRequest.password());
//        if (!equals) {
//            throw new UserException(ErrorCode.USER_NOT_FOUND, Map.of("userPassword", loginRequest.password()));
//        }
//
//        log.info("✅login ok! - loginId = {}", user.getId());
//        return userMapper.toDto(user);
//    }

    @Transactional(readOnly = true)
    @Override
    public UserDto userRoleUpdateRequest(UserRoleUpdateRequest userRoleUpdateRequest) {

        User user = userRepository.findById(userRoleUpdateRequest.userId())
            .orElseThrow(() -> new UserNotFoundException(userRoleUpdateRequest.userId()));

        user.setRole(userRoleUpdateRequest.newRole());

        return userMapper.toDto(user);
    }
}
