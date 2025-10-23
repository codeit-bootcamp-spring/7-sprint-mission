package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.req.UserCreatedReq;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;

public class BasicUserService implements UserService {

    //싱글톤 구현
    private final static BasicUserService basicUserService = getInstance();

    private BasicUserService(){}
    public static BasicUserService getInstance(){
        return basicUserService;
    }

    //유저 추가
    public User create(UserCreatedReq req){
        return new User(req.email(), req.nickname(), req.password());
    }
}
