package backend.dtos.room.response;

public record RoomResponseDto(Long id,
                              String number,
                              String description,
                              Double price,
                              Integer capacity,
                              String roomType,
                              String roomStatus) {
}
