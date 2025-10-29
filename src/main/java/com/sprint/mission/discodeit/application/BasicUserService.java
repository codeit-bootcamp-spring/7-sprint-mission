package com.sprint.mission.discodeit.application;


import com.sprint.mission.discodeit.application.dto.request.UserCreateRequestDto;
import com.sprint.mission.discodeit.application.dto.request.UserRequestDto;
import com.sprint.mission.discodeit.application.dto.request.UserUpdateDto;
import com.sprint.mission.discodeit.application.dto.response.UserResponseDto;
import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.domain.repository.UserRepository;
import com.sprint.mission.discodeit.domain.exception.DuplicateUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.sprint.mission.discodeit.application.UserFindHelper.findById;
import static com.sprint.mission.discodeit.application.dto.UserDtoMapper.userToResponseDto;

@Service
@RequiredArgsConstructor
public class BasicUserService{


    private final UserRepository userRepository;

    private final FileManager fileManager;



    public UserResponseDto createUser(UserCreateRequestDto requestDto) throws IOException {
        validateDuplicateEmail(requestDto.email());
        validateDuplicateUsername(requestDto.username());


        User user = new User(
                requestDto.email(),
                requestDto.password(),
                requestDto.username(),
                requestDto.phoneNumber());
        userRepository.save(user);
        fileManager.createUserFolder(user.getId());
        if (requestDto.profileImage() != null) {
            BinaryContent content = fileManager.saveUserProfile(user.getId(), requestDto.profileImage());
            user.setProfile(content);

        }

        //유저 전용 폴더 생성 & 파일 저장


        return userToResponseDto(user);
    }

    public UserResponseDto updateUserInfo(UserUpdateDto updateDto) throws IOException {
        User user = findById(userRepository, updateDto.id());
        if (updateDto.username() != null) {
            user.updateUsername(updateDto.username());
        }
        if (updateDto.password() != null) {
            user.updatePassword(updateDto.password());
        }
        if (updateDto.phoneNumber() != null) {
            user.updatePhoneNumber(updateDto.phoneNumber());
        }
        if (updateDto.updateFile() != null) {
            BinaryContent content = fileManager.saveUserProfile(user.getId(), updateDto.updateFile());
            user.setProfile(content);
        }
        userRepository.save(user);
        return userToResponseDto(user);
    }


    public void delete(UserRequestDto requestDto) {
        userRepository.remove(findById(userRepository,requestDto.id()));
        fileManager.deleteUserFolder(requestDto.id());
    }

    public UserResponseDto getUser(UserRequestDto userRequestDto){
        User user = findById(userRepository, userRequestDto.id());
        return userToResponseDto(user);
    }


    private void validateDuplicateUsername(String username) {
        userRepository.findByUsername(username).ifPresent(u -> {
            throw new DuplicateUserException("이미 존재하는 사용자 이름입니다.");
        });
    }

    private void validateDuplicateEmail(String email) {
        userRepository.findByEmail(email).ifPresent(u -> {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        });
    }
}
