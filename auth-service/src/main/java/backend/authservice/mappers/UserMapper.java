package backend.authservice.mappers;

import backend.authservice.models.entities.UserCustom;
import backend.dtos.user.responses.UserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserResponseDto toDto(UserCustom entity);
}
