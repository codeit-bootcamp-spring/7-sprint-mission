package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.dto.request.UserCreateReq;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class FileUserService implements UserService {
    //리포지토리
    private final FileUserRepository fileUserRepository;

    //유저 추가
    @Override
    public User create(UserCreateReq req){
        return fileUserRepository.save(req.to());
    }

    //유저 목록
    @Override
    public List<User> findAll(){
        return fileUserRepository.findAll();
    }

    //이메일 찾기
    @Override
    public User findByEmail(String email) {
        return fileUserRepository.findByEmail(email);
    }

    //닉네임으로 찾기
    @Override
    public List<User> findByNickname(String nickname) {
        return fileUserRepository.findByNickname(nickname);
    }

    //삭제
    @Override
    public User delete(UUID id) {
        return fileUserRepository.delete(id);
    }

    //업데이트
    @Override
    public User update(UUID id, String nickname, String password) {
        return fileUserRepository.update(id,nickname,password);
    }

    @Override
    public boolean existsByEmail(String email) {
        return fileUserRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return fileUserRepository.existsByNickname(nickname);
    }
}
