package com.sprint.mission.discodeit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

@Configuration
public class RoleHierarchyConfig {

    // 권한 계층 구조 정의
    // ADMIN > MANAGER > USER
    @Bean
    public RoleHierarchy roleHierarchy() {
        String hierarchy = """
                ROLE_ADMIN > ROLE_CHANNEL_MANAGER
                ROLE_CHANNEL_MANAGER > ROLE_USER
                """;
        return RoleHierarchyImpl.fromHierarchy(hierarchy);
    }

    @Bean
    static MethodSecurityExpressionHandler methodSecurityExpressionHandler( // NOTE: @PreAuthorize사용 메서드 계층에서 처리할때 적용, 현재론 filter 웹 레벨에서 적용되기에 쓰진않음
            RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        handler.setRoleHierarchy(roleHierarchy);
        return handler;
    }


}
