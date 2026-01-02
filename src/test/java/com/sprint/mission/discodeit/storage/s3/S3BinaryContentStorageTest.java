package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentResponseDto;
import io.findify.s3mock.S3Mock;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.io.InputStream;
import java.net.URI;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

// 일반적으로 메서드마다 클래스를 새로 만드는데, 클래스 하나로 모든 테스트를 공유
// S3Mock 같은 무거운 자원을 딱 한번만 띄우고 돌려서 쓰기 위해
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class S3BinaryContentStorageTest {

    private S3Mock s3Mock;
    private S3Client s3Client;
    private S3Presigner s3Presigner;
    private S3BinaryContentStorage binaryContentStorage;

    private static final int PORT = 8001;
    private static final String BUCKET = "test-bucket";

    @BeforeAll
    void setUpAll() {
        // S3Mock 시작
        s3Mock = new S3Mock.Builder()
                .withPort(PORT)
                .withInMemoryBackend()
                .build();
        s3Mock.start();

        // S3 클라이언트 생성 (S3 서버 주소는 AWS가 아니라 내 컴퓨터(localhost:8081)이다!
        s3Client = S3Client.builder()
                .endpointOverride(URI.create("http://localhost:" + PORT))
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create("test", "test")))
                .forcePathStyle(true)  // S3Mock에서 필수
                .build();

        // S3Presigner 생성
        s3Presigner = S3Presigner.builder()
                .endpointOverride(URI.create("http://localhost:" + PORT))
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create("test", "test")))
                .build();

        // Storage 인스턴스 생성
        binaryContentStorage = new S3BinaryContentStorage(s3Client, s3Presigner);

        // private 필드 설정
        ReflectionTestUtils.setField(binaryContentStorage, "bucket", BUCKET);
        ReflectionTestUtils.setField(binaryContentStorage, "presignedUrlExpiration", 600L);

        // 버킷 생성
        s3Client.createBucket(CreateBucketRequest.builder()
                .bucket(BUCKET)
                .build());
    }

    @AfterAll
    void tearDownAll() {
        if (s3Mock != null) {
            s3Mock.shutdown();
        }
    }


    @Test
    @DisplayName("파일을 S3에 업로드하고 UUID를 반환")
    void put_should_upload_file_and_return_uuid() {
        // given
        UUID binaryContentId = UUID.randomUUID();
        byte[] content = "Test".getBytes();

        // when
        UUID result = binaryContentStorage.put(binaryContentId, content);

        // then
        assertThat(result).isEqualTo(binaryContentId);

        // S3에 실제로 저장되었는지 확인
        // HeadObjectResponse는 S3파일의 메타 데이터만 가져오는 응답 객체
        /**
         * // 주요 메서드들:
         * headObject.contentLength()      // 파일 크기 (바이트)
         * headObject.contentType()        // 파일 타입 (image/png 등)
         * headObject.lastModified()       // 마지막 수정 시간
         * headObject.eTag()               // 파일 해시값 (변경 감지용)
         * headObject.metadata()           // 사용자 정의 메타데이터
         * headObject.storageClass()       // 스토리지 클래스
         * ```
         */
        HeadObjectResponse headObject = s3Client.headObject(HeadObjectRequest.builder()
                .bucket(BUCKET)
                .key(binaryContentId.toString())
                .build());

        assertThat(headObject.contentLength()).isEqualTo(content.length);
    }

    @Test
    @DisplayName("S3에서 파일을 다운로드 한다.")
    void get_should_download_file_from_s3() throws Exception {
        UUID binaryContentId = UUID.randomUUID();
        byte[] originContent = "Test".getBytes();
        binaryContentStorage.put(binaryContentId, originContent);
        InputStream inputStream = binaryContentStorage.get(binaryContentId);

        byte[] result = inputStream.readAllBytes();

        assertThat(result).isEqualTo(originContent);
    }

    @Test
    @DisplayName("Presigned URL을 생성하고 302 리다이렉트를 반환")
    void download_should_return_redirect_with_presigned_url() {
        UUID binaryContentId = UUID.randomUUID();
        byte[] content = "Image test".getBytes();
        binaryContentStorage.put(binaryContentId, content);

        Instant createdAt = Instant.parse("2026-01-01T10:00:00Z");
        BinaryContentResponseDto binaryContentResponseDto = new BinaryContentResponseDto(
                binaryContentId,
                createdAt,
                "test.img",
                (long) content.length,
                "image/png");

        ResponseEntity<Void> result = binaryContentStorage.download(binaryContentResponseDto);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(result.getHeaders().getLocation()).isNotNull();

        String presignedURL = Objects.requireNonNull(result.getHeaders()
                .getLocation()).toString();

        assertThat(presignedURL).contains("test-bucket").contains(binaryContentId.toString());
    }

    @Test
    @DisplayName("존재하지 않는 파일 조회 시 NoSuchKeyException 발생")
    void get_should_throw_nosuchkey_exception_when_file_not_exists() {
        // given
        UUID nonExistentId = UUID.randomUUID();

        // when & then
        NoSuchKeyException exception = Assertions.assertThrows(
                NoSuchKeyException.class,
                () -> binaryContentStorage.get(nonExistentId)
        );

        // 예외 메시지도 확인 가능
        assertThat(exception.getMessage()).contains("does not exist");
    }

}