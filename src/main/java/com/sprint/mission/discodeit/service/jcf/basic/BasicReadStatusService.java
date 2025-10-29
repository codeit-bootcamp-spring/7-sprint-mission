package com.sprint.mission.discodeit.service.jcf.basic;

import com.sprint.mission.discodeit.dto.readstatus.requset.ReadStatusCreateReuqest;
import com.sprint.mission.discodeit.dto.readstatus.requset.ReadStatusUpdateReuqest;
import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicReadStatusService implements ReadStatusService {
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;
    private final ChannelRepository channelRepository;


    @Override
    public ReadStatus create(ReadStatusCreateReuqest reuqest) {

        //request에 받은 Channel이나 User의 uuid가 존재하냐
        boolean channelMatch = channelRepository.findAll().stream().anyMatch(channel -> channel.getId() == reuqest.channelId());
        boolean userMatch = userRepository.findAll().stream().anyMatch(user -> user.getId() == reuqest.userId());
        //같은 Channel과 User와 관련된 객체 존재하냐
        boolean UserChannelMatch = readStatusRepository.findAll().stream()
                .anyMatch(readStatus ->
                        readStatus.getUserId().equals(reuqest.userId()) && readStatus.getChannelId().equals(reuqest.channelId()));
      // Channel이나 User가 존재하지 않으면 예외
      if(!(channelMatch && userMatch)){
          throw new RuntimeException("맞는uuid가 존재하지않아요\n"
                  +"채널아이디 : " + reuqest.channelId()+"\n"
                  +"유저아이디 : " + reuqest.userId());
      }else if(!UserChannelMatch){
         throw new IllegalStateException("이미 존재하는 ReadStatus다");
      }

        return readStatusRepository.save(reuqest.channelId(), reuqest.userId());
    }

    @Override
    public ReadStatus find(UUID readStatusId) {
        return   readStatusRepository.find(readStatusId);
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
       return readStatusRepository.findAllByUserId(userId);
    }

    @Override
    public ReadStatus update(ReadStatusUpdateReuqest request) {
        ReadStatus readStatus = readStatusRepository.find(request.readStatusId());
        //업데이트자체가 시간만 있는게아닌가 시간초기화
        readStatus.update();
        return readStatusRepository.save(readStatus.getUserId(), readStatus.getChannelId());
    }

    @Override
    public void delete(UUID readStatusId) {
        readStatusRepository.deleteById(readStatusId);
    }
}
