package com.green.greengram4.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

// 잘못된 토큰, 만료퇸 토큰, 지원되지 않는 토큰 > 응답
// 커스터마이징입니다.
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // ( 요청, 응답, 에러 )
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
