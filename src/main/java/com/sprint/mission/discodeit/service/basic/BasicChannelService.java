package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    //싱글톤 구현
    private final static BasicChannelService basicChannelService = new BasicChannelService();
    private BasicChannelService(){}
    public static BasicChannelService getInstance(){
        return basicChannelService;
    }

    //레포지토리
    private JCFChannelRepository jcfChannelRepository = JCFChannelRepository.getInstance();

    //채널 생성
    @Override
    public Channel create(UUID managerId, String name) {
        return jcfChannelRepository.save(new  Channel(managerId, name));
    }
    
    //채널 수정
    @Override
    public Channel update(UUID id, String name) {
        return jcfChannelRepository.update(id, name);
    }

    //채널 삭제
    @Override
    public Channel delete(UUID id) {
        return jcfChannelRepository.delete(id);
    }

    //채널 목록
    @Override
    public List<Channel> findAll() {
        return jcfChannelRepository.findAll();
    }

    //채널명으로 찾기
    @Override
    public Channel findByName(String name) {
        return jcfChannelRepository.findByName(name);
    }
}
