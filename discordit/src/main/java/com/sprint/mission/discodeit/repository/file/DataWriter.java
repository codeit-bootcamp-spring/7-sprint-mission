package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.base.Channel;
import com.sprint.mission.discodeit.entity.base.Message;
import com.sprint.mission.discodeit.entity.base.Receivable;
import com.sprint.mission.discodeit.entity.base.User;
import com.sprint.mission.discodeit.entity.dto.fileIo.mapper.Mapper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.sprint.mission.discodeit.config.DataPath.*;

class DataWriter {
    public static void writeChannel(Map<UUID, Channel> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CHANNEL_FILE_PATH))) {
            oos.writeObject(data.values().stream()
                    .map(Mapper::toChannelDTO)
                    .toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeUser(Map<String, User> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_FILE_PATH))) {
            oos.writeObject(data.values().stream()
                    .map(Mapper::toUserDto)
                    .toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void writeMessage(List<Message<Receivable>> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(MESSAGE_FILE_PATH))) {
            oos.writeObject(data.stream()
                    .map(Mapper::toMessageDto)
                    .toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
