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
import org.springframework.stereotype.Service;

import java.time.Instant;
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
    public User create(UserCreateRequest userCreateRequest,Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {

     //이메일매칭
     if(userRepository.findAll().stream()
             .anyMatch(user -> user.getEmail().equals(userCreateRequest.email()))){
         throw new IllegalArgumentException("이메일이 이미 존재합니다");
     }
     //닉네임매칭
     if(userRepository.findAll().stream()
                .anyMatch(user -> user.getUsername().equals(userCreateRequest.username()))){
         throw new IllegalArgumentException("이름 이미 존재합니다");
     }

        UUID uuid = profileId(optionalProfileCreateRequest);
        //유저정보
        User user = new User(
                userCreateRequest.email(),
                userCreateRequest.password(),
                userCreateRequest.username(),
                uuid

        );

        //유저정보저장
        User savedUser = userRepository.save(user);
        //유저 상태 저장
        userStatusRepository.save(savedUser.getId());
        //유저 스테이터스 생성
        UserStatus userStatus = new UserStatus(user.getId(), Instant.now());
        return  user;
    }


    @Override
    public UserDto find(UUID userId) {

        return  userRepository.findById(userId)
                .map(this::toDto)
                .orElseThrow(()->new NoSuchElementException("유저아이디로 찾을수없어"));

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
    public User update(UserUpdateRequest userUpdateRequest,Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {

        //이메일매칭
        if(userRepository.findAll().stream()
                .anyMatch(u -> u.getEmail().equals(userUpdateRequest.newEmail()))){
            throw new IllegalArgumentException("이메일이 이미 존재합니다");
        }
        //네임매칭
        if(userRepository.findAll().stream()
                .anyMatch(u -> u.getUsername().equals(userUpdateRequest.newUsername()))){
            throw new IllegalArgumentException("이름 이미 존재합니다");
        }



        UUID uuid = profileId(optionalProfileCreateRequest);
        User user = new User(userUpdateRequest.newUsername(),userUpdateRequest.newEmail(), userUpdateRequest.newPassword(), uuid);

        Instant now = Instant.now();
        UserStatus userStatus = new UserStatus(user.getId(), now);

          //폼으로 바로넣고주자
        return userRepository.save(user);
    }

    @Override
    public void delete(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("유유아이디없어요" + userId + " 이걸 못찾았어요"));

        Optional.ofNullable(user.getProfileId())
                .ifPresent(binaryRepository::delete);
        userStatusRepository.deleteByUserId(userId);

        userRepository.deleteById(userId);
    }



    private UUID profileId(Optional<BinaryContentCreateRequest>  binaryContentCreateRequest){
        return binaryContentCreateRequest
                .map(profileRequest -> {
                    String fileName = profileRequest.fileName();
                    String contentType = profileRequest.contentType();
                    byte[] bytes = profileRequest.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
                            contentType, bytes);
                    return binaryRepository.save(binaryContent).getId();
                })
                .orElse(null);
    }


    private UserDto toDto(User user) {
        Boolean online = userStatusRepository.findByUserId(user.getId())
                .map(UserStatus::isOnline)
                .orElse(null);

        return new UserDto(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getUsername(),
                user.getEmail(),
                user.getProfileId(),
                online
        );
    }
}
