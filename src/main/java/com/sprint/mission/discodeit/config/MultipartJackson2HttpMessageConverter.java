package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
//임시방편이다 스웨거가 멀티파트로 요청을하면
//컨트롤러에 보낼때 스웨거는
//joson으로 보낸다는 것을 안해준다
// 대충봤지만 들어갈 여러가지 타입중에
//스웨거가 널값이 있으면 거기에다 정보를 넣어줘서
//'application/octet-stream이거로 인식을 한다고 한다
//그래서 강제로 받는 정보는 json이라고 지명했다
//
/*
@Component // 스프링 빈으로 자동 등록됨
public class MultipartJackson2HttpMessageConverter extends AbstractJackson2HttpMessageConverter {

    public MultipartJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        // 지원 미디어타입을 강제로 application/octet-stream으로 등록
        super(objectMapper, MediaType.APPLICATION_OCTET_STREAM);
    }

    // write는 사용하지 않겠다는 의미 (읽기 전용)
    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) { return false; }

    @Override
    public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) { return false; }

    @Override
    protected boolean canWrite(MediaType mediaType) { return false; }
}
*/
