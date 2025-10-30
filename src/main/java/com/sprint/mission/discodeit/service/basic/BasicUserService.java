package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.UserCreateReq;
import com.sprint.mission.discodeit.dto.request.UserUpdateReq;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {
    //리포지토리
    private UserRepository userRepository;
    private UserStatusRepository userStatusRepository;

    //유저 추가
    @Override
    public User create(UserCreateReq req){
        //중복검사
        if(existsByEmail(req.email())){
            throw new RuntimeException("Email is already registered");
        }
        if(existsByNickname(req.nickname())){
            throw new RuntimeException("Nickname is already registered");
        }

        User newUser = userRepository.save(req.to());

        if(req.profileId() != null){
            userRepository.updateProfileImg(newUser.getId(), req.profileId());
        }
        userStatusRepository.save(new UserStatus(newUser.getId()));

        return newUser;
    }

    //이메일 중복 검사
    @Override
    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    //닉네임 중복 검사
    @Override
    public boolean existsByNickname(String nickname){
        return userRepository.existsByNickname(nickname);
    }

    //유저 목록
    @Override
    public List<User> findAll(){
        return userRepository.findAll();
    }

    //이메일 찾기
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    //닉네임으로 찾기
    @Override
    public User findByNickname(String nickname) {
        return userRepository.findByNickname(nickname);
    }

    //삭제
    @Override
    public User delete(UUID id, UUID profileId, UUID userStatusId) {
        // Todo : BinaryContent, UserStatus 리포지토리를 이용하여 삭제
        return userRepository.delete(id);
    }

    //업데이트
    @Override
    public User update(UUID id, UserUpdateReq req) {
        User updatedUser = userRepository.update(id,req.nickname(),req.password());
        if(req.profileId() != null){
            userRepository.updateProfileImg(id, req.profileId());
        }
        return updatedUser;
    }
}
