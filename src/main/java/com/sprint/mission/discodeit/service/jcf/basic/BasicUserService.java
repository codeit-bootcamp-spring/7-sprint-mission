package com.sprint.mission.discodeit.service.jcf.basic;

import com.sprint.mission.discodeit.dto.user.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.response.UserCreateResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.List;

@Service
public class BasicUserService implements UserService {

    //컬렉션 필드  final  메서드들은 이 데이터를 가지고 만들꺼다
    //왜 리스트였냐 수업중에 조회는,수정은 리스트
    // 추가(중간정도) , 삭제가 빈번하면 링크라들었다
    //마지막에 컴퓨터 좋아지면서 리스트를 많이쓴다 해서 리스트했다
    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserCreateResponse create(UserCreateRequest userCreateRequest) {
     //이메일매칭
     if(userRepository.findAll().stream()
             .anyMatch(user -> user.getUserEmail().equals(userCreateRequest.email()))){
         throw new RuntimeException("Email already exists");
     }
     //닉네임매칭
     if(userRepository.findAll().stream()
                .anyMatch(user -> user.getUserNickname().equals(userCreateRequest.userNickname()))){
         throw new RuntimeException("Nickname already exists");
     }



     //저장을위한 모든 정보
        User user = new User(
                userCreateRequest.username()
                ,userCreateRequest.email()
                ,userCreateRequest.rawPassword()
                ,userCreateRequest.userNickname());
       //유저저장
       userRepository.save(user);

        //유저 스테이터스 생성
        new  UserStatus(user.getId());
      return new UserCreateResponse(user.getId(),user.getUserNickname(),userCreateRequest.userNickname());
    }


    @Override
    public User find(UUID userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(()-> new NoSuchElementException("이런 uuid는 없어"+ userId));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    //이게진짜 이해가 안된다
    public User update(UUID userId, String newUsername, String newEmail,String newPassword ,String newNickname,String newProfilePicture) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new NoSuchElementException("유저uuid못찾아용"+userId));
         user.update(newUsername,newEmail,newPassword,newNickname,newProfilePicture);
        return userRepository.save(user);
    }

    @Override
    public void delete(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("유저uuid못찾아용"+userId);
        }
        userRepository.deleteById(userId);
    }
}
