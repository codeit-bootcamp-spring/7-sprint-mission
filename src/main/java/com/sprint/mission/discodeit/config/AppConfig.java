package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.ValidateService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.service.util.ValidateOperator;

public class AppConfig {
    private JCFChannelRepository jcfChannelRepository;
    private JCFMessageRepository jcfMessageRepository;
    private JCFUserRepository jcfUserRepository;

    private FileChannelRepository fileChannelRepository;
    private FileMessageRepository fileMessageRepository;
    private FileUserRepository fileUserRepository;

    private ValidateService fileValidateService;
    private ValidateService jcfValidateService;

    private ChannelService fileBasicChannelService;
    private ChannelService jcfBasicChannelService ;

    private UserService fileBasicUserService ;
    private UserService jcfBasicUserService ;

    private MessageService fileBasicMessageService ;
    private MessageService jcfBasicMessageService ;

  private UserDto user1Dto = new UserDto("황준영","hwang","genius5375@gmail.com",true);
   private UserDto user2Dto = new UserDto("대상혁","Faker" ,"faker@riot.org" , false);
   private UserDto notInUserDBUserDto = new UserDto("신창섭", "정상화","maple.org" ,true);

  private MessageDto m1Dto = new MessageDto("Hello", user1Dto, false);
   private MessageDto m2Dto = new MessageDto("Hi I am Faker", user2Dto, false);
   private MessageDto m3Dto = new MessageDto("JAVA 를 정상화하네", notInUserDBUserDto, false);

   private ChannelDto channel1Dto = new ChannelDto("JAVA","JAVA 안전자산 놀이터",true,true);
   private ChannelDto channel2Dto = new ChannelDto("Git","Git fork 선봉자의 모임",false,false);
   private ChannelDto channel3Dto = new ChannelDto("던파", "던악귀의 모임", true, false);

    public ChannelService getFileBasicChannelService() {
        return fileBasicChannelService;
    }

    public ChannelService getJcfBasicChannelService() {
        return jcfBasicChannelService;
    }

    public UserService getFileBasicUserService() {
        return fileBasicUserService;
    }

    public UserService getJcfBasicUserService() {
        return jcfBasicUserService;
    }

    public MessageService getFileBasicMessageService() {
        return fileBasicMessageService;
    }

    public MessageService getJcfBasicMessageService() {
        return jcfBasicMessageService;
    }

    public UserDto getUser1Dto() {
        return user1Dto;
    }

    public UserDto getUser2Dto() {
        return user2Dto;
    }

    public UserDto getNotInUserDBUserDto() {
        return notInUserDBUserDto;
    }

    public MessageDto getM1Dto() {
        return m1Dto;
    }

    public MessageDto getM2Dto() {
        return m2Dto;
    }

    public MessageDto getM3Dto() {
        return m3Dto;
    }

    public ChannelDto getChannel1Dto() {
        return channel1Dto;
    }

    public ChannelDto getChannel2Dto() {
        return channel2Dto;
    }

    public ChannelDto getChannel3Dto() {
        return channel3Dto;
    }

    public AppConfig(){
        jcfChannelRepository = new JCFChannelRepository();
        jcfMessageRepository = new JCFMessageRepository();
        jcfUserRepository = new JCFUserRepository();
        fileChannelRepository = new FileChannelRepository();
        fileMessageRepository = new FileMessageRepository();
        fileUserRepository = new FileUserRepository();

        fileValidateService = new ValidateOperator(fileChannelRepository, fileUserRepository, fileMessageRepository);
        jcfValidateService = new ValidateOperator(jcfChannelRepository, jcfUserRepository, jcfMessageRepository);

        fileBasicChannelService = new BasicChannelService(fileChannelRepository,fileValidateService,fileUserRepository);
        jcfBasicChannelService = new BasicChannelService(jcfChannelRepository,jcfValidateService,jcfUserRepository);

        fileBasicUserService = new BasicUserService(fileUserRepository,fileValidateService,fileChannelRepository,fileMessageRepository);
        jcfBasicUserService = new BasicUserService(jcfUserRepository,jcfValidateService,jcfChannelRepository,jcfMessageRepository);

        fileBasicMessageService = new BasicMessageService(fileMessageRepository,fileValidateService);
        jcfBasicMessageService = new BasicMessageService(jcfMessageRepository,jcfValidateService);
    }

}
