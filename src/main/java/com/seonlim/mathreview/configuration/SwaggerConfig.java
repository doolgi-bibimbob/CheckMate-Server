package com.seonlim.mathreview.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Seonlim MathReview API",
                description = "수학 리뷰 API 문서입니다.",
                version = "v1.0"
        )
)
class SwaggerConfig {
}
