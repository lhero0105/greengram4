package com.green.greengram4.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {
    public MyUserDetails getLoginUser(){
        return (MyUserDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    public int getLoginUserPk(){
        return getLoginUser()
                .getMyPrincipal()
                .getIuser();
    }
}
