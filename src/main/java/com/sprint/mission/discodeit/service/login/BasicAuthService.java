package com.sprint.mission.discodeit.service.login;

import com.sprint.mission.discodeit.dto.request.authService.LoginRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserRoleUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.login.LoginResponseDto;
import com.sprint.mission.discodeit.dto.response.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.domain.user.UserNotExistException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.Role;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;
    private final UserMapper  userMapper;

    @Override
    public LoginResponseDto checkLoginUser(LoginRequestDto loginRequestDto) {
        String userName = loginRequestDto.username();
        String password = loginRequestDto.password();
        List<User> userList = userRepository.findAll();
        Optional<User> optionalUser = userList.stream().filter(x -> x.getUserName().equals(userName) && x.getPassword().equals(password)).findFirst();
        User existUser = optionalUser.orElseThrow(()->new UserNotExistException(null));
        return new LoginResponseDto(
                existUser.getId(),
                existUser.getCreatedAt(),
                existUser.getUpdatedAt(),
                existUser.getUserName(),
                existUser.getEmail(),
                existUser.getPassword(),
                existUser.getProfile() == null ? null : existUser.getProfile().getId()
        );
    }

    @Override
    @Transactional
    public UserDto updateUserRole(UserRoleUpdateRequestDto userRoleUpdateRequestDto) {
        UUID userId = userRoleUpdateRequestDto.userId();
        Role newRole = userRoleUpdateRequestDto.newRole();
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotExistException(userId));
        user.updateUserRole(newRole);
        return userMapper.toDto(user);
    }
}
