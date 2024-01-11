package com.green.greengram4.user;

import com.green.greengram4.common.ResVo;
import com.green.greengram4.user.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "user", description = "유저관련")
@Slf4j
public class UserController {
    private final UserService service;

    // 회원가입
    // get방식에서만 쓰입니다 post에서 쓰면 쿼리스트링으로 주솟값이 입력됨
    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "회원가입 처리")
    @Parameters(value = {
            @Parameter(name="uid", description = "아이디")
            , @Parameter(name="upw", description = "비밀번호")
            , @Parameter(name="nm", description = "이름")
            , @Parameter(name="pic", description = "프로필 사진")
    })
    public ResVo postSignup(@RequestBody UserSignupDto dto){
        log.info("dto : {}", dto);
        return service.signup(dto);
    }

    // 로그인
    @PostMapping("/signin")
    @Operation(summary = "인증", description = "아이디/비번 활용한 인증처리")
    public UserSigninVo postSignin(HttpServletRequest req
                                   , HttpServletResponse res
                                   , @RequestBody UserSigninDto dto){
        log.info("dto: {}", dto);
        return service.signin(req, res, dto); // 1성공, 2:아이디없음, 3:비번틀림
    }

    // 웹 푸시
    @PatchMapping("/firebase-token")
    public ResVo patchUserFirebaseToken(@RequestBody UserFirebaseTokenPatchDto dto) {
        return service.patchUserFirebaseToken(dto);
    }
    // 유저 프사 변경
    @PatchMapping("/pic")
    public ResVo patchUserPic(@RequestBody UserPicPatchDto dto) {
        return service.patchUserPic(dto);
    }
    // -----------follow
    // ResVo - result : 1-following, 0-취소
    @PostMapping("/follow")
    public ResVo toggleFollow(@RequestBody UserFollowDto dto){
        return service.toggleFollow(dto);
    }

    @PostMapping("/signout")
    public ResVo postsignout( HttpServletResponse res){
        return service.signout(res);
    }

    @GetMapping("/refresh-token")
    public UserSigninVo getRefreshToken(HttpServletRequest req){
        return service.getRefreshToken(req);
    }

    // 유저 프로필 정보
    @GetMapping
    @Operation(summary = "유저 정보", description = "프로필 화면에서 사용할 프로필 유저 정보")
    public UserInfoVo getUserInfo(UserInfoSelDto dto) {
        log.info("dto: {}", dto);
        return service.getUserInfo(dto);
    }
}
