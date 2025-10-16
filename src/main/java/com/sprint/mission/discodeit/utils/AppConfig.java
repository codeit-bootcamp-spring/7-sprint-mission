package com.sprint.mission.discodeit.utils;

import com.google.gson.reflect.TypeToken;
import com.sprint.mission.discodeit.data.DataPersistenceManager;
import com.sprint.mission.discodeit.data.JsonPersistenceManager;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.jcf.*;
import com.sprint.mission.discodeit.service.jcf.*;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AppConfig {

    // 모든 설정과 컴포넌트를 소유하고 관리합니다.
//    private final ConfigurationLoader configLoader;
//    private final JsonPersistenceManager persistenceManager;
    private final DataPersistenceManager dataPersistenceManager;

    private final JCFUserRepository userRepository;
    private final JCFChannelRepository channelRepository;
    private final JCFParticipationRepository participationRepository;
    private final JCFChannelMessageRepository channelMessageRepository;
    private final JCFDirectMessageRepository directMessageRepository;
    private final JCFUserService userService;
    private final JCFChannelService channelService;
    private final JCFParticipationService participationService;
    private final JCFChannelMessageService channelMessageService;
    private final JCFDirectMessageService directMessageService;


    public AppConfig() {
        // 설정 로더를 가장 먼저 생성합니다.
//        this.configLoader = new ConfigurationLoader("config.properties");

        // 1. 데이터 영속성 관리자 생성
//        this.persistenceManager = new JsonPersistenceManager(configLoader);
        this.dataPersistenceManager = DataPersistenceManager.getInstance();


        // 2. Repository 생성
        this.userRepository = new JCFUserRepository();
        this.channelRepository = new JCFChannelRepository();
        this.participationRepository = new JCFParticipationRepository();
        this.channelMessageRepository = new JCFChannelMessageRepository();
        this.directMessageRepository = new JCFDirectMessageRepository();

        // 3. 데이터 로드
        loadAllData();

        // 4. Service 생성 및 의존성 주입
        this.userService = new JCFUserService(userRepository, participationRepository, channelMessageRepository, directMessageRepository);
        this.channelService = new JCFChannelService(channelRepository);
        this.participationService = new JCFParticipationService(participationRepository, userRepository, channelRepository);
        this.channelMessageService = new JCFChannelMessageService(channelMessageRepository, participationRepository);
        this.directMessageService = new JCFDirectMessageService(directMessageRepository, userRepository);


    }

    private void loadAllData() {
        userRepository.loadDataMap(dataPersistenceManager.loadData(DataKey.USER));
        channelRepository.loadDataMap(dataPersistenceManager.loadData(DataKey.CHANNEL));
        participationRepository.loadDataMap(dataPersistenceManager.loadData(DataKey.PARTICIPATION));
        channelMessageRepository.loadDataMap(dataPersistenceManager.loadData(DataKey.CHANNEL_MESSAGE));
        directMessageRepository.loadDataMap(dataPersistenceManager.loadData(DataKey.DIRECT_MESSAGE));
        System.out.println("모든 데이터를 파일로부터 로드했습니다.");
        System.out.println("유저 수 : " + userRepository.getDataMap().size());
    }

    public void saveAllData() {
        dataPersistenceManager.saveData(DataKey.USER, userRepository.getDataMap());
        dataPersistenceManager.saveData(DataKey.CHANNEL, channelRepository.getDataMap());
        dataPersistenceManager.saveData(DataKey.PARTICIPATION, participationRepository.getDataMap());
        dataPersistenceManager.saveData(DataKey.CHANNEL_MESSAGE, channelMessageRepository.getDataMap());
        dataPersistenceManager.saveData(DataKey.DIRECT_MESSAGE, directMessageRepository.getDataMap());
        System.out.println("모든 데이터를 파일에 저장했습니다.");
    }

    // --- Service Getters ---
    public JCFUserService getUserService() { return userService; }
    public JCFChannelService getChannelService() { return channelService; }
    public JCFParticipationService getParticipationService() { return participationService; }
    public JCFChannelMessageService getChannelMessageService() { return channelMessageService; }
    public JCFDirectMessageService getDirectMessageService() { return directMessageService; }

    /**
     * UI 계층에서 설정 파일에 접근할 수 있도록 ConfigurationLoader를 제공합니다.
//     */
//    public ConfigurationLoader getConfigLoader() {
//        return configLoader;
//    }
}