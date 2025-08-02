package com.seonlim.mathreview.controller;

import com.seonlim.mathreview.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.time.Duration;

@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
public class S3Controller {
    private final S3Service s3Service;

    @GetMapping("/upload-url")
    public ResponseEntity<String> getPresignedUrl(@RequestParam String filename) {
        URL url = s3Service.generatePresignedUrl(filename, Duration.ofMinutes(5));
        return ResponseEntity.ok(url.toString());
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam MultipartFile file) {
        String key = s3Service.uploadFile(file, "test");
        return ResponseEntity.ok("업로드 성공: " + key);
    }
}
