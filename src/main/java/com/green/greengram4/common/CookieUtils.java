package com.green.greengram4.common;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class CookieUtils {
    // request 담긴 쿠키를 가져오는 부분 -> 갱신 시
    public Cookie getCookie(HttpServletRequest request, String name){
        Cookie[] cookies = request.getCookies();

        if(cookies != null && cookies.length > 0){
            for (Cookie cookie : cookies) {
                if(name.equals(cookie.getName())){
                    return cookie;
                }
            }
        }
        return null;
    }

    // 쿠키를 담는 부분
    public void setCookie(HttpServletResponse response, String name, String value, int maxAge){
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        // 모든 url에서 쿠키를 사용할 수 있음
        cookie.setHttpOnly(true);
        // 외부 컴퓨터에서 탈취 할 수 없습니다.
        cookie.setMaxAge(maxAge);
        // 시간은 초 값
        response.addCookie(cookie);
        // 응답 시 계속 담겨서 올 겁니다.
    }

    // 쿠키를 삭제 하는 부분
    public void deleteCookie(HttpServletResponse response, String name){
        //Cookie cookie = getCookie(request, name);
        //if(cookie == null){ return; }
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
