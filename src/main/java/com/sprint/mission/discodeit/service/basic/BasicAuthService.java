package com.sprint.mission.discodeit.service.basic;

import com.nimbusds.jwt.JWTClaimsSet;
import com.sprint.mission.discodeit.dto.auth.TokenResult;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.mapper.UserMapperManual;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.jwt.JwtTokenProvider;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final RefreshTokenService refreshTokenService;

    @Override
    @Transactional
    public TokenResult refreshAccessToken(String refreshToken) {
        JWTClaimsSet jwtClaimsSet = jwtTokenProvider.validateToken(refreshToken);
        try {

            if (!"refresh".equals(jwtClaimsSet.getStringClaim("token_type"))) {
                throw new DiscodeitException(ErrorCode.INVALID_TOKEN);
            }


            UUID userId = UUID.fromString(jwtClaimsSet.getStringClaim("user_id"));

            if(!refreshTokenService.exists(userId, refreshToken)){ // NOTE: 위 리프레시토큰 검증이후 존재하지않다면 탈취하여 재사용한다고 보기때문에
                refreshTokenService.revokeAll(userId);
                throw new DiscodeitException(ErrorCode.INVALID_TOKEN);
            }

            User user = userRepository.findById(userId).orElseThrow(() -> new DiscodeitException(ErrorCode.USER_NOT_FOUND));

            UserResponseDto authDto = userMapper.toAuthDto(user);

            // 기존 리프레시 토큰 삭제
            refreshTokenService.revokeAll(userId);

            String newAccessToken = jwtTokenProvider.generateAccessToken(
                    user.getId(),
                    user.getEmail(),
                    user.getUsername(),
                    user.getRole()
            );

            String newRefreshToken = jwtTokenProvider.generateRefreshToken(
                    user.getId(),
                    user.getEmail()
            );

            // 새로운 리프레시 토큰 저장
            refreshTokenService.rotateRefreshToken(userId, newRefreshToken);

            return new TokenResult(authDto, newAccessToken, newRefreshToken);

        } catch (ParseException e) {
            SecurityContextHolder.clearContext();
        }

        return null;
    }

    @Override
    @CacheEvict(cacheNames="users", allEntries=true)
    public void logout(String refreshToken) {
        refreshTokenService.deleteRefreshToken(refreshToken);
    }
}
