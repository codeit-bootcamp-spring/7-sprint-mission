package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.req.UserCreatedReq;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class BasicUserService implements UserService {
    //로그인 했다고 가정
    public static User LoginUser = new User("최지혜", "principle950@daum.net", "qwer123$");

    //싱글톤 구현
    private final static BasicUserService basicUserService = getInstance();

    private BasicUserService(){}

    public static BasicUserService getInstance(){
        return basicUserService;
    }

    //리포지토리
    JCFUserRepository jcfUserRepository = JCFUserRepository.getInstance();

    //유저 추가
    @Override
    public User create(UserCreatedReq req){
        return new User(req.email(), req.nickname(), req.password());
    }

    @Override
    public User findById(UUID id) {
        return null;
    }

    //유저 목록
    @Override
    public List<User> findAll(){
        return jcfUserRepository.findAll();
    }

    @Override
    public User findByEmail(String email) {
        return null;
    }

    @Override
    public User findByNickname(String nickname) {
        return null;
    }
}
