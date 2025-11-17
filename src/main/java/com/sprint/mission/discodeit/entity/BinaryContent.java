package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.dto.Dto_BinaryContent;
import lombok.Getter;

@Getter
public class BinaryContent extends BaseModel {
//    이미지, 파일 등 바이너리 데이터를 표현하는 도메인 모델입니다. 사용자의 프로필 이미지, 메시지에 첨부된 파일을 저장하기 위해 활용합니다.
//[ ] User, Message 도메인 모델과의 의존 관계 방향성을 잘 고려하여 readStatusID 참조 필드를 추가하세요.

    private String fileName;
    private String contentType;
    private byte[] data;
    private Long fileSize;

    public BinaryContent(String fileName, String contentType, byte[] data, Long fileSize) {
        super();
        this.fileName = fileName;
        this.contentType = contentType;
        this.data = data;
        this.fileSize = fileSize;
    }

    public BinaryContent(Dto_BinaryContent dtoBinaryContent) {
        super();
        this.fileName = dtoBinaryContent.fileName();
        this.contentType = dtoBinaryContent.contentType();
        this.data = dtoBinaryContent.bytes();
        this.fileSize = dtoBinaryContent.fileSize();
    }

    private void setUpdateAt() {
        ; // [ ] 수정 불가능한 도메인 모델로 간주합니다. 따라서 updatedAt 필드는 정의하지 않습니다. 🚨
        // UserService 고도화> update> 선택적으로 프로필 이미지를 대체할 수 있습니다 == 요건 뭔 말?? 🚨
    }
}
