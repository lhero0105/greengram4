package com.green.greengram4.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// 인증 로그인
// 인가 권한
// 이 파일은 인가 부분 입니다
@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean // 스프링 컨테이너가 호출 해줍니다.
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        // 인가 단계에서 쓰이는 메소드가 필터입니다.
        return httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(http -> http.disable()) // 기본적인 창 사용 안하겠다(리소스 확보)
                .formLogin(from -> from.disable()) // form태그 로그인를 끕니다.
                .csrf(csrf -> csrf.disable()) // 보안기능(보는 화면이 없어서)
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/user/signin"
                                                                    , "/api/user/signup"
                                                                    , "/error"
                                                                    , "/err"
                                                                    , "/"
                                                                    , "/index.html"
                                                                    , "/static/**" // 로그인을 해도 이걸 안적으면 접근안됩니다.
                                                                    , "/swagger.html" // 스웨거는 3개를 작성해야 합니다.
                                                                    , "/swagger-ui/**"
                                                                    , "/v3/api-docs/**" // 스웨거 라이프러리 부분
                                                                    , "/api/user/refresh-token"
                                        ).permitAll() //매치 시키는데.permitAll : 무사 통과
                                        .anyRequest().authenticated() // 나머지는 무조건 로그인이 되어야 합니다.
                                        //.requestMatchers(HttpMethod.GET, "/sign-api/refresh-token").permitAll()
                                        //.requestMatchers(HttpMethod.GET, "/product/**").permitAll()
                                        //.requestMatchers(HttpMethod.POST, "/product/**").permitAll()
                                        //.requestMatchers("**exception**").permitAll()
                                        //.requestMatchers("/todo-api").hasAnyRole("USER", "ADMIN")

                        // .anyRequest().hasRole("ADMIN"))// anyRequest 는 항상 잴 아래에 있어야 합니다.
                                        //.anyRequest().authenticated()) // 로그인 할 때만 사용 가능 합니다.
                )
                // 필터를 중간에 끼워 넣습니다.
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // 익셉션 핸들러로 커스터마이징 한 에러 사용
                .exceptionHandling(except -> {
                    except.authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                            .accessDeniedHandler(new JwtAccessDeniedHandler());
                })
                .build(); // sessionManagement : 세션 사용 x(로그인 시 세션사용 안할거기에)
    }
    @Bean // 스프링에 빈등록 하여 나중 변경 시 편하게 사용가능 합니다.
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
