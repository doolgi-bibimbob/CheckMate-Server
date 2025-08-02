package com.seonlim.mathreview.dto;

import java.util.List;

public record CreateReviewRequest(List<ReviewAnnotationDto> annotations, List<ReviewLayerDto> layers) { }

