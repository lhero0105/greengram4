package com.green.greengram4.security;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

// 유연하게 하기 위해 객체를 씁니다.
// 롤, 권한까지 담아줍니다.
@Getter
@Setter
@Builder
public class MyPrincipal {
    private int iuser;
}
