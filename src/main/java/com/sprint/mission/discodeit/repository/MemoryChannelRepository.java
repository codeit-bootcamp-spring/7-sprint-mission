package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.UpdatedChannelDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

public class MemoryChannelRepository {
    private final Map<UUID, Channel> store = new HashMap<>();

    public void save(Channel channel){
        if (channel.getServerName()==null){
            System.out.println("채널 생성 실패");

        } else {
            UUID key = channel.getId();
            store.put(key, channel);
            System.out.println("채널 생성 성공");
        }
    }
    public void remove(Channel channel){
        UUID findChannelId = channel.getId();
        store.remove(findChannelId);
        System.out.println("채널 삭제 성공");
    }

    public Channel findById(UUID id){
        if(!store.containsKey(id)){
            throw new NoSuchElementException("채널을 찾을 수 없습니다");
        }
        Channel findChannel = store.get(id);
        System.out.println("채널 찾기 성공");
        return findChannel;
    }

    public void updateChannel(UUID id, UpdatedChannelDTO updatedChannelDTO){
        if(!store.containsKey(id)){
            throw new NoSuchElementException("채널을 찾을 수 없습니다.");
        }
        Channel findChannel = store.get(id);
        if (findChannel.isPrivate()!=updatedChannelDTO.isPrivate()){
            System.out.println("[채널 공개 설정이 "+findChannel.isPrivate() +"에서 "+updatedChannelDTO.isPrivate()+"로 변경되었습니다]");
            findChannel.setPrivate(updatedChannelDTO.isPrivate());
        }
        if (updatedChannelDTO.getServerLevel()!=null){
            System.out.printf("[채널 레벨이 %s에서 %s로 변경되었습니다]\n", findChannel.getServerLevel(), updatedChannelDTO.getServerLevel());
            findChannel.setServerLevel(updatedChannelDTO.getServerLevel());
        }
        if(updatedChannelDTO.getManager()!=null){
            System.out.printf("[채널 매니저가 %s에서 %s로 변경되었습니다]\n",
                    findChannel.getManager().getUsername(), updatedChannelDTO.getManager().getUsername());
            findChannel.setManager(updatedChannelDTO.getManager());
        }
        if(updatedChannelDTO.getServerName()!=null){
            System.out.printf("[채널 이름이 %s에서 %s로 변경되었습니다]\n",
                    findChannel.getServerName(), updatedChannelDTO.getServerName());
            findChannel.setServerName(updatedChannelDTO.getServerName());
        }
        store.put(id, findChannel);
        System.out.println("채널 정보 변경 성공");
    }

}
