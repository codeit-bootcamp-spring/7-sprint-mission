package com.sprint.mission.discodeit.entity.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.service.ChannelService;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class JCFChannelService implements ChannelService {

    private  final  List<Channel>  channels;

    private static final JCFChannelService INSTANCE = new JCFChannelService();

    private  JCFChannelService(){
        channels = new LinkedList<>();
    }

    //이것도 다 똑같다
    public static JCFChannelService getInstance(){
        return INSTANCE;
    }





    @Override
    public  void create(User user,String channelName) {
        Channel ch = new Channel(user,channelName);
        channels.add(ch);
        ch.setChannelName(channelName);
        System.out.printf("%s가 채널을 만들엇습니다\n",user.getUserName());

    }

    @Override
    public  void read(UUID channelId) {
           channels.stream()
                   .filter(ch -> ch.getId().equals(channelId))
                   .forEach(d ->{
                       System.out.printf("채널 ID: %s\n", d.getId().toString().replace("-", ""));
                       System.out.printf("생성 시각: %s\n", d.getCreatedAt());
                       System.out.printf("채널 이름: %s\n", d.getChannelName());
                       System.out.printf("채널장(boss): %s\n", d.getBose());
                    });




    }

    //파라미터 어떻게 해야할것같다
    @Override
    public void update(UUID channelId, String channelName) {
          return;
    }


    @Override
    public void readAll() {
        System.out.printf("%d개의채널 ", channels.size());
        channels.stream()
                .forEach(d ->{
                    System.out.printf("채널 ID: %s\n", d.getId().toString().replace("-", ""));
                    System.out.printf("생성 시각: %s\n", d.getCreatedAt());
                    System.out.printf("채널 이름: %s\n", d.getChannelName());
                    System.out.printf("채널장(boss): %s\n", d.getBose());
                });
    }

    @Override
    public void delete(UUID channelId) {
        channels.stream() .filter(u -> u.equals(channelId))
                          .toList()
                          .forEach(u-> {
                              channels.remove(u);
                             System.out.printf("%s채널이 삭제되었습니다\n",u.getChannelName());
                });

    }
}
