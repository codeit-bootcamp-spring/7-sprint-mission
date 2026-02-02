package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.response.user.UserDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.exception.domain.DiscodeitException;
import com.sprint.mission.discodeit.exception.domain.file.FileByteReadFailException;
import com.sprint.mission.discodeit.exception.domain.user.UserNotExistException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final UserMapper userMapper;
    private final BinaryContentStorage binaryContentStorage;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDto createUser(UserCreateRequestDto userCreateRequestDto, MultipartFile profile)  {
        log.debug("createUser : {} Siuuuuu!!!",userCreateRequestDto);
        String password = passwordEncoder.encode(userCreateRequestDto.password());
    if(profile!=null) {
        BinaryContent binaryContent = binaryContentRepository.save(
                new BinaryContent(profile.getName(), profile.getContentType(), profile.getSize())
        );

        try {
            binaryContentStorage.put(binaryContent.getId(),profile.getBytes());
        } catch (Exception e) {
            throw new FileByteReadFailException(profile.getName());
        }

        User user = userRepository.save(User.createUserWithProfileFactory(
                userCreateRequestDto.username(),
                userCreateRequestDto.email(),
               password,
                binaryContent
        ));



        return userMapper.toDto(user);
    }
        User user = userRepository.save(
                User.createUserFactory(userCreateRequestDto.username()
                        , userCreateRequestDto.email()
                        , password)
        );
        return userMapper.toDto(user);
    }

    @Override
    public UserDto readUser(UUID userID) {
        User user = userRepository.findById(userID).orElseThrow(() -> new UserNotExistException(userID));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> readAllUser() {
        return userRepository.findAll().stream().map(userMapper::toDto).toList();
    }

    @Override
    public void deleteUser(UUID userId) {
        if (!userRepository.existsById(userId)) throw new UserNotExistException(userId);
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
    public UserDto patchUser(UUID userId, UserUpdateRequest dto, MultipartFile profile)  {

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotExistException(userId));
        user.setUserName(dto.newUsername()==null?user.getUserName():dto.newUsername());
        user.setPassword(dto.newPassword()==null?user.getPassword():dto.newPassword());
        user.setEmail(dto.newEmail()==null?user.getEmail():dto.newEmail());
        if(profile!=null) {
            if(user.getProfile()!=null)  binaryContentRepository.delete(user.getProfile());
            BinaryContent tmpBinaryContent = binaryContentRepository.save(
                    new BinaryContent(profile.getName(), profile.getContentType(), profile.getSize())
            );
            try {
                binaryContentStorage.put(tmpBinaryContent.getId(),profile.getBytes());
            } catch (Exception e) {
                throw new FileByteReadFailException(profile.getName());
            }
            user.setProfile(tmpBinaryContent);
        }
        userRepository.save(user);
        log.debug("patchUser : {} siuuuuuuuu!!!",user);
        return userMapper.toDto(user);
    }


}
