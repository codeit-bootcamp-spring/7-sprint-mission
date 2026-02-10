package com.sprint.mission.discodeit.service.basic;

import com.nimbusds.jwt.JWTClaimsSet;
import com.sprint.mission.discodeit.dto.auth.JwtDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.mapper.UserMapperManual;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.jwt.JwtTokenProvider;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final UserMapperManual userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public JwtDto refreshAccessToken(String refreshToken) {
        JWTClaimsSet jwtClaimsSet = jwtTokenProvider.validateToken(refreshToken);
        try {
            UUID userId = UUID.fromString(jwtClaimsSet.getStringClaim("user_id"));

            User user = userRepository.findById(userId).orElseThrow(() -> new DiscodeitException(ErrorCode.USER_NOT_FOUND));

            UserResponseDto authDto = userMapper.toAuthDto(user);

            String accessToken = jwtTokenProvider.generateAccessToken(
                    user.getId(),
                    user.getEmail(),
                    user.getUsername(),
                    user.getRole()
            );

            return new JwtDto(authDto, accessToken);

        } catch (ParseException e) {
            SecurityContextHolder.clearContext();
        }


        return null;
    }
}
