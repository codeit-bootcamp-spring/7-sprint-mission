package com.sprint.mission.discodeit.security.repository;

import com.sprint.mission.discodeit.security.entity.PersistentLogin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JpaPersistentTokenRepository implements PersistentTokenRepository {

    private final PersistentLoginRepository repository;

    @Override
    @Transactional
    public void createNewToken(PersistentRememberMeToken token) {
        PersistentLogin entity = new PersistentLogin(
                token.getSeries(),
                token.getTokenValue(),
                token.getUsername()
        );

        repository.save(entity);

        log.info("Remember-Me 토큰 생성");
        log.info("사용자: {}", token.getUsername());
        log.info("Series: {}", token.getSeries());
        log.info("Token: {}", token.getTokenValue().substring(0, 8));
        log.info("생성 시간: {}", entity.getLastUsed());
    }

    @Override
    @Transactional
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        repository.findBySeries(series)
                .ifPresentOrElse(
                        entity -> {
                            entity.updateToken(tokenValue);
                            repository.save(entity);

                            log.info("Remember-Me 토큰 갱신! 사용자: {}", entity.getUsername());
                            log.info("Series: {}", series);
                            log.info("새 Token: {}", entity.getToken().substring(0, 8));
                            log.info("갱신 시간: {}", entity.getLastUsed());
                        },
                        () -> {
                            log.warn("Remember-Me 토큰 갱신 실패! series: {}", series);
                            // 로그아웃으로 삭제, 토큰 도용 감지로 삭제, 관리자가 수동 삭제 등등...
                        }
                );
    }

    @Override
    @Transactional(readOnly = true)
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        log.info("Remember-Me 토큰 조회 시도: series={}", seriesId);

        return repository.findBySeries(seriesId)
                .map(entity -> {
                    // JPA 엔티티 -> Spring Security 토큰 객체 변환
                    PersistentRememberMeToken token = new PersistentRememberMeToken(
                            entity.getUsername(),
                            entity.getSeries(),
                            entity.getToken(),
                            // LocalDateTime -> Date 변환
                            Date.from(entity.getLastUsed()
                                    .atZone(ZoneId.systemDefault())
                                    .toInstant())
                    );

                    log.info("토큰 조회 성공: username={}, series={}", entity.getUsername(), seriesId);
                    return token;
                })
                .orElse(null);
    }

    @Override
    @Transactional
    public void removeUserTokens(String username) {
        long count = repository.countByUsername(username);

        if (count > 0) {
            repository.deleteByUsername(username);
            log.info("Remember-Me 토큰 삭제! 사용자: {}", username);
            log.info("삭제된 토큰 수: {}", count);
        } else {
            log.info("삭제할 Remember-Me 토큰 없음! 사용자: {}", username);
        }
    }
}
