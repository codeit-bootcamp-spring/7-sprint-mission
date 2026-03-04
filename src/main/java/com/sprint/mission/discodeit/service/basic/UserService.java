package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.exception.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.dto.dto_Neo.BinaryContentCreatedEvent;
import com.sprint.mission.discodeit.dto.dto_Neo.UserUpdateRequest;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.dto.dto_Neo.UserDto;
import com.sprint.mission.discodeit.repository.jpa.BinaryContentsRepository;
import com.sprint.mission.discodeit.repository.jpa.UsersRepository;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.InterfaceUserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
//@Transactional // 영속성 컨텍스트
//!! final 필드나 @NonNull 어노테이션이 붙은 필드에 대한 생성자를 자동으로 생성
@RequiredArgsConstructor //@Repository 있어야 등록시켜 줌!!
public class UserService implements InterfaceUserService {
    private final UsersRepository userRepository;
    private final BinaryContentsRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final SessionRegistry sessionRegistry;
    private final AuthService authService;
    private final ApplicationEventPublisher eventPublisher;

//    @Autowired
//    EntityManager em;

//    public UserService(FileUserRepository fileUserRepository) {
//        this.fileUserRepository = fileUserRepository;
//    }
//  🍎의존성 주입 방법
//    1. 생성자 주입(Constructor Injection) : 생성자가 1개만 있을 경우에 @Autowired를 생략 가능
//    2. 필드 주입(Field Injection) : Setter 메서드를 사용
//    3. 수정자 주입(Setter Injection) : 간단하지만 테스트 어려워서 지양

    //    @PreAuthorize("hasRole('USER')")
    @Transactional
    @Override
    @CacheEvict(cacheNames = "users", allEntries = true)
    public UserDto create(UserCreateRequest userCreateRequest, Optional<MultipartFile> optionalProfileFile) {
//    public User create(String newUsername, Optional<BufferedImage> profileImageBytes) {
//        [ ] 선택적으로 프로필 이미지를 같이 등록할 수 있습니다.
//        [ ] DTO를 활용해 파라미터를 그룹화합니다.
//                유저를 등록하기 위해 필요한 파라미터, 프로필 이미지를 등록하기 위해 필요한 파라미터 등
//        [ ] username과 email은 다른 유저와 같으면 안됩니다.
//        [ ] UserStatus를 같이 생성합니다.
        if (userRepository.findUserByUsername(userCreateRequest.username()).isPresent()) {
            throw  new UserAlreadyExistsException("userName", userCreateRequest.username());
        }

        if(userRepository.findUserByEmail(userCreateRequest.email()).isPresent()) {
            throw  new UserAlreadyExistsException("email", userCreateRequest.email());
        }

        BinaryContent profile = optionalProfileFile
            .map(file -> {
                BinaryContent binaryContent = new BinaryContent(file);
                BinaryContent savedBinaryContent = binaryContentRepository.save(binaryContent);
                eventPublisher.publishEvent(new BinaryContentCreatedEvent(savedBinaryContent.getId(), file));

                return savedBinaryContent;
            })
            .orElse(null);

        String password = userCreateRequest.password();
        String encodePassword = passwordEncoder.encode(password); //!! 🛠️

        User newUser = new User(userCreateRequest.username(),
            userCreateRequest.email(),
            encodePassword,
            profile);

        userRepository.save(newUser);

        return userMapper.toDto(newUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto find(UUID userID) {
//        [ ] 사용자의 온라인 상태 정보를 같이 포함하세요.
//        [ ] 패스워드 정보는 제외하세요.
        User user = userRepository
            .findById(userID)
            .orElseThrow(() -> new UserNotFoundException(userID));

        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "users")
    public List<UserDto> findAll() {
//        DTO를 활용하여:
//        [ ] 사용자의 온라인 상태 정보를 같이 포함하세요. =>온라인 상태를 findAll()에 포함하고 있다면 로그인/로그아웃 때도 반드시 캐시 삭제해야 함.
//        [ ] 패스워드 정보는 제외하세요.

        return userRepository.findAll().stream()
            .map(userMapper::toDto)
            .toList();
    }

    private List<DiscodeitUserDetails> getDiscodeitUserDetails(User user) {

        return sessionRegistry.getAllPrincipals()
            .stream()
            .filter(DiscodeitUserDetails.class::isInstance)
            .map(DiscodeitUserDetails.class::cast)
            .filter(userDetails -> userDetails.getUser().id().equals(user.getId()))
//                .map(discodeitUserDetail -> dtoList.add(discodeitUserDetail.getUserDto()))
            .toList();
    }

    @Transactional
    @Override
    @PreAuthorize("#userId == authentication.principal.user.id") // SpEL
    @CacheEvict(cacheNames = "users", allEntries = true)
    public UserDto update(UUID userId, UserUpdateRequest dtoUserUpdate, Optional<MultipartFile> optionalProfileFile) {
//        [ ] 선택적으로 프로필 이미지를 대체할 수 있습니다.
//        [ ] DTO를 활용해 파라미터를 그룹화합니다.
//        수정 대상 객체의 readStatusID 파라미터, 수정할 값 파라미터

        log.info("✅ UserService.update.id = [" + userId.toString() + "] / dto_userCreate = [" + dtoUserUpdate.toString() + "]");

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

        String userName = user.getUsername();

        if (!userId.equals(user.getId())
                && userRepository.findUserByUsername(dtoUserUpdate.newUsername()).isPresent()) {
            throw new UserAlreadyExistsException("🚨username", dtoUserUpdate.newUsername());
        }

        if (!userId.equals(user.getId())
                && userRepository.findUserByEmail(dtoUserUpdate.newEmail()).isPresent()) {
            throw new UserAlreadyExistsException("🚨email", dtoUserUpdate.newEmail());
        }

        BinaryContent profile = optionalProfileFile.map(file -> {
                BinaryContent neoBinaryContent = binaryContentRepository
                    .findById(userId)
                    .orElse( new BinaryContent(file));

                eventPublisher.publishEvent(new BinaryContentCreatedEvent(neoBinaryContent.getId(), file));

                return neoBinaryContent;
            })
            .orElse(null);

        user.updateFrom(dtoUserUpdate, profile);

        log.info("✅ UserService.update = [" + userName + "]를 [" + dtoUserUpdate.newUsername() + "]로 변경 완료");

        return userMapper.toDto(user);
    }

    @Transactional
    @Override
    @PreAuthorize("#userID == authentication.principal.user.id") // SpEL
    @CacheEvict(cacheNames = "users", allEntries = true)
    public void delete(UUID userID) {
//        [ ] 관련된 도메인도 같이 삭제합니다.
        User user = userRepository.findById(userID)
            .orElseThrow(() -> new UserNotFoundException(userID));

//        Optional<UserStatus> optionalStatus = userStatusRepository.findUserStatusByUserId(user.getId());
//        if (optionalStatus.isPresent()) {
//            userStatusRepository.deleteById(optionalStatus.get().getId());
//            log.info("✅ UserService.userStatusRepository.deleteById = [" + user.getUsername() + "]");
//        }


        if (user.getProfile() != null && user.getProfile().getId() != null) {
            Optional<BinaryContent> optionalContents = binaryContentRepository.findById(user.getProfile().getId());
            if (optionalContents.isPresent()) {
                binaryContentRepository.deleteById(optionalContents.get().getId());
                log.info("✅ UserService.binaryContentRepository.deleteById = [" + user.getUsername() + "]");
            }
//            else {
//                log.error("🚨UserService.binaryContentRepository.deleteById = [" + user.getUserName() + "]", e);
//            }
        }

        userRepository.deleteById(userID);
        log.info("✅ ⛔️ UserService.userRepository.deleteById [" + user.getUsername() + "] 완료 ️⛔️");
    }
}
