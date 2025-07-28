package backend.dtos.apiresponse;

public record ApiResponseDto<T>(int code, String message, T data, String traceId) {
}
