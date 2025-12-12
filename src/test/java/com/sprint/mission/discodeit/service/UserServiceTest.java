package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.binarycontent.BinaryContentService;
import com.sprint.mission.discodeit.service.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.service.dto.response.UserDto;
import com.sprint.mission.discodeit.service.mapper.UserMapper;
import com.sprint.mission.discodeit.service.mapper.UserStatusMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.BDDAssumptions.given;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService 테스트")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private BinaryContentService binaryContentService;
    @Mock
    private ReadStatusRepository readStatusRepository;

    @Spy
    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Spy
    private final UserStatusMapper userStatusMapper = Mappers.getMapper(UserStatusMapper.class);

    @InjectMocks
    private UserService userService;

    @Nested
    @DisplayName("유정 생성")
    class createUser {

        @Test
        @DisplayName("유저 생성 성공")
        void createUserSuccess (){
            //given
            UserCreateRequest request = new UserCreateRequest("test@gmail.com", "1234", "test");
            when(userRepository.existsByEmail("test@gmail.com"))
                    .thenReturn(false);
            when(userRepository.existsByUsername("test"))
                    .thenReturn(false);
            User user = new User("test@gmail.com", "1234", "test");
            when(userRepository.save(any(User.class)))
                    .thenReturn(user);
//            BinaryContent binaryContent = new BinaryContent(null, null, null, 1);


            //when
            UserDto userDto = userService.createUser(request, null);

            //then

         }
    }
}