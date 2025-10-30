package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.request.UserCreateReq;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {
    //싱글톤 구현
    private final static JCFUserService jcfUserService = new JCFUserService();

    private JCFUserService(){}

    public static JCFUserService getInstance(){
        return jcfUserService;
    }

    //리포지토리
    private final JCFUserRepository jcfUserRepository = JCFUserRepository.getInstance();

    //유저 추가
    @Override
    public User create(UserCreateReq req){
        return jcfUserRepository.save(req.to());
    }

    //유저 목록
    @Override
    public List<User> findAll(){
        return jcfUserRepository.findAll();
    }

    //이메일 찾기
    @Override
    public User findByEmail(String email) {
        return jcfUserRepository.findByEmail(email);
    }

    //닉네임으로 찾기
    @Override
    public List<User> findByNickname(String nickname) {
        return jcfUserRepository.findByNickname(nickname);
    }

    //삭제
    @Override
    public User delete(UUID id) {
        return jcfUserRepository.delete(id);
    }

    //업데이트
    @Override
    public User update(UUID id, String nickname, String password) {
        return jcfUserRepository.update(id,nickname,password);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jcfUserRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return jcfUserRepository.existsByNickname(nickname);
    }
}
