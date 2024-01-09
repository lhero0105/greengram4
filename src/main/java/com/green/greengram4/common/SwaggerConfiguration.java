package com.green.greengram4.common;

/*import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;*/ // 임포트 에러 주의
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration // 설정과 관련된 친구란 것을 알려줍니다.
@OpenAPIDefinition(
        info = @Info(title = "Greengram Ver.4"
                    , description = "인스타그램 클론 코딩 v4"
                    , version = "4.0.0"
        ),
        security = @SecurityRequirement(name = "authorization")
)
@SecurityScheme(
        type = SecuritySchemeType.HTTP




        , name = "autorization"
        , in = SecuritySchemeIn.HEADER
        , bearerFormat = "JWT"
        , scheme = "BEARER"
)
public class SwaggerConfiguration {
/*    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Greengram Ver.3")
                        .description("인스타그램 클론 코딩 v3")
                        .version("2.0.0")
                );
    }*/
}
