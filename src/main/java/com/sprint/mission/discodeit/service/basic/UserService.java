package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.Util;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.dto.Dto_BinaryContent;
import com.sprint.mission.discodeit.entity.dto.Dto_User;
import com.sprint.mission.discodeit.entity.dto.Res_IsOnlineUser;
import com.sprint.mission.discodeit.entity.dto.Res_User;
import com.sprint.mission.discodeit.repository.InterfaceBinaryContentRepository;
import com.sprint.mission.discodeit.repository.InterfaceUserRepository;
import com.sprint.mission.discodeit.repository.InterfaceUserStatusRepository;
import com.sprint.mission.discodeit.service.InterfaceUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
//!! final 필드나 @NonNull 어노테이션이 붙은 필드에 대한 생성자를 자동으로 생성
@RequiredArgsConstructor //@Repository 있어야 등록시켜 줌!!
public class UserService implements InterfaceUserService {
    private final InterfaceUserRepository userRepository;
    private final InterfaceUserStatusRepository userStatusRepository;
    private final InterfaceBinaryContentRepository binaryContentRepository;

//    public UserService(FileUserRepository fileUserRepository) {
//        this.fileUserRepository = fileUserRepository;
//    }
//  🍎의존성 주입 방법
//    1. 생성자 주입(Constructor Injection) : 생성자가 1개만 있을 경우에 @Autowired를 생략 가능
//    2. 필드 주입(Field Injection) : Setter 메서드를 사용
//    3. 수정자 주입(Setter Injection) : 간단하지만 테스트 어려워서 지양

    @Override
    public Res_User create(Dto_User dto_user, Optional<Dto_BinaryContent> requestDto) {
//    public User create(String userName, Optional<BufferedImage> profileImageBytes) {
//        [ ] 선택적으로 프로필 이미지를 같이 등록할 수 있습니다.
//        [ ] DTO를 활용해 파라미터를 그룹화합니다.
//                유저를 등록하기 위해 필요한 파라미터, 프로필 이미지를 등록하기 위해 필요한 파라미터 등
//        [ ] username과 email은 다른 유저와 같으면 안됩니다.
//        [ ] UserStatus를 같이 생성합니다.
        if (userRepository.isUsingName(dto_user.userName())) {
            throw new IllegalArgumentException("🚨create : 동일한 username [" + dto_user.userName() + "] 사용 중");
        }

        if(userRepository.isUsingEmail(dto_user.eMail())) {
            throw new IllegalArgumentException("🚨create : 동일한 eMail [" + dto_user.eMail() + "] 사용 중");
        }

        UUID binaryContentId = null;

        if (requestDto.isPresent()) {
            Dto_BinaryContent dtoBinaryContent = requestDto.get();
            BinaryContent binaryContent = new BinaryContent(dtoBinaryContent);
            binaryContentId = binaryContent.getId();
            binaryContentRepository.save(binaryContent);
        }

        User user = new User(dto_user, binaryContentId);
        Res_User resUser = Res_User.from(user);
        userRepository.save(user);

        UserStatus userStatus = new UserStatus(user.getId());
        userStatusRepository.save(userStatus);
        Util.okMessage("UserService.create = [" + user.getUserName() + "] 온라인 상태 = [" + userStatus.isOnline() + "]");;

        return resUser;
    }

    @Override
    public Res_IsOnlineUser find(UUID userID) {
//        DTO를 활용하여:
//        [ ] 사용자의 온라인 상태 정보를 같이 포함하세요.
//        [ ] 패스워드 정보는 제외하세요.
        String message = "find.userID = [" + userID.toString() + "] 오류";
        User user = userRepository.findById(userID).orElseThrow(() -> new IllegalArgumentException(message));

//        Util.okMessage("♣️user.readStatusID() = [" + user.getId() + "]");
        UserStatus userStatus = userStatusRepository.findByUserId(userID).orElseThrow(() -> new IllegalArgumentException(message));

        Util.okMessage("UserService.findAllByChannleId = [" + user.getUserName() + "] isOnline = [" + userStatus.isOnline() + "]");

        return Res_IsOnlineUser.from(user, userStatus.isOnline());
    }

    @Override
    public List<Res_IsOnlineUser> findAll() {
//        DTO를 활용하여:
//        [ ] 사용자의 온라인 상태 정보를 같이 포함하세요.
//        [ ] 패스워드 정보는 제외하세요.
        List<Res_IsOnlineUser> dtoList = new ArrayList<>(){};
        Optional<List<User>> optionalUsers = userRepository.findAll();

        if (optionalUsers.isEmpty()) {
            throw new IllegalArgumentException("🚨UserService.findAll: 등록된 user 가 없습니다.");
        }

        List<User> users = optionalUsers.get();
        Optional<List<UserStatus>> optionalUserStatus = userStatusRepository.findAll();

        if (optionalUserStatus.isPresent()) {
            for (User user : users) {
                List<UserStatus> userStatusList = optionalUserStatus.get();
                Optional<UserStatus> userStatus = userStatusList.stream().filter(status -> status.getUserId() != null &&
                        status.getUserId().equals(user.getId())).findFirst();
                if (userStatus.isPresent()) {
                    Res_IsOnlineUser dto = Res_IsOnlineUser.from(user, userStatus.get().isOnline());
                    dtoList.add(dto);
                    Util.okMessage("UserService.findAll = [" + user.getUserName() + "] isOnline = [" + userStatus.get().isOnline() + "]");
                } else {
                    Res_IsOnlineUser dto = Res_IsOnlineUser.from(user, false);
                    dtoList.add(dto);
                    // optionUserStatus 없는 경우 기본값(offline)으로 처리
                    Util.errMessage("UserService.findAll: User [" + user.getUserName() + "]의 UserStatus가 없습니다. 기본값(offline)으로 처리합니다.");
                }
            }
        }

        return dtoList;
    }

    @Override
    public Res_User update(UUID userId, Dto_User dto_user, Optional<Dto_BinaryContent> requestDto_Content) {
//        [ ] 선택적으로 프로필 이미지를 대체할 수 있습니다.
//        [ ] DTO를 활용해 파라미터를 그룹화합니다.
//        수정 대상 객체의 readStatusID 파라미터, 수정할 값 파라미터

        Util.okMessage("UserService.update.userId = [" + userId + "]");

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("🚨UserService.update.userId = [" + userId + "] 오류"));
        String userName = user.getUserName();

        if (!userId.equals(user.getId())
                && userRepository.isUsingName(dto_user.userName())) {
            throw new IllegalArgumentException("🚨 UserService.update = [" + dto_user.userName() + "]은 이미 사용중인 user name 입니다");
        }

        if (!userId.equals(user.getId())
                && userRepository.isUsingEmail(dto_user.eMail())) {
            throw new IllegalArgumentException("🚨 UserService.update = [" + dto_user.eMail() + "]은 이미 사용중인 eMail 입니다");
        }

        //!! 선택적으로 프로필 이미지를 대체할 수 있습니다.
        if (requestDto_Content != null && requestDto_Content.isPresent()) {
            BinaryContent neoBinaryContent = binaryContentRepository.findById(userId).orElse(new BinaryContent(requestDto_Content.get()));
            binaryContentRepository.save(neoBinaryContent);
            //!! 순서 유의_I
            user.updateUser(dto_user.userName(), dto_user.password(), dto_user.eMail(), neoBinaryContent.getId());
        }
        else {
            user.updateUser(dto_user.userName(), dto_user.password(), dto_user.eMail(), null);
        }

        //!! 순서 유의_II
        userRepository.save(user);
        Util.okMessage("UserService.update = [" + userName + "]를 [" + dto_user.userName() + "]로 변경 완료");
        return Res_User.from(user);
    }

    @Override
    public void delete(UUID userID) {
//        [ ] 관련된 도메인도 같이 삭제합니다.
//        BinaryContent(프로필), UserStatus

        User user = userRepository.findById(userID).orElseThrow(() -> new IllegalArgumentException("🚨deleteModel 오류 - [" + userID + "]"));
        Optional<UserStatus> optionalStatus = userStatusRepository.findByUserId(user.getId());
        if (optionalStatus.isPresent()) {
            userStatusRepository.deleteById(optionalStatus.get().getId());
            Util.okMessage("UserService.userStatusRepository.deleteById = [" + user.getUserName() + "]");
        }
//        else {
//            Util.errMessage("UserService.userStatusRepository.deleteById = [" + user.getUserName() + "]");
//        }

        if (user.getProfileId() != null) {
            Optional<BinaryContent> optionalContents = binaryContentRepository.findById(user.getProfileId());
            if (optionalContents.isPresent()) {
                binaryContentRepository.deleteById(optionalContents.get().getId());
                Util.okMessage("UserService.binaryContentRepository.deleteById = [" + user.getUserName() + "]");
            }
//            else {
//                Util.errMessage("UserService.binaryContentRepository.deleteById = [" + user.getUserName() + "]");
//            }
        }

        userRepository.deleteById(userID);
        Util.okMessage("⛔️ UserService.userRepository.deleteById [" + user.getUserName() + "] 완료 ️⛔️");
    }
}
