package com.seonlim.mathreview.dto;

import com.seonlim.mathreview.entity.ReviewLayer;

public record ReviewLayerDto(
        String imgUrl,
        Long pageNumber,
        String backgroundImgUrl
) {
    public static ReviewLayerDto from(ReviewLayer layer) {
        return new ReviewLayerDto(
                layer.getImgUrl(),
                layer.getPageNumber(),
                layer.getBackgroundImgUrl()
        );
    }
}
