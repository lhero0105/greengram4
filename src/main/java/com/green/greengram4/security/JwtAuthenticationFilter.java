package com.green.greengram4.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // 앞 단에서 한번 거치고 헤더에 토큰이 있다면 Authentication에 값을 넣습니다.
    // 로그인 한지 안한지는 Authentication에 값이 있냐 없냐로 구분 됩니다.

    // 빈등록 후 di 주입 됩니다.
    private final JwtTokenProvider jwtTokenProvider;

    // 스프링컨테이너가 메소드를 호출합니다.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 요청이 들어 올때 마다 실행 값이 넘어오면 로그인 유저, null이 넘어오면 비로그인유저
        String token = jwtTokenProvider.resolveToken(request);
        log.info("JwtAuthentication-Token : {}", token);

        if(token != null && jwtTokenProvider.isValidateToken(token)){
            Authentication auth = jwtTokenProvider.getAuthentication(token); // 알맞는 상태로 만들어서 전달
            if(auth != null){
                SecurityContextHolder.getContext().setAuthentication(auth);
                // 그림 보면 대충 이해갑니다.
            }
        }
        // filter의 흐름을 따라 다음 filter에 넘겨주어야합니다.
        filterChain.doFilter(request, response);
    }
}
