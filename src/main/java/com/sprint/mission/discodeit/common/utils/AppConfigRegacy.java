package com.sprint.mission.discodeit.common.utils;

import com.sprint.mission.discodeit.channel.ChannelRepository;
import com.sprint.mission.discodeit.channel.ChannelRepositoryImpl;
import com.sprint.mission.discodeit.channel.ChannelService;
import com.sprint.mission.discodeit.channel.ChannelServiceImpl;
import com.sprint.mission.discodeit.message.channel.ChannelMessageRepository;
import com.sprint.mission.discodeit.message.channel.ChannelMessageRepositoryImpl;
import com.sprint.mission.discodeit.message.channel.ChannelMessageService;
import com.sprint.mission.discodeit.message.channel.ChannelMessageServiceImpl;
import com.sprint.mission.discodeit.message.direct.DirectMessageRepository;
import com.sprint.mission.discodeit.message.direct.DirectMessageRepositoryImpl;
import com.sprint.mission.discodeit.message.direct.DirectMessageService;
import com.sprint.mission.discodeit.message.direct.DirectMessageServiceImpl;
import com.sprint.mission.discodeit.participation.ParticipationRepository;
import com.sprint.mission.discodeit.participation.ParticipationRepositoryImpl;
import com.sprint.mission.discodeit.participation.ParticipationService;
import com.sprint.mission.discodeit.participation.ParticipationServiceImpl;
import com.sprint.mission.discodeit.user.*;
import lombok.Getter;

@Getter
public class AppConfigRegacy {

    // 모든 설정과 컴포넌트를 소유하고 관리합니다.
//    private final ConfigurationLoader configLoader;
//    private final JsonPersistenceManager persistenceManager;
//    private final DataPersistenceManager dataPersistenceManager;

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ParticipationRepository participationRepository;
    private final ChannelMessageRepository channelMessageRepository;
    private final DirectMessageRepository directMessageRepository;
    // --- Service Getters ---

    private final UserService userService;
    private final ChannelService channelService;
    private final ParticipationService participationService;
    private final ChannelMessageService channelMessageService;
    private final DirectMessageService directMessageService;

    private final PasswordEncoder passwordEncoder;


    public AppConfigRegacy() {
//        this.dataPersistenceManager = DataPersistenceManager

        this.passwordEncoder = new PasswordEncoder();
        // 2. Repository 생성
        this.userRepository = new UserRepositoryImpl();
        this.channelRepository = new ChannelRepositoryImpl();
        this.participationRepository = new ParticipationRepositoryImpl();
        this.channelMessageRepository = new ChannelMessageRepositoryImpl();
        this.directMessageRepository = new DirectMessageRepositoryImpl();

        // 3. 데이터 로드
//        loadAllData();

        // 4. Service 생성 및 의존성 주입
        this.userService = new UserServiceImpl(userRepository, passwordEncoder);
        this.channelService = new ChannelServiceImpl(channelRepository);
        this.participationService = new ParticipationServiceImpl(participationRepository, userService, channelService);
        this.channelMessageService = new ChannelMessageServiceImpl(channelMessageRepository, participationService);
        this.directMessageService = new DirectMessageServiceImpl(directMessageRepository, userService);


    }

//    private void loadAllData() {
//        userRepository.loadDataMap(dataPersistenceManager.loadData(DataKey.USER));
//        channelRepository.loadDataMap(dataPersistenceManager.loadData(DataKey.CHANNEL));
//        participationRepository.loadDataMap(dataPersistenceManager.loadData(DataKey.PARTICIPATION));
//        channelMessageRepository.loadDataMap(dataPersistenceManager.loadData(DataKey.CHANNEL_MESSAGE));
//        directMessageRepository.loadDataMap(dataPersistenceManager.loadData(DataKey.DIRECT_MESSAGE));
//        System.out.println("모든 데이터를 파일로부터 로드했습니다.");
//        System.out.println("유저 수 : " + userRepository.getDataMap().size());
//    }
//
//    public void saveAllData() {
//        dataPersistenceManager.saveData(DataKey.USER, userRepository.getDataMap());
//        dataPersistenceManager.saveData(DataKey.CHANNEL, channelRepository.getDataMap());
//        dataPersistenceManager.saveData(DataKey.PARTICIPATION, participationRepository.getDataMap());
//        dataPersistenceManager.saveData(DataKey.CHANNEL_MESSAGE, channelMessageRepository.getDataMap());
//        dataPersistenceManager.saveData(DataKey.DIRECT_MESSAGE, directMessageRepository.getDataMap());
//        System.out.println("모든 데이터를 파일에 저장했습니다.");
//    }

    /**
     * UI 계층에서 설정 파일에 접근할 수 있도록 ConfigurationLoader를 제공합니다.
//     */
//    public ConfigurationLoader getConfigLoader() {
//        return configLoader;
//    }
}