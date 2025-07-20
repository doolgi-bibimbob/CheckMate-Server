package com.seonlim.mathreview.dto;

public record AnnotationDto(
        String imageUrl,
        String content,
        Position position,
        int width,
        int height,
        long index
) { }
