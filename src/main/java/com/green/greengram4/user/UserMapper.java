package com.green.greengram4.user;

import com.green.greengram4.user.model.*;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    int insUser(UserSignUpProcDto dDto);
    UserEntity selUser(UserSelDto dto);
    // 관리자가 범용적으로 관리할 떄 (로그인 할 때 포함)
    int delUserFollow(UserFollowDto dto);
    int updUserFirebaseToken(UserFirebaseTokenPatchDto dto);
    int updUserPic(UserPicPatchDto dto);
    int insUserFollow(UserFollowDto dto);
    UserInfoVo selUserInfo(UserInfoSelDto dto);
}
