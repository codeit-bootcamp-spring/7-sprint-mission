package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.mapper.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.mapper.dto.UserDto;
import com.sprint.mission.discodeit.repository.jpa.BinaryContentsRepository;
import com.sprint.mission.discodeit.repository.jpa.UserStatusesRepository;
import com.sprint.mission.discodeit.repository.jpa.UsersRepository;
import com.sprint.mission.discodeit.service.InterfaceUserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@Transactional // 영속성 컨텍스트
//!! final 필드나 @NonNull 어노테이션이 붙은 필드에 대한 생성자를 자동으로 생성
@RequiredArgsConstructor //@Repository 있어야 등록시켜 줌!!
public class UserService implements InterfaceUserService {
    private final UsersRepository userRepository;
    private final UserStatusesRepository userStatusRepository;
    private final BinaryContentsRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final UserMapper userMapper;

//    @Autowired
//    EntityManager em;

//    public UserService(FileUserRepository fileUserRepository) {
//        this.fileUserRepository = fileUserRepository;
//    }
//  🍎의존성 주입 방법
//    1. 생성자 주입(Constructor Injection) : 생성자가 1개만 있을 경우에 @Autowired를 생략 가능
//    2. 필드 주입(Field Injection) : Setter 메서드를 사용
//    3. 수정자 주입(Setter Injection) : 간단하지만 테스트 어려워서 지양

    @Override
    public UserDto create(UserCreateRequest userCreateRequest, Optional<MultipartFile> optionalProfileFile) {
        //log.info("🩷UserDto create");
//    public User create(String newUsername, Optional<BufferedImage> profileImageBytes) {
//        [ ] 선택적으로 프로필 이미지를 같이 등록할 수 있습니다.
//        [ ] DTO를 활용해 파라미터를 그룹화합니다.
//                유저를 등록하기 위해 필요한 파라미터, 프로필 이미지를 등록하기 위해 필요한 파라미터 등
//        [ ] username과 email은 다른 유저와 같으면 안됩니다.
//        [ ] UserStatus를 같이 생성합니다.
        if (userRepository.findUserByUsername(userCreateRequest.username()).isPresent()) {
            throw new IllegalArgumentException("🚨create : 동일한 newUsername [" + userCreateRequest.username() + "] 사용햐는 User 가 이미 존재함");
        }

        if(userRepository.findUserByEmail(userCreateRequest.email()).isPresent()) {
            throw new IllegalArgumentException("🚨create : 동일한 newEmail [" + userCreateRequest.email() + "] 사용햐는 User 가 이미 존재함");
        }

        BinaryContent profile = optionalProfileFile
            .map(file -> {
                BinaryContent binaryContent = new BinaryContent(
                    file.getOriginalFilename(),
                    file.getSize(),
                    file.getContentType(),
                    null
                );

                // 파일 저장
                binaryContentStorage.put(file, binaryContent);

                // DB 저장
                return binaryContentRepository.save(binaryContent);
            })
            .orElse(null);

        User newUser = new User(userCreateRequest.username(),
            userCreateRequest.email(),
            userCreateRequest.password(),
            profile);

        newUser.initUserStatus();

        userRepository.save(newUser);

        return userMapper.toDto(newUser);
    }

    @Override
    public UserDto find(UUID userID) {
        //log.info("🩷UserDto find");
//        [ ] 사용자의 온라인 상태 정보를 같이 포함하세요.
//        [ ] 패스워드 정보는 제외하세요.
        String message = "🚨 find.userID = [" + userID.toString() + "] 오류";
        User user = userRepository
            .findById(userID)
            .orElseThrow(() -> new IllegalArgumentException(message));

        UserStatus userStatus = userStatusRepository
            .findUserStatusByUserId(userID)
            .orElseThrow(() -> new IllegalArgumentException(message));

        log.info("✅ UserService.findAllByChannelId = [" + user.getUsername() + "] online = [" + userStatus.isOnline() + "]");

        return userMapper.toDto(user);
    }

    @Override
    public List<UserDto> findAll() {
        //log.info("🩷UserDto findAll");
//        DTO를 활용하여:
//        [ ] 사용자의 온라인 상태 정보를 같이 포함하세요.
//        [ ] 패스워드 정보는 제외하세요.
        List<UserDto> dtoList = new ArrayList<>(){};
        List<User> users = userRepository.findAll();
        List<UserStatus> userStatusList = userStatusRepository.findAll();

        for (User user : users) {
            UserStatus userStatus = userStatusList.stream()
                  .filter(status -> status.getUser().getId() != null
                      && status.getUser().getId().equals(user.getId()))
                  .findFirst()
                  .orElse(null);

            if (userStatus != null) {
                UserDto dto = userMapper.toDto(user);
                dtoList.add(dto);
                log.info("✅ UserService.findAll = [" + user.getUsername() + "] online = [" + userStatus.isOnline() + "]");
            }
        }

        return dtoList;
    }

    @Override
    public UserDto update(UUID userId, UserUpdateRequest dtoUserUpdate, Optional<MultipartFile> optionalProfileFile) {
        //log.info("🩷UserDto update");
//        [ ] 선택적으로 프로필 이미지를 대체할 수 있습니다.
//        [ ] DTO를 활용해 파라미터를 그룹화합니다.
//        수정 대상 객체의 readStatusID 파라미터, 수정할 값 파라미터

        log.info("✅ UserService.update.id = [" + userId.toString() + "] / dto_userCreate = [" + dtoUserUpdate.toString() + "]");

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NoSuchElementException("🚨User [" + userId.toString() + "]를 찾을 수 없음"));

        String userName = user.getUsername();

        if (!userId.equals(user.getId())
                && userRepository.findUserByUsername(dtoUserUpdate.newUsername()).isPresent()) {
            throw new IllegalArgumentException("🚨 같은 newUsername [" + dtoUserUpdate.newUsername() + "]을 사용하는 User가 이미 존재함");
        }

        if (!userId.equals(user.getId())
                && userRepository.findUserByEmail(dtoUserUpdate.newEmail()).isPresent()) {
            throw new IllegalArgumentException("🚨 같은 newEmail [" + dtoUserUpdate.newEmail() + "]을 사용하는 User가 이미 존재함");
        }

        BinaryContent profile = optionalProfileFile.map(file -> {
                BinaryContent neoBinaryContent = null;
                neoBinaryContent = binaryContentRepository
                    .findById(userId)
                    .orElse(
                        new BinaryContent(
                        file.getOriginalFilename(),
                        file.getSize(),
                        file.getContentType(),
                        null
                    ));

                // 파일 저장
                binaryContentStorage.put(file, neoBinaryContent);

                // DB 저장
                return binaryContentRepository.save(neoBinaryContent);
            })
            .orElse(null);

        user.setProfile(profile);
        user.setUsername(dtoUserUpdate.newUsername());
        user.setEmail(dtoUserUpdate.newEmail());

        if (null != dtoUserUpdate.newPassword()) {
            user.setPassword(dtoUserUpdate.newPassword());
        }

        //!! 순서 유의_II
        userRepository.save(user);
        log.info("✅ UserService.update = [" + userName + "]를 [" + dtoUserUpdate.newUsername() + "]로 변경 완료");

        return userMapper.toDto(user);
    }

    @Override
    public void delete(UUID userID) {
        //log.info("🩷UserDto delete");
//        [ ] 관련된 도메인도 같이 삭제합니다.
        User user = userRepository.findById(userID)
            .orElseThrow(() -> new NoSuchElementException("🚨User [" + userID.toString() + "] 를 찾을 수 없음"));

        Optional<UserStatus> optionalStatus = userStatusRepository.findUserStatusByUserId(user.getId());
        if (optionalStatus.isPresent()) {
            userStatusRepository.deleteById(optionalStatus.get().getId());
            log.info("✅ UserService.userStatusRepository.deleteById = [" + user.getUsername() + "]");
        }
//        else {
//            log.error("🚨UserService.userStatusRepository.deleteById = [" + user.getUserName() + "]");
//        }

        if (user.getProfile().getId() != null) {
            Optional<BinaryContent> optionalContents = binaryContentRepository.findById(user.getProfile().getId());
            if (optionalContents.isPresent()) {
                binaryContentRepository.deleteById(optionalContents.get().getId());
                log.info("✅ UserService.binaryContentRepository.deleteById = [" + user.getUsername() + "]");
            }
//            else {
//                log.error("🚨UserService.binaryContentRepository.deleteById = [" + user.getUserName() + "]");
//            }
        }

        userRepository.deleteById(userID);
        log.info("✅ ⛔️ UserService.userRepository.deleteById [" + user.getUsername() + "] 완료 ️⛔️");
    }
}
