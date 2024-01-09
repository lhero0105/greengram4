package com.green.greengram4.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.greengram4.common.AppProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component // 빈등록
@RequiredArgsConstructor
public class JwtTokenProvider {
    // final을 붙혀야 바뀔 가능성을 없앨 수 있습니다.
/*    private final String secret;
    private final String headerSchemeName;
    private final String tokenType;*/

    private final ObjectMapper om;
    private final AppProperties appProperties; // di주입이 이루어집니다.
    private Key key;


    // 생성자로 yaml에 적은 친구의 값을 secret에 넣어줍니다.
/*
    public JwtTokenProvider(@Value("${springboot.jwt.secret}") String secret
                            , @Value("${springboot.jwt.header-schema-name}") String headerSchemeName
                            , @Value("${springboot.jwt.token-type}") String tokenType
    ){
        this.secret = secret;
        log.info("secret: {}");
        this.headerSchemeName = headerSchemeName;
        this.tokenType = tokenType;
    }
*/

    @PostConstruct // di가 주입되고 나서 메소드가 호출되고자 할 때, 이 방식이 가장 최신
    public void init(){ // 호출 해줘서 실행
        log.info("secret: {}", appProperties.getJwt().getSecret());
        byte[] keyBytes = Decoders.BASE64.decode(appProperties.getJwt().getSecret());
        this.key = Keys.hmacShaKeyFor(keyBytes); // 키 발행
    }
    // 여기서부터
    public String generateAccessToken(MyPrincipal principal){
        return generateToken(principal, appProperties.getJwt().getAccessTokenExpiry());
    }

    public String generateRefreshToken(MyPrincipal principal){
        return generateToken(principal, appProperties.getJwt().getRefreshTokenExpiry());
    }

    public String generateToken(MyPrincipal principal, long tokenValidMs){
        Date now = new Date();
        return Jwts.builder()
                //.issuedAt(now) 발행일자 입니다. 아래 내용과 동일
                .claims(createClaims(principal)) // 값을 담습니다.
                .issuedAt(new Date(System.currentTimeMillis())) // 성능이슈 -> 이렇게 해주면 값의 오차가 줄어듭니다.
                .expiration(new Date(System.currentTimeMillis() + tokenValidMs)) // 만료시간
                .signWith(this.key) // 키 저장
                .compact(); // build()와 동일
    }
    // key와 value를 저장할 수 있습니다.
    private Claims createClaims(MyPrincipal principal){
        try {
            // 객체를 문자열로 바꿔서 보냅니다. 자바스크립트도 동일
            String json = om.writeValueAsString(principal); //에러가 있으면 호출한 친구가 처리하도록 던집니다.
            return Jwts.claims()
                    .add("iuser", json)
                    .build();
        } catch (Exception e){
            return null;
        }
    }
    // 여기까지 JWT 만드는 과정 입니다.




    // 만든 JWT 해석하는 법
    // 토큰 뽑아내는 법
    public String resolveToken(HttpServletRequest req){ // HttpServletRequest에는 모든 요청 정보가 다 들어 있습니다.
        String auth = req.getHeader(appProperties.getJwt().getHeaderSchemaName());// header에 담겨져 있는거 중에 headerSchemeName에 해당하는 vaule값이 담깁니다.
        if(auth == null){return  null;}

        // 0123456 substring으로 6부터 자릅니다. 띄워쓰기 포함
        // trim() 메서드는 문자열 양 끝의 공백을 제거
        // Bearer DaeguGreenArtAcademyClass502RoomForJavaSpringBootFighting
        // 시작하는 값이 TokenType 이면 실행합니다.
        if(auth.startsWith(appProperties.getJwt().getTokenType())){
            return auth.substring(appProperties.getJwt().getTokenType().length()).trim();
        }
        // 요약 Bearer 이면 DaeguGreenArtAcademyClass502RoomForJavaSpringBootFighting 리턴
        return null;
    }

    public boolean isValidateToken(String token){
        try {//                     만료기간
        return !getAllClaims(token).getExpiration().before(new Date()); // Date가 전인지 물어보는 겁니다.
            // 현재시간이 만료기간보다 후이면 false
            // 현재시간이 만료기간보다 전이면 true
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getAllClaims(String token){
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody(); // 옛날 버전입니다.
    }

    public Authentication getAuthentication(String token){
        UserDetails userDetails = getUserDetailsFromToken(token);

        return userDetails == null
                ? null
                : new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private UserDetails getUserDetailsFromToken(String token){
        try {
        Claims claims = getAllClaims(token); // 다 가져옵니다
        String json = (String) claims.get("iuser");
        MyPrincipal myPrincipal = om.readValue(json, MyPrincipal.class);
        return MyUserDetails.builder()
                .myPrincipal(myPrincipal)
                .build();
        } catch (Exception e) {
            return null;
        }
    }
}
