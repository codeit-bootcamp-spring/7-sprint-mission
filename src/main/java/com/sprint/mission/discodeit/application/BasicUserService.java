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
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


import static com.sprint.mission.discodeit.application.dto.UserDtoMapper.userToResponseDto;

@Service
@RequiredArgsConstructor
public class BasicUserService{


    private final UserRepository userRepository;

    private final FileService fileService;



    public UserResponseDto createUser(UserCreateRequestDto requestDto) throws IOException {
        validateDuplicateEmail(requestDto.email());
        validateDuplicateUsername(requestDto.username());


        User user = new User(
                requestDto.email(),
                requestDto.password(),
                requestDto.username(),
                requestDto.phoneNumber());
        userRepository.save(user);
        fileService.createUserFolder(user.getId());
        if (requestDto.profileImage() != null) {
            BinaryContent content = fileService.saveUserProfile(user.getId(), requestDto.profileImage());
            user.setProfile(content);

        }

        //유저 전용 폴더 생성 & 파일 저장


        return userToResponseDto(user);
    }

    public UserResponseDto updateUserInfo(UserUpdateDto updateDto) throws IOException {
        User user = findById(updateDto.id());
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
            BinaryContent content = fileService.saveUserProfile(user.getId(), updateDto.updateFile());
            user.setProfile(content);
        }
        userRepository.save(user);
        return userToResponseDto(user);
    }


    public void delete(UserRequestDto requestDto) {
        userRepository.remove(findById(requestDto.id()));
        fileService.deleteUserFolder(requestDto.id());
    }

    public UserResponseDto getUser(UserRequestDto userRequestDto){
        User user = findById(userRequestDto.id());
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

    public User findById( UUID userId) {
        return userRepository.findById(userId).orElseThrow(()->new NoSuchElementException("해당 유저가 존재하지 않습니다."));
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(()->new NoSuchElementException("아이디가 틀렸습니다"));
    }


    public List<User> findAll(UserRepository userRepository) {
        return userRepository.findAll();
    }

    public void login(String loginId, String password) {
        User user = findByUsername(loginId);
        if (!user.getPassword().equals(password)){
            throw new IllegalArgumentException("비밀번호가 틀립니다");
        }
        user.markOnline();
        userRepository.save(user);
    }

    public void logout(UUID userId){
        User user = findById(userId);
        user.markOffline();
        userRepository.save(user);
    }

    public boolean checkUserOnline(UUID userId){
        User user = findById(userId);
        return user.checkOnline();
    }
}
