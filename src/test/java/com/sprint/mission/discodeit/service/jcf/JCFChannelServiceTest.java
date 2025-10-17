package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.util.ValidateOperator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;


class JCFChannelServiceTest {


    private JCFChannelService jcfChannelService;
    private ChannelRepository channelRepository;
    private ValidateOperator validateOperator;
    private UserRepository userRepository;
    private MessageRepository messageRepository;

    @BeforeEach
    public void before(){
       messageRepository = new JCFMessageRepository();
       userRepository = new JCFUserRepository();
        channelRepository = new JCFChannelRepository();
        validateOperator = new ValidateOperator(channelRepository, userRepository, messageRepository);

        jcfChannelService = new JCFChannelService( channelRepository, validateOperator, userRepository);
        System.out.println("before");
    }

    @Test
    @DisplayName("createChannel 테스트")
    public void test_createChannel(){
      //Given

        ChannelDto channelDto = new ChannelDto("JAva","Javastack",false,true);

        //When

       jcfChannelService.createChannel(channelDto);

        //Then

        Assertions.assertThat(channelDto.getName()).isEqualTo("JAva");

    }

    @AfterEach
    public void after(){
        System.out.println("after");
    }

}