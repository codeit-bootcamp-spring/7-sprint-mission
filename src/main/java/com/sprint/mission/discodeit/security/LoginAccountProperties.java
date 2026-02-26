package com.sprint.mission.discodeit.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
//import ConfigurationProperties;

@Component
@ConfigurationProperties(prefix = "login.account")
@Getter @Setter
public class LoginAccountProperties {
    private String userAdmin;
    private String userAdminPassword;
    private String userAdminEmail;
    private String userDefault;
    private String userDefaultPassword;
    private String userDefaultEmail;
}
