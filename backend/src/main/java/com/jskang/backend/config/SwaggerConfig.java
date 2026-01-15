package com.jskang.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("GPS Project API")      // 1. 문서 제목
                        .description("GPS 프로젝트 백엔드 API 명세서입니다.") // 2. 문서 설명
                        .version("1.0.0"));            // 3. 버전
    }

}
