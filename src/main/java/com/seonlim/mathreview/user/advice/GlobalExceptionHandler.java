package com.seonlim.mathreview.user.advice;

import com.seonlim.mathreview.user.exception.DuplicateEmailException;
import com.seonlim.mathreview.user.exception.VerificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(VerificationException.class)
    public ResponseEntity<String> handleVerificationError(VerificationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ " + ex.getMessage());
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<String> handleDuplicateEmail(DuplicateEmailException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("❗ " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleOthers(Exception ex) {
        log.error("⚠️ 처리되지 않은 예외 발생", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("⚠️ 예기치 못한 오류가 발생했습니다.");
    }
}
