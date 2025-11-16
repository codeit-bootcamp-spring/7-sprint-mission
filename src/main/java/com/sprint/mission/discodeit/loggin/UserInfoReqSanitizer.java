package com.sprint.mission.discodeit.loggin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.user.request.UserInfoReq;
import com.sprint.mission.discodeit.dto.auth.request.UserLoginReq;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserInfoReqSanitizer implements ArgSanitizer {

  private static final ObjectMapper mapper = new ObjectMapper();

  @Override
  public boolean isFilterCase(Object arg) {
    return arg instanceof UserInfoReq || arg instanceof UserLoginReq;
  }

  @Override
  public Object sanitize(Object arg) {
    Map<String, Object> map = mapper.convertValue(arg, Map.class);
    if (map.containsKey("password")) {
      map.put("password", "*****");
    }
    return map;
  }
}
