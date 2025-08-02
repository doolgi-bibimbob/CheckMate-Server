package com.seonlim.mathreview.dto;

import com.seonlim.mathreview.entity.Position;
import com.seonlim.mathreview.entity.ReviewAnnotation;

public record ReviewAnnotationDto(
        String imageUrl,
        String content,
        Position position,
        int width,
        int height,
        long index
) {
    public static ReviewAnnotationDto from(ReviewAnnotation a) {
        return new ReviewAnnotationDto(
                a.getImageUrl(),
                a.getContent(),
                a.getPosition(),
                a.getWidth(),
                a.getHeight(),
                a.getIndex()
        );
    }

}
