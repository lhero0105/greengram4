package com.green.greengram4.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(title = "회원가입 데이터")
public class UserSignupDto {
    @Schema(title = "유저 아이디", example = "mic")
    private String uid;
    @Schema(title = "유저 비밀번호", example = "1212")
    private String upw;
    @Schema(title = "유저 이름", example = "김춘식")
    private String nm;
    @Schema(title = "유저 사진", example = "aaa.jpg")
    private String pic;
}
