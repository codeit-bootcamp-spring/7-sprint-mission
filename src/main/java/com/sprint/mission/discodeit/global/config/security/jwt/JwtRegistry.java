package com.sprint.mission.discodeit.global.config.security.jwt;

import java.util.UUID;

public interface JwtRegistry {

    /*
     * 로그인 성공 시 JwtInformation 등록
     * 최대 동시 로그인수(1) 제어
     */
    void registerJwtInformation(JwtInformation jwtInformation);

    // UserId로 해당 유저의 모든 JwtInformation 정보 삭제
    void invalidateJwtInformationByUserId(UUID userId);

    // JwtInformation이 Registry에 존재하는지 확인
    // 사용자의 로그인 상태를 파악
    boolean hasActiveJwtInformationByUserId(UUID userId);

    // JwtInformation이 Registry에 존재하는지 확인
    // 필터에서 유효한 토큰인지 확인
    boolean hasActiveJwtInformationByAccessToken(String accessToken);

    // JwtInformation이 Registry에 존재하는지 확인
    // 토큰 재발급 시 유효환 토큰인지 
    boolean hasActiveJwtInformationByRefreshToken(String refreshToken);

    // 토큰 재발급 시 로테이션 수행
    void rotateJwtInformation(String refreshToken, JwtInformation newJwtInformation);

    // 만료된 JwtInformation 삭제
    void clearExpiredJwtInformation();
}
