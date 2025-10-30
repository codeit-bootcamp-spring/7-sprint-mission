package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.UserCreateReq;
import com.sprint.mission.discodeit.dto.request.UserUpdateReq;
import com.sprint.mission.discodeit.dto.response.UserInfoRes;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
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
    private BinaryContentRepository binaryContentRepository;

    //유저 추가
    @Override
    public User create(UserCreateReq req){
        validateDuplicate(req.email(), req.nickname());
        User newUser = userRepository.save(req.to());

        if(req.profileId() != null){
            userRepository.updateProfileImg(newUser.getId(), req.profileId());
        }
        userStatusRepository.save(new UserStatus(newUser.getId()));

        return newUser;
    }

    //중복 검사
    private void validateDuplicate(String email, String nickname){
        if(userRepository.existsByEmail(email)){
            throw new RuntimeException("Email is already registered");
        }
        if(userRepository.existsByNickname(nickname)){
            throw new RuntimeException("Nickname is already registered");
        }
    }

    //유저 목록
    @Override
    public List<UserInfoRes> findAll(){
        return userRepository.findAll().stream()
                .map(user -> UserInfoRes.from(user,
                        binaryContentRepository.findById(user.getProfileId()).getData()))
                .toList();
    }

    //이메일 찾기
    @Override
    public UserInfoRes findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if(user != null){
            return UserInfoRes.from(user,
                    binaryContentRepository.findById(user.getProfileId()).getData());
        }
        return null;
    }

    //닉네임으로 찾기
    @Override
    public UserInfoRes findByNickname(String nickname) {
        User user = userRepository.findByNickname(nickname);
        if(user != null){
            return UserInfoRes.from(user,
                    binaryContentRepository.findById(user.getProfileId()).getData());
        }
        return null;
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
        validateDuplicate(req.email(), req.nickname());
        User updatedUser = userRepository.update(id,req.email(), req.nickname(),req.password());
        if(req.profileId() != null){
            userRepository.updateProfileImg(id, req.profileId());
        }
        return updatedUser;
    }
}
