package com.green.greengram4.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component // 빈등록
public class JwtTokenProvider {
    // final을 붙혀야 바뀔 가능성을 없앨 수 있습니다.
    private final String secret; // 라이브러리가 긴 글을 주입 해 줍니다.
    private final String headerSchemeName;
    private final String tokenType;
    private Key key;

    // 생성자로 yaml에 적은 친구의 값을 secret에 넣어줍니다.
    public JwtTokenProvider(@Value("${springboot.jwt.secret}") String secret
                            , @Value("${springboot.jwt.header-schema-name}") String headerSchemeName
                            , @Value("${springboot.jwt.token-type}") String tokenType
    ){
        this.secret = secret;
        log.info("secret: {}");
        this.headerSchemeName = headerSchemeName;
        this.tokenType = tokenType;
    }

    @PostConstruct // di가 주입되고 나서 메소드가 호출되고자 할 때, 이 방식이 가장 최신
    public void init(){ // 호출 해줘서 실행
        log.info("secret: {}", secret);
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes); // 키 발행
    }
    // 여기서부터
    public String generateToken(MyPrincipal principal, long tokenValidMs){
        Date now = new Date();
        return Jwts.builder()
                //.issuedAt(now) 발행일자 입니다. 아래 내용과 동일
                .claims(createClaims(principal)) // 값을 담습니다.
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + tokenValidMs))
                .signWith(this.key)
                .compact();
    }
    private Claims createClaims(MyPrincipal principal){
        return Jwts.claims()
                .add("iuser", principal.getIuser())
                .build();
    }
    // 여기까지 JWT 만드는 과정 입니다.

    // 만든 JWT 해석하는 법
    // 토큰 뽑아내는 법
    public String resolveToken(HttpServletRequest req){ // HttpServletRequest에는 모든 요청 정보가 다 들어 있습니다.
        String auth = req.getHeader(headerSchemeName);// headre에 담겨져 있는거 중에 headerSchemeName를 달라는 것 입니다.
        if(auth == null){return  null;}
        if(auth.startsWith(tokenType)){
            return auth.substring(tokenType.length()).trim();
        }
        return null;
    }
}
