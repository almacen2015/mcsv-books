package backend.roomservice.services;

import backend.dtos.apiresponse.ApiResponseDto;
import backend.dtos.room.request.RoomRequestDto;
import backend.dtos.room.response.RoomResponseDto;

public interface RoomService {
    ApiResponseDto<RoomResponseDto> add(RoomRequestDto dto);
}
