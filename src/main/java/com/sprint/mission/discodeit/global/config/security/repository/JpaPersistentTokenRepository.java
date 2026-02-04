package com.sprint.mission.discodeit.global.config.security.repository;

import com.sprint.mission.discodeit.global.config.security.domain.PersistentLogin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class JpaPersistentTokenRepository implements PersistentTokenRepository {
    private final PersistentLoginRepository persistentLoginRepository;

    @Override
    @Transactional
    public void createNewToken(PersistentRememberMeToken token) {
        PersistentLogin persistentLogin = PersistentLogin.builder()
                .username(token.getUsername())
                .series(token.getSeries())
                .token(token.getTokenValue())
                .build();
        persistentLoginRepository.save(persistentLogin);
        log.info("Remember-me 토큰 생성: {}", persistentLogin);
    }

    @Override
    @Transactional
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        persistentLoginRepository.findBySeries(series)
                .ifPresentOrElse(persistentLogin -> {
                            persistentLogin.updateToken(tokenValue, lastUsed.toInstant());
                            persistentLoginRepository.save(persistentLogin);
                            log.info("토큰 갱신 성공: {}", persistentLogin);
                        },
                        () -> {
                            log.warn("토큰 갱신 실패, series: {}", series);
                        }

                );
    }

    @Override
    @Transactional(readOnly = true)
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        log.info("토큰 조회 시도 seriesId: {}", seriesId);
        return persistentLoginRepository.findBySeries(seriesId)
                .map(entity -> {
                    PersistentRememberMeToken token = new PersistentRememberMeToken(
                            entity.getUsername(),
                            entity.getSeries(),
                            entity.getToken(),
                            Date.from(entity.getLastUsed()
                                    .atZone(ZoneId.systemDefault())
                                    .toInstant())
                    );

                    log.info("토큰 조회 성공: username = {}, series = {}", entity.getUsername(), seriesId);
                    return token;
                })
                .orElse(null);
    }

    @Override
    @Transactional
    public void removeUserTokens(String username) {
        long count = persistentLoginRepository.countByUsername(username);

        if (count > 0) {
            persistentLoginRepository.deleteByUsername(username);
            log.info("Remember-Me 토큰 삭제 사용자: {}", username);
            log.info("삭제된 토큰 수: {}", count);
        } else {
            log.info("삭제할 Remember-Me 토큰 없음! 사용자: {}", username);
        }
    }
}
