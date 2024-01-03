package com.green.greengram4.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor // readvalue 시 setter 로 넣기에 기본생성자가 있어야합니다.
@Getter
public class ResVo {
    private int result;
}
