package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.common.constants.DataPath;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.dto.fileIo.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
class DataWriter {
    private final DataPath dataPath;

    public void writeChannel(Map<UUID, Channel> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataPath.CHANNEL_FILE_PATH()))) {
            oos.writeObject(data.values().stream()
                    .map(Mapper::toChannelDTO)
                    .toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeUser(Map<UUID, User> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataPath.USER_FILE_PATH()))) {
            oos.writeObject(data.values().stream()
                    .map(Mapper::toUserDto)
                    .toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeMessage(List<Message> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataPath.MESSAGE_FILE_PATH()))) {
            oos.writeObject(data.stream()
                    .map(Mapper::toMessageDto)
                    .toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeBinaryContent(List<BinaryContent> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataPath.BINARY_CONTENT_FILE_PATH()))) {
            oos.writeObject(data.stream()
                    .map(Mapper::toDto)
                    .toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeReadStatus(List<ReadStatus> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataPath.READ_STATUS_FILE_PATH()))) {
            oos.writeObject(data.stream()
                    .map(Mapper::toDto)
                    .toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeUserStatus(Map<UUID, UserStatus> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataPath.USER_STATUS_FILE_PATH()))) {
            oos.writeObject(data.values().stream()
                    .map(Mapper::toDto)
                    .toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
