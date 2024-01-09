package com.green.greengram4.user.model;

import lombok.Data;

@Data
public class UserSigninVo {
    private int result; // iuser로 분기하지 않고 코드로 분기합니다.
    private int iuser;
    private String nm;
    private String pic;
    private String firebaseToken;
    private String accessToken;
}