package com.green.greengram4.security;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
@Builder
public class MyUserDetails implements UserDetails {
    // 요청이 왔을 때 로그인 증명, pk값도 저장이 되었을 때

    private MyPrincipal myPrincipal;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    // 1. 루틴이용 방법, return에 셋팅해야 합니다.
    // 2. 커스터마이징 방법
    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
