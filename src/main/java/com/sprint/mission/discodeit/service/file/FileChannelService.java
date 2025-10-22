package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileChannelService implements ChannelService {
    private List<Channel> data;
    private static final FileChannelService singleton = new FileChannelService();
    private final String filename = "channels";

    public static FileChannelService getInstance() {
        return singleton;
    }

    private FileChannelService() {}

    //모든 채널 띄우기
    @Override
    public List<Channel> findAll() {
        try(ObjectInputStream is = FileInOutUtil.getInputStream(filename);){
            data = (ArrayList)is.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    //채널 하나 찾기
    @Override
    public Channel findById(UUID id) {
        data = findAll();
        return data.stream().filter(
                ch -> ch.getId().equals(id)).findFirst().orElseThrow(
                () -> new RuntimeException("해당 ID를 가진 Channel를 찾을 수 없습니다: " + id)
        );
    }

    //채널 이름으로 찾기
    @Override
    public Channel findByName(String name){
        data = findAll();
        return data.stream().filter(
                ch -> ch.getName().equals(name)).findFirst().orElseThrow(
                () -> new RuntimeException("해당 이름을 가진 Channel을 찾을 수 없음: "+ name)
        );
    }

    //방 생성
    @Override
    public Channel insert(Channel channel) {
        try {
            FileOutputStream fos = new FileOutputStream("channel.txt");
            FileInputStream fis = new FileInputStream("channel.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            BufferedInputStream bis = new BufferedInputStream(ois);
            BufferedOutputStream bos = new BufferedOutputStream(oos);
        }catch (Exception e){
            e.printStackTrace();
        }
        //로그인을 구현하지 않았으므로 임의로 나의 이메일을..
        Channel newChannel = new Channel("방1", FileUserService.loginUser);
        data.add(newChannel);
        try(ObjectOutputStream os = FileInOutUtil.getOutputStream(filename);){
            data = findAll();
            data.add(newChannel);
            os.writeObject(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newChannel;
    }

    //방 이름 변경
    @Override
    public Channel update(UUID id, String name) {
        Channel channel = findById(id);
        channel.setName(name);
        channel.setUpdatedAt(System.currentTimeMillis());

        try(ObjectOutputStream os = FileInOutUtil.getOutputStream(filename);){
            data = findAll();
            os.writeObject(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return channel;
    }

    //방 삭제
    @Override
    public Channel delete(UUID id) {
        Channel channel = findById(id);
        data.remove(channel);

        try(ObjectOutputStream os = FileInOutUtil.getOutputStream(filename);){
            data = findAll();
            os.writeObject(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return channel;
    }
}
