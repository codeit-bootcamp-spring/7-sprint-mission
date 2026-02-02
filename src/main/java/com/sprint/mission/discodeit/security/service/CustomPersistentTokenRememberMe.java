package com.sprint.mission.discodeit.security.service;

import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;



public class CustomPersistentTokenRememberMe extends PersistentTokenBasedRememberMeServices {

    private final PersistentTokenRepository customTokenRepository;

    public CustomPersistentTokenRememberMe(
           String key,
            UserDetailsService userDetailsService,
            PersistentTokenRepository tokenRepository) {
        super(key, userDetailsService, tokenRepository);
        this.customTokenRepository = tokenRepository; // 직접 저장
    }

    @Override
    protected void onLoginSuccess(HttpServletRequest request,
                                  HttpServletResponse response,
                                  Authentication successfulAuthentication) {
        DiscodeitUserDetails userDetails = (DiscodeitUserDetails) successfulAuthentication.getPrincipal();

        String userId = userDetails.getUserDto().id().toString();
        String series = generateSeriesData();
        String tokenValue = generateTokenData();

        PersistentRememberMeToken persistentToken = new PersistentRememberMeToken(
                userId,
                series,
                tokenValue,
                new Date()
        );

        customTokenRepository.createNewToken(persistentToken);
        setCookie(new String[]{series, tokenValue}, getTokenValiditySeconds(), request, response);
    }

}
