package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.dto.Dto_BinaryContent;
import lombok.Getter;

@Getter
public class BinaryContent extends BaseModel {
//    이미지, 파일 등 바이너리 데이터를 표현하는 도메인 모델입니다. 사용자의 프로필 이미지, 메시지에 첨부된 파일을 저장하기 위해 활용합니다.
//[ ] User, Message 도메인 모델과의 의존 관계 방향성을 잘 고려하여 id 참조 필드를 추가하세요.

    //!! BufferedImage는 직렬화할 수 없으므로 ✅ transient(직렬화 대상에서 제외) 로 선언
//    private transient BufferedImage profileImage;
//    private transient File contentFile;

    // 직렬화를 위한 바이트 배열 필드
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


    // 커스텀 직렬화 메서드
//    private void writeObject(ObjectOutputStream out) throws IOException {
//        // 기본 필드 직렬화
//        out.defaultWriteObject();
//
//        // BufferedImage를 바이트 배열로 변환하여 저장
//        if (profileImageBytes != null) {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            ImageIO.write(profileImageBytes, "png", baos);
//            profileImageBytes = baos.toByteArray();
//            out.writeObject(profileImageBytes);
//            out.writeBoolean(true);
//        } else {
//            out.writeBoolean(false);
//        }
//
//        // File을 바이트 배열로 변환하여 저장
//        if (listContentFile != null && listContentFile.exists()) {
//            try (FileInputStream fis = new FileInputStream(listContentFile)) {
//                contentFileBytes = fis.readAllBytes();
//                out.writeObject(contentFileBytes);
//                out.writeUTF(listContentFile.getName());
//                out.writeBoolean(true);
//            }
//        } else {
//            out.writeBoolean(false);
//        }
//    }
//
//    // 커스텀 역직렬화 메서드
//    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
//        // 기본 필드 역직렬화
//        in.defaultReadObject();
//
//        // BufferedImage 복원
//        boolean hasProfileImage = in.readBoolean();
//        if (hasProfileImage) {
//            profileImageBytes = (byte[]) in.readObject();
//            ByteArrayInputStream bais = new ByteArrayInputStream(profileImageBytes);
//            profileImageBytes = ImageIO.read(bais);
//        }
//
//        // File 복원
//        boolean hasContentFile = in.readBoolean();
//        if (hasContentFile) {
//            contentFileBytes = (byte[]) in.readObject();
//            String fileName = in.readUTF();
//            // 임시 파일로 복원 (필요에 따라 경로 수정)
//            listContentFile = File.createTempFile("content_", "_" + fileName);
//            try (FileOutputStream fos = new FileOutputStream(listContentFile)) {
//                fos.write(contentFileBytes);
//            }
//        }
//    }
}
