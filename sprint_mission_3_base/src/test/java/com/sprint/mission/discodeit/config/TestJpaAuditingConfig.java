package com.sprint.mission.discodeit.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@TestConfiguration
public class TestJpaAuditingConfig {
}

//해결은 inner class(중첩 @Configuration) 제거 + Auditing 설정을 “외부 @TestConfiguration”로 빼서 @Import만 하기.