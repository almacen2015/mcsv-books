package backend.dtos.room.request;

public record RoomRequestDto(String number,
                             String description,
                             Double price,
                             Integer capacity,
                             String roomType,
                             String roomStatus) {
}
