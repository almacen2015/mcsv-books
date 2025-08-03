package backend.dtos.pageable;

public record PageableCustom(Integer page, Integer size, String orderBy) {
}
