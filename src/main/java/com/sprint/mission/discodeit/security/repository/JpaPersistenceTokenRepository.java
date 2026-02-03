package com.sprint.mission.discodeit.security.repository;

import com.sprint.mission.discodeit.entity.PersistenceLogin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JpaPersistenceTokenRepository implements PersistentTokenRepository {

    private final PersistenceLoginRepository repository;

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        PersistenceLogin entity = new PersistenceLogin(UUID.fromString(token.getUsername()));
        repository.save(entity);
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        repository.findBySeries(series)
                .ifPresentOrElse(entity -> {
                    entity.updateToken();
                    repository.save(entity);
                },
                        ()->{
                    log.warn("token update failed");
                        }
                        );

    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        Optional<PersistentRememberMeToken> tokenOptional = repository.findBySeries(seriesId).map(entity -> {
            PersistentRememberMeToken token = new PersistentRememberMeToken(
                    entity.getUserId().toString(),
                    entity.getSeries(),
                    entity.getToken(),
                    Date.from(entity.getLastUsed())

            );
            return token;
        });
        return tokenOptional.orElse(null);
    }

    @Override
    @Transactional
    public void removeUserTokens(String username) {
        UUID userId = UUID.fromString(username);

        long count = repository.countByUserId(userId);
        if(count > 0){
            repository.deleteByUserId(userId);
        }
        else{
            log.info("삭제할 remember-me 토큰 없음! 사용자 : {}",username);
        }
    }


}
