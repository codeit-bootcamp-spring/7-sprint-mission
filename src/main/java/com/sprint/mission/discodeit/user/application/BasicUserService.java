package com.sprint.mission.discodeit.user.application;


import com.sprint.mission.discodeit.auth.AuthManager;
import com.sprint.mission.discodeit.friendrequest.domain.FriendRequest;
import com.sprint.mission.discodeit.friendrequest.application.FriendRequestRepository;
import com.sprint.mission.discodeit.friendship.domain.FriendShip;
import com.sprint.mission.discodeit.friendship.application.FriendShipRepository;
import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.user.domain.exception.DuplicateUserException;
import com.sprint.mission.discodeit.user.presentation.UserService;
import com.sprint.mission.discodeit.user.presentation.dto.request.UserCreateRequestDto;
import com.sprint.mission.discodeit.user.presentation.dto.request.UserRequestDto;
import com.sprint.mission.discodeit.user.presentation.dto.request.UserUpdateDto;
import com.sprint.mission.discodeit.user.presentation.dto.response.UserResponseDto;
import com.sprint.mission.discodeit.userstatus.application.UserStatusRepository;
import com.sprint.mission.discodeit.userstatus.domain.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {


    private final UserRepository userRepository;
    private final FriendRequestRepository requestRepository;
    private final FriendShipRepository friendShipRepository;
    private final UserStatusRepository userStatusRepository;
    private final AuthManager authManager;




    @Override
    public UserResponseDto createUser(UserCreateRequestDto requestDto) {
        checkDuplicateEmail(requestDto.email());
        checkDuplicateUsername(requestDto.username());
        User user = userCreateDtoToUser(requestDto);
        save(user);
        createUserStatue(user.getId());
        return userToResponseDto(user);
    }

    @Override
    public UserResponseDto getUser(UserRequestDto requestDto) {
        User user = findByUsername(requestDto.username());
        return userToResponseDto(user);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return findAll().stream().map(this::userToResponseDto).toList();
    }

    @Override
    public UserResponseDto updateUserInfo(UserUpdateDto updateDto) {
        User user = findById(updateDto.id());
        if(updateDto.username()!=null){
            user.updateUsername(updateDto.username());
        }
        if(updateDto.password()!=null){
            user.updatePassword(updateDto.password());
        }
        if(updateDto.phoneNumber()!=null){
            user.updatePhoneNumber(updateDto.phoneNumber());
        }
        save(user);
        return userToResponseDto(user);
    }

    @Override
    public void delete(UserRequestDto requestDto) {
        remove(requestDto.id());
        removeUserStatusById(requestDto.id());
    }

    //간단하게 crud하는 로직이지만 우선은 메서드의 책임을 분리해보고 싶었음.
    private void save(User user) {
        userRepository.save(user);
        System.out.println("유저 저장 성공");
    }


    private void remove(UUID id) {
        userRepository.remove(findById(id));
        System.out.println("유저 삭제 성공");
    }


    private User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));
    }


    private List<User> findAll() {
        return userRepository.findAll();
    }
    //

    //이메일로 유저 정보 가져오기
    private User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));
    }

    private User findByUsername(String username) {
        return userRepository.findByEmail(username).orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));
    }

    //애매 이거 수정
    public FriendRequest sendFriendRequest(User sender, User target) {
        validateNotAlreadyFriend(sender, target);
        validateNotDuplicateRequest(sender, target);
        FriendRequest friendRequest = sender.sendFriendRequestTo(target);
        requestRepository.save(friendRequest);
        return friendRequest;

    }

    //애매 이거 수정
    public List<FriendRequest> getSentFriendRequests(User user) {
        return requestRepository.findAll().stream().filter(r -> r.getSenderId().equals(user.getId())).toList();
    }

    //애매 이거 수정
    public List<FriendRequest> getReceivedFriendRequests(User user) {
        return requestRepository.findAll().stream().filter(r -> r.getReceiverId().equals(user.getId())).toList();
    }

    //애매 이거 수정
    public void acceptFriendRequest(User receiver, FriendRequest request) {
        FriendShip friendShip = receiver.acceptFriendRequest(request);
        requestRepository.remove(request);
        friendShipRepository.save(friendShip);
    }


    private void validateNotDuplicateUser(String email) {
        userRepository.findByEmail(email).ifPresent(u -> {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        });
    }



    // 이미 친구인지 검사
    private void validateNotAlreadyFriend(User sender, User target) {
        boolean alreadyFriends =
                friendShipRepository.findAll().stream()
                        .anyMatch(fs ->
                                (fs.getUserAId().equals(sender.getId()) && fs.getUserBId().equals(target.getId())) ||
                                        (fs.getUserAId().equals(target.getId()) && fs.getUserBId().equals(sender.getId())));
        if (alreadyFriends) {
            throw new IllegalStateException("이미 친구 관계입니다");
        }
    }

    // 중복 요청 검사
    private void validateNotDuplicateRequest(User sender, User target) {
        boolean alreadySent = requestRepository.findAll().stream()
                .anyMatch(req -> req.getSenderId().equals(sender.getId()) &&

                        req.getReceiverId().equals(target.getId()));
        if (alreadySent) {
            throw new IllegalStateException("이미 친구 요청을 보냈습니다");
        }
    }


    private void checkDuplicateUsername(String username) {
        userRepository.findByUsername(username).ifPresent(u -> {
            throw new DuplicateUserException("이미 존재하는 사용자 이름입니다.");
        });
    }

    private void checkDuplicateEmail(String email) {
        userRepository.findByEmail(email).ifPresent(u -> {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        });
    }

    private User userCreateDtoToUser(UserCreateRequestDto requestDto){
        return User.create(
                requestDto.email(),
                requestDto.password(),
                requestDto.username(),
                requestDto.phoneNumber()
        );
    }
    private UserResponseDto userToResponseDto(User user){
        return new UserResponseDto(user.getEmail(), user.getUsername(),user.getPhoneNumber());
    }

    //UserStatus 관련 CRUD
    private UserStatus createUserStatue(UUID userId){
        UserStatus userStatus = UserStatus.create(userId);
        userStatusRepository.save(userStatus);
        return userStatus;
    }

    private UserStatus findUserStatusByUserId(UUID id){
        return userStatusRepository.findByUserId(id).orElse(null);
    }

    private void removeUserStatusById(UUID userId){
        userStatusRepository.findByUserId(userId);
    }

}
