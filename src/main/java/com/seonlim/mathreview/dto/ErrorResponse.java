package com.seonlim.mathreview.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Schema(description = "에러 응답 포맷")
public record ErrorResponse(@Schema(description = "HTTP 상태 코드", example = "400") int status,
                            @Schema(description = "에러 메시지", example = "잘못된 요청입니다.") String message,
                            @Schema(description = "에러 발생 시각", example = "2025-07-31T20:12:00") LocalDateTime timestamp) {
}
