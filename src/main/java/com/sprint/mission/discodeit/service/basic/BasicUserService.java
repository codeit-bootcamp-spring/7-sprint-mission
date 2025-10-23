package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;

public class BasicUserService implements UserService {
    //싱글톤 구현
    private final BasicUserService basicUserService = getInstance();

    private BasicUserService(){}
    public BasicUserService getInstance(){
        return basicUserService;
    }
}
