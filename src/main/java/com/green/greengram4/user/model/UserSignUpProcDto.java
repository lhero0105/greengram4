package com.green.greengram4.user.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserSignUpProcDto {
    private int iuser;
    private String uid;
    private String upw;
    private String nm;
    private String pic;
}
