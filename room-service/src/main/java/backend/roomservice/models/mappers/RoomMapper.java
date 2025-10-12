package backend.roomservice.models.mappers;

import backend.dtos.room.request.RoomRequestDto;
import backend.dtos.room.response.RoomResponseDto;
import backend.roomservice.models.entities.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoomMapper {
    RoomMapper MAPPER = Mappers.getMapper(RoomMapper.class);

    @Mapping(target = "id", ignore = true)
    Room toEntity(RoomRequestDto dto);

    RoomResponseDto toDto(Room room);

}
