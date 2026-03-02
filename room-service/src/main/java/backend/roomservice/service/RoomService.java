package backend.roomservice.service;

import backend.dtos.room.request.RoomRequestDto;
import backend.dtos.room.response.RoomResponseDto;

public interface RoomService {
    RoomResponseDto add(RoomRequestDto dto);
}
