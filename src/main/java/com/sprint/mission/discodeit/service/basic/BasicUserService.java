package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.response.user.UserDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.service.util.StaticString.USER_NOT_EXIST;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;
    private final UserMapper userMapper;
    private final BinaryContentStorage binaryContentStorage;

    @Override
    @Transactional
    public UserDto createUser(UserCreateRequestDto userCreateRequestDto, MultipartFile profile) throws IOException {
        log.debug("createUser : {} Siuuuuu!!!",userCreateRequestDto);
    if(profile!=null) {
        BinaryContent binaryContent = binaryContentRepository.save(
                new BinaryContent(profile.getName(), profile.getContentType(), profile.getSize())
        );

        binaryContentStorage.put(binaryContent.getId(),profile.getBytes());

        User user = userRepository.save(User.createUserWithProfileFactory(
                userCreateRequestDto.username(),
                userCreateRequestDto.email(),
                userCreateRequestDto.password(),
                binaryContent
        ));

        user.setUserStatus(userStatusRepository.save(
                new UserStatus(user,Instant.now())
        ));


        return userMapper.toDto(user);
    }
        User user = userRepository.save(
                User.createUserFactory(userCreateRequestDto.username()
                        , userCreateRequestDto.email()
                        , userCreateRequestDto.password())
        );

        UserStatus targetUserStatus = userStatusRepository.save(
                new UserStatus(user,Instant.now())
        );
        user.setUserStatus(targetUserStatus);
        return userMapper.toDto(user);
    }

    @Override
    public UserDto readUser(UUID userID) {
        User user = userRepository.findById(userID).orElseThrow(() -> new IllegalArgumentException(USER_NOT_EXIST));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> readAllUser() {
        return userRepository.findAll().stream().map(userMapper::toDto).toList();
    }

    @Override
    public void deleteUser(UUID userId) {

        log.info("deleteUser : {} info layer",userId);
        userRepository.deleteById(userId);
    }

    @Override
    public void resetUserRepository() {
        userRepository.deleteAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAllUsers(){
        return userRepository.findAll().stream().map(userMapper::toDto).toList();
    }

    @Override
    @Transactional
    public UserDto patchUser(UUID userId, UserUpdateRequest dto, MultipartFile profile) throws IOException {

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException(USER_NOT_EXIST));
        user.setUserName(dto.newUsername()==null?user.getUserName():dto.newUsername());
        user.setPassword(dto.newPassword()==null?user.getPassword():dto.newPassword());
        user.setEmail(dto.newEmail()==null?user.getEmail():dto.newEmail());
        if(profile!=null) {
            if(user.getProfile()!=null)  binaryContentRepository.delete(user.getProfile());
            BinaryContent tmpBinaryContent = binaryContentRepository.save(
                    new BinaryContent(profile.getName(), profile.getContentType(), profile.getSize())
            );
            binaryContentStorage.put(tmpBinaryContent.getId(),profile.getBytes());
            user.setProfile(tmpBinaryContent);
        }
        userRepository.save(user);
        log.debug("patchUser : {} siuuuuuuuu!!!",user);
        return userMapper.toDto(user);
    }


}
