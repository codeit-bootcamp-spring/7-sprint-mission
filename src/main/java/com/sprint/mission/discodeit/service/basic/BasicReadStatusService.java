package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatus.response.ReadStatusResponse;
import com.sprint.mission.discodeit.dto.readstatus.requset.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.requset.ReadStatusFindByUserRequest;
import com.sprint.mission.discodeit.dto.readstatus.requset.ReadStatusFindRequest;
import com.sprint.mission.discodeit.dto.readstatus.requset.ReadStatusUpdateReuqest;
import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicReadStatusService implements ReadStatusService {
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;
    private final ChannelRepository channelRepository;


    @Override
    public ReadStatus create(ReadStatusCreateRequest request) {

        //request에 받은 Channel이나 User의 uuid가 존재하냐
        boolean channelMatch = channelRepository.findAll().stream().anyMatch(channel -> channel.getId().equals(request.channelId()));
        boolean userMatch = userRepository.findAll().stream().anyMatch(user -> user.getId().equals(request.channelId()));
        //같은 Channel과 User와 관련된 객체 존재하냐
        boolean UserChannelMatch = readStatusRepository.findAll().stream()
                .anyMatch(readStatus ->
                        readStatus.getUserId().equals(request.userId()) && readStatus.getChannelId().equals(request.channelId()));
      // Channel이나 User가 존재하지 않으면 예외
      if(!(channelMatch || userMatch)){
          throw new RuntimeException("맞는uuid가 존재하지않아요\n"
                  +"채널아이디 : " + request.channelId()+"\n"
                  +"유저아이디 : " + request.userId());
      }else if(UserChannelMatch){
         throw new IllegalStateException("이미 존재하는 ReadStatus다");
      }
      ReadStatus createReadStatus = new ReadStatus(request.userId(),request.channelId(),request.lastReadAt());
        ReadStatus save = readStatusRepository.save(createReadStatus);

      return save;

    }

    @Override
    public ReadStatus find(UUID userStatusId) {
        return readStatusRepository.find(userStatusId);
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
       return readStatusRepository.findAllByUserId(userId).stream()
                .toList();
    }

    @Override
    public ReadStatus update(UUID readStatusId,ReadStatusUpdateReuqest request) {
        Instant newLastReadAt = request.newLastReadAt();
        ReadStatus readStatus = readStatusRepository.find(readStatusId);
        //업데이트자체가 시간만 있는게아닌가 시간초기화
        readStatus.update(newLastReadAt);
      return  readStatusRepository.save(readStatus);

    }

    @Override
    public void delete(UUID readStatusId) {
        readStatusRepository.deleteById(readStatusId);
    }
}
