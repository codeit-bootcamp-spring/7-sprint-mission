package com.sprint.mission.discodeit.security.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthenticationSuccessListener {
    // NOTE: 인증 성공시 사이드이팩트 담당, 관찰할 부분에 대한 책임 넣어도됨(ex) ), 참고로 AuthenticationXXXHandler 보다 먼저 실행됨
    // 하지만 AuthenticationManager.authenticate()가 먼저 실행된후 실행됨
    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event){
        Authentication authentication = event.getAuthentication();
        String name = authentication.getName();
        log.info("로그인 성공 name = {}", name);
    }
}
