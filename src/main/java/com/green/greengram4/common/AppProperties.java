package com.green.greengram4.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
// 이 전에 맴버 필드에 쓰건 애들을 클래스로 따로 빼 관리합니다.
@Getter
@ConfigurationProperties(prefix = "app") // prefix 접두 = "yaml에 있는 app입니다."
// @ConfigurationPropertiesScan 없어서 에러, 시작 파일에 어노태이션 임포트
public class AppProperties {
    // inner 클래스
    private final Jwt jwt = new Jwt();// 객체 생성하여 빈등록이 이루어 집니다.

    @Getter
    @Setter
    public class Jwt {
        private String secret;
        private String headerSchemaName;
        private String tokenType;
        private Long accessTokenExpiry;
        private Long refreshTokenExpiry;
        // 프로그램 실행 시 yaml의 내용이 주입 됩니다.
    }
}
