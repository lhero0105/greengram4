package com.green.greengram4.user;

import com.green.greengram4.common.AppProperties;
import com.green.greengram4.common.Const;
import com.green.greengram4.common.CookieUtils;
import com.green.greengram4.common.ResVo;
import com.green.greengram4.security.JwtTokenProvider;
import com.green.greengram4.security.MyPrincipal;
import com.green.greengram4.security.MyUserDetails;
import com.green.greengram4.user.model.*;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserMapper mapper;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AppProperties appProperties;
    private final CookieUtils cookieUtils;

    public ResVo signup(UserSignupDto dto){
        //비밀번호 암호화
//        String hashPw = BCrypt.hashpw(dto.getUpw(), BCrypt.gensalt());
        String hashedPw = passwordEncoder.encode(dto.getUpw());
        UserSignUpProcDto pDto = UserSignUpProcDto.builder()
                .uid(dto.getUid())
                .upw(hashedPw)
                .nm(dto.getNm())
                .pic(dto.getPic())
                .build();
        int affectedRow = mapper.insUser(pDto);
        log.info("affectedRow : {}", affectedRow);
        return new ResVo(pDto.getIuser());//회원가입 한 iuser pk값 리턴
    }
    public UserSigninVo signin(HttpServletRequest req, HttpServletResponse res, UserSigninDto dto){
        UserSelDto sDto = new UserSelDto();
        sDto.setUid(dto.getUid());

        UserEntity entity = mapper.selUser(sDto);
        UserSigninVo vo = new UserSigninVo();
        if(entity == null){
            vo.setResult(Const.LOGIN_NO_UID); // 의미있는 값을 common테이블에 뺴줍니다.
            return vo;
        } else if(!passwordEncoder.matches(dto.getUpw(), entity.getUpw())){
            vo.setResult(Const.LOGIN_DIFF_UPW);
            return vo;
        }
        // --
        MyPrincipal myPrincipal = new MyPrincipal();
        myPrincipal.setIuser(entity.getIuser());
        String at = jwtTokenProvider.generateAccessToken(myPrincipal);
        String rt = jwtTokenProvider.generateRefreshToken(myPrincipal);
        // --
        int rtCookieMaxAge = appProperties.getJwt().getRefreshTokenCookieMaxAge();
        cookieUtils.deleteCookie(res, "rt");
        // 삭제를 한번 시키고 다시 담습니다.
        cookieUtils.setCookie(res, "rt", rt, rtCookieMaxAge);

        Boolean comparedPw = BCrypt.checkpw(dto.getUpw(), entity.getUpw());
        if(comparedPw){
            vo.setResult(Const.SUCCESS);
            vo.setIuser(entity.getIuser());
            vo.setNm(entity.getNm());
            vo.setPic(entity.getPic());
            vo.setFirebaseToken(entity.getFirebaseToken());
            vo.setAccessToken(at);
            return vo;
        }
        vo.setResult(Const.LOGIN_DIFF_UPW);
        return vo;
    }
    public ResVo toggleFollow(UserFollowDto dto){
        // 삭제 먼저
        int affectedRow = mapper.delUserFollow(dto);
        if(affectedRow == 1){
            return new ResVo(Const.USER_FOLLOW_DELETE);
            // 영향받은행이 있으면 리턴
        }
        // 등록
        mapper.insUserFollow(dto);
        return new ResVo(Const.USER_FOLLOW_INSERT);
    }

    public ResVo signout(HttpServletResponse res){
        // AT RT는 stateless이며, 발행
        cookieUtils.deleteCookie(res, "rt");
        return new ResVo(1);
    }

    public UserSigninVo getRefreshToken(HttpServletRequest req){
        Cookie cookie = cookieUtils.getCookie(req, "rt");
        // 쿠키 없음 체크
        if(cookie == null){
            UserSigninVo vo = new UserSigninVo();
            vo.setResult(Const.FAIL);
            vo.setAccessToken(null);
            return vo;
        }
        String token = cookie.getValue();
        // 기간지남 체크
        if(!jwtTokenProvider.isValidateToken(token)){
            UserSigninVo vo = new UserSigninVo();
            vo.setResult(Const.FAIL);
            vo.setAccessToken(null);
            return vo;
        }

        MyUserDetails myUserDetails = (MyUserDetails) jwtTokenProvider.getUserDetailsFromToken(token);
        MyPrincipal myPrincipal = myUserDetails.getMyPrincipal();

        String at = jwtTokenProvider.generateAccessToken(myPrincipal);

        UserSigninVo vo = new UserSigninVo();
        vo.setResult(Const.SUCCESS);
        vo.setAccessToken(at);
        return vo;
    }

    public UserInfoVo getUserInfo(UserInfoSelDto dto){
        return mapper.selUserInfo(dto);
    }
    public ResVo patchUserFirebaseToken(UserFirebaseTokenPatchDto dto) {
        int affectedRows = mapper.updUserFirebaseToken(dto);
        return new ResVo(affectedRows);
    }
    public ResVo patchUserPic(UserPicPatchDto dto) {
        int affectedRows = mapper.updUserPic(dto);
        return new ResVo(affectedRows);
    }
}
