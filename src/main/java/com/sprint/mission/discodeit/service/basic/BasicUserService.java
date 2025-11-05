package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.Binarycontent.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.user.response.UserCreateResponse;
import com.sprint.mission.discodeit.dto.user.response.UserDto;
import com.sprint.mission.discodeit.dto.user.response.UserFindResponse;
import com.sprint.mission.discodeit.dto.user.response.UserUpdateResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.content.BinaryContent;
import com.sprint.mission.discodeit.entity.content.ContentsType;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {

    //컬렉션 필드  final  메서드들은 이 데이터를 가지고 만들꺼다
    //왜 리스트였냐 수업중에 조회는,수정은 리스트
    // 추가(중간정도) , 삭제가 빈번하면 링크라들었다
    //마지막에 컴퓨터 좋아지면서 리스트를 많이쓴다 해서 리스트했다
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryRepository binaryRepository;



    @Override
    public UserCreateResponse create(UserCreateRequest userCreateRequest,Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {

     //이메일매칭
     if(userRepository.findAll().stream()
             .anyMatch(user -> user.getUserEmail().equals(userCreateRequest.email()))){
         throw new IllegalArgumentException("이메일이 이미 존재합니다");
     }
     //닉네임매칭
     if(userRepository.findAll().stream()
                .anyMatch(user -> user.getUserNickname().equals(userCreateRequest.userNickname()))){
         throw new IllegalArgumentException("닉네임이 이미 존재합니다");
     }
     

        UUID nullableProfileId = optionalProfileCreateRequest
                .map(profileRequest -> {

                    byte[] bytes = profileRequest.contentByte();
                    BinaryContent binaryContent = new BinaryContent(ContentsType.PROFILE_IMAGE, bytes, "null");
                    return binaryRepository.save(binaryContent).getId();
                })
                .orElse(null);

        //유저정보
        User user = new User(
                userCreateRequest.email(),
                userCreateRequest.rawPassword(),
                userCreateRequest.username(),
                userCreateRequest.userNickname(),
                nullableProfileId

        );
     //   user.setProfileID(nullableProfileId);
       //혹시이미지가 있니 없니
    /*    if (optionalProfileCreateRequest. != null) {
            BinaryContent binaryContent = new BinaryContent(ContentsType.PROFILE_IMAGE, userCreateRequest.profileImage());
            binaryRepository.save(binaryContent);

            user.setProfileID(binaryContent.getId());
        }*/

        //유저정보저장
        userRepository.save(user);
        //유저 상태 저장
        userStatusRepository.save(user.getId());
        //유저 스테이터스 생성
        new  UserStatus(user.getId());
      return  UserCreateResponse.from(user);
    }


    @Override
    public UserFindResponse find(UUID userId) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("유저uuid못찾아용" + userId));
        User finduser = userRepository.findById(userId)
                .orElseThrow(()->new NoSuchElementException("유저uuid못찾아용"+userId));

        //유저 스타터스추가
        return UserFindResponse.from(finduser,userStatus);
    }

    @Override
    public List<UserDto> findAll() {
        List<UserDto>  userList = new ArrayList<>(List.of());
        for(User user:userRepository.findAll()){

            UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new NoSuchElementException("유저uuid못찾아용" + user.getId()));

            userList.add(UserDto.from(user,userStatus));
        }

        return userList;
    }

    @Override
    public UserUpdateResponse update(UserUpdateRequest userUpdateRequest,Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
        //id있는지확인
        User user = userRepository.findById(userUpdateRequest.userId())
                .orElseThrow(()->new NoSuchElementException("유저uuid못찾아용"+userUpdateRequest.userId()));
        UUID nullableProfileId = optionalProfileCreateRequest
                .map(profileRequest -> {
                    Optional.ofNullable(user.getId())
                            .ifPresent(binaryRepository::delete);

                    byte[] bytes = profileRequest.contentByte();
                    BinaryContent binaryContent = new BinaryContent(ContentsType.PROFILE_IMAGE, bytes,"null");
                    return binaryRepository.save(binaryContent).getId();
                })
                .orElse(null);

        //저장용
         user.update(userUpdateRequest);//업데이트했으니 갱신해야지
          userRepository.save(user);
          //폼으로 바로넣고주자
        return UserUpdateResponse.from(userUpdateRequest.userId(),user);
    }

    @Override
    public void delete(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("유저uuid못찾아용"+userId);
        }

        UUID profileID = userRepository
                .findById(userId)
                .orElseThrow(() -> new NoSuchElementException("유저uuid못찾아용" + userId))
                .getProfileID();
        if(profileID != null) {
            binaryRepository.delete(profileID);
        }
        //이런게 너무번거롭다 싫다
        userStatusRepository.findByUserId(userId);
        userRepository.deleteById(userId);
    }
}
