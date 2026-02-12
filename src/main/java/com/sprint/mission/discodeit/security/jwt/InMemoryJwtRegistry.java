package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.dto.response.jwt.JwtInformation;
import com.sprint.mission.discodeit.dto.response.user.UserDto;
import com.sprint.mission.discodeit.exception.domain.user.UserNotExistException;
import com.sprint.mission.discodeit.security.service.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@RequiredArgsConstructor
public class InMemoryJwtRegistry implements JwtRegistry {


    private final int maxActiveJwtCount =1;
    private final Map<UUID, Queue<JwtInformation>> origin = new ConcurrentHashMap<>();
    private final JwtTokenProvider jwtTokenProvider;
    private final int CLEAR_INTERVAL = 1000*60* 5; //5분마다 삭제 진행
    @Override
    public void registerJwtInformation(JwtInformation jwtInformation) {
        UserDto userDto = jwtInformation.userDto();
        String accessToken = jwtInformation.accessToken();
        String refreshToken = jwtInformation.refreshToken();
        if(origin.containsKey(userDto.id())) {
            Queue<JwtInformation> queue = origin.get(userDto.id());
            queue.clear();
            queue.add(jwtInformation);
            return;
        };

        Queue<JwtInformation> queue = new ConcurrentLinkedQueue<>();
        queue.add(jwtInformation);
        origin.put(userDto.id(), queue);
        return;
    }

    @Override
    public void invalidateJwtInformationByUserId(UUID userId) {
    if(!origin.containsKey(userId)) throw new UserNotExistException(userId);
    origin.remove(userId);
    }



    @Override
    public boolean hasActiveJwtInformationByUserId(UUID userId) {
        return origin.containsKey(userId);
    }

    @Override
    public boolean hasActiveJwtInformationByAccessToken(String accessToken) {
        return origin.values().stream().anyMatch(x->

                x.stream().anyMatch(
                        jwtInformation ->
                                jwtInformation.accessToken().equals(accessToken))
                );
    }

    @Override
    public boolean hasActiveJwtInformationByRefreshToken(String refreshToken) {
        return origin.values().stream().anyMatch(x->

                x.stream().anyMatch(
                        jwtInformation ->
                                jwtInformation.accessToken().equals(refreshToken))
        );
    }

    @Override
    public Queue<JwtInformation> getJwtInformationByUserId(UUID userId){
        return origin.get(userId);
    }

    @Override
    public void rotateJwtInformation(String refreshToken, JwtInformation newJwtInformation) {

        origin.values().stream().filter(
                x->
                        x.stream().anyMatch(
                                jwtInformation ->
                                        jwtInformation.refreshToken().equals(refreshToken)
                        )
        ).findFirst().ifPresent(
                x->
                {
                    x.clear();
                    x.add(newJwtInformation);
                }
        );
    }

    @Scheduled(fixedDelay = CLEAR_INTERVAL)
    @Override
    public void clearExpiredJwtInformation() {
        origin.values().forEach(
                x->
                        x.removeIf(
                                jwtInformation ->
                                {
                                    Date expirationDate = jwtTokenProvider.getRefreshTokenExpirationDate(jwtInformation.refreshToken());
                                    return expirationDate.before(new Date());
                                }
                        )
        );
    }
}
