package com.green.greengram4.common;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Getter
@Component
public class MyFileUtils {
    private final String uploadPrefixPath;
    // final이 붙으면 객체생성 할 때 초기화가 되어야합니다.
    public MyFileUtils(@Value("${flil.dir}") String uploadPrefixPath){
        // spring에게 flil안에 dir을 가져와서 쓰라고 알려줍니다.
        this.uploadPrefixPath = uploadPrefixPath;
    }

    // 폴더 만들기
    public String makeFolders(String path){
        File folder = new File(uploadPrefixPath, path);
        folder.mkdirs();
                // make directorys줄임말
        return folder.getAbsolutePath();
        // 문자열 합친 값을 AbsolutePath에 저장
        // 절대주소 : 전체주소
        // 상대주소 : 기준주소로 부터 위치 사용 시 씁니다.
    }

    // 랜덤 파일명 만들기
    public String getRandomFileNm(){
        return UUID.randomUUID().toString();
        // UUID - 범용 고유 식별자, 계속 돌릴 때 10년에 한번 정도 중복
    }

    // 확장자 얻어오기
    public String getExt(String fileNm){
        return fileNm.substring(fileNm.lastIndexOf("."));
    }

    // 랜덤 파일명 만들기 with 확장자
    public String getRandomFileNm(String originFileNm){
        return getRandomFileNm() + getExt(originFileNm);
    }

    // 랜덤 파일명 만들기 with 확장자 from MultipartFile
    public String getRandomFileNm(MultipartFile mf){
        String fileNm = mf.getOriginalFilename();
        return getRandomFileNm(fileNm);
    }
}
