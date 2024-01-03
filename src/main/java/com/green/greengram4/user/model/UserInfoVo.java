package com.green.greengram4.user.model;

import lombok.Data;

@Data
public class UserInfoVo {
    private int feedCnt;
    private int favCnt;
    private String nm;
    private String createdAt;
    private String pic;
    private int follower; // 팔로워 수 (targetIuser를 팔로우하는 사람)
    private int following; // 팔로잉 수 (targetIuser가 팔로우하는 사람)
    private int followState; //
    // 0 - 둘 다 팔로우 안 함
    // 1 - loginedIuser가 targetIuser를 팔로우 한 상황
    // 2 - targetIuser 가 loginedIuser를 팔로우만 한 상황
    // 3 - 둘 다 팔로우 한 상황
}
