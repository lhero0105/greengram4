package com.green.greengram4.security;

import lombok.*;

// 용량을 최소화 하여 토큰에 집어 넣을 때 씁니다.
// 유연하게 하기 위해 객체를 씁니다.
// 롤, 권한까지 담아줍니다.
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor // 빌더 도 쓰고 싶을 때
public class MyPrincipal {
    private int iuser;
    // 애 객체가 Authentication 에 담겨야 하는데
    // 담길 때는 임플리먼트 한 애를 담을 수 있습니다.
}
