package backend.roomservice.service.impl;

import backend.dtos.room.request.RoomRequestDto;
import backend.dtos.room.response.RoomResponseDto;
import backend.exceptions.room.RoomException;
import backend.roomservice.model.entity.Room;
import backend.roomservice.model.mapper.RoomMapper;
import backend.roomservice.repository.RoomRepository;
import backend.roomservice.service.RoomService;
import backend.utils.Utils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper mapper = RoomMapper.INSTANCE;

    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    @Transactional(rollbackFor = RoomException.class)
    public RoomResponseDto add(RoomRequestDto dto) {
        Utils.validateRoomDto(dto);
        Room roomEntity = mapper.toEntity(dto);
        return mapper.toDto(roomRepository.save(roomEntity));
    }
}
