package backend.roomservice.services.impl;

import backend.dtos.apiresponse.ApiResponseDto;
import backend.dtos.room.request.RoomRequestDto;
import backend.dtos.room.response.RoomResponseDto;
import backend.roomservice.repositories.RoomRepository;
import backend.roomservice.services.RoomService;
import org.springframework.stereotype.Service;

@Service
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;

    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public ApiResponseDto<RoomResponseDto> add(RoomRequestDto dto) {
        return null;
    }
}
