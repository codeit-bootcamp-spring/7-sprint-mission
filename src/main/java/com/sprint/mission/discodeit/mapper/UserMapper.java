package com.sprint.mission.discodeit.mapper;


import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/***
 * 컴파일 시점에 코드를 생성하여 런타임에서 안정성을 보장합니다.
 다른 매핑 라이브러리보다 속도가 빠릅니다.
 반복되는 객체 매핑에서 발생할 수 있는 오류를 줄일 수 있으며, 구현 코드를 자동으로 만들어주기 때문에 사용이 쉽습니다.
 Annotation processor를 이용하여 객체 간 매핑을 자동으로 제공합니다.
 */
@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class, UserStatusMapper.class})
// componetModel을 spring으로 지정해줘야 bean으로 등록할 수 있음.
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  //  source = from (입력)
  //  target = to (출력)
  @Mapping(target = "online", source = "user")
  UserResponseDto toResponseDto(User user);

}
