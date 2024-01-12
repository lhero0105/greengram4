package com.green.greengram4.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(SpringExtension.class) // 야믈파일이 안올라오니 있는것 처럼 꾸며주는 겁니다.
@Import({ MyFileUtils.class })
@TestPropertySource(properties = {
        "file.dir=D:/ggg/download",
}) // 야믈파일 적용이 안되어 테스트 시 사용될 주소를 입력합니다.
public class MyFileUtilsTest {

    @Autowired
    private MyFileUtils myFileUtils;
    @Test
    public void makeFolderTest(){
        String path = ("/ggg2");
        // File - 컴퓨터 안에 있는 파일들을 io할 수 있습니다.
        File preFolder = new File(myFileUtils.getUploadPrefixPath(), path);
        // 문자열 합치기가 자동으로 되며,
        assertEquals(false, preFolder.exists());
        // 파일인지 폴더인지 상관없이 존재하냐 물어봅니다.

        String newPath = myFileUtils.makeFolders(path);
        // newPath에 만들어진 합쳐진 값이 들어갑니다.
        File newFolder = new File(newPath);
        assertEquals(preFolder.getAbsolutePath(), newFolder.getAbsolutePath());
        assertEquals(true, newFolder.exists());
    }

    @Test
    public void getRandomFileNmTest(){
        String fileNm = myFileUtils.getRandomFileNm();;
        System.out.println("fileNm : " + fileNm);
        Assertions.assertNotNull(fileNm);
        assertNotEquals("", fileNm);
    }

    @Test
    public void getExtTest(){
        String fileNm = "abc.efg.eee.jpg";

        String ext = myFileUtils.getExt(fileNm);
        assertEquals(".jpg", ext);

        String fileNm2 = "jjj-asdfasd.png";
        String ext2 = myFileUtils.getExt(fileNm2);
        assertEquals(".png", ext2);
    }

    @Test
    public void getRandomFileNm2(){
        String fileNm1 = "반갑다.친구야.jpg";
        String rFileNm1 = myFileUtils.getRandomFileNm(fileNm1);
        System.out.println("rFileNm1 " + rFileNm1);
        //랜덤 문자열.jpeg

        String fileNm2 = "크크크.ㅇㅁㅈㅇㅇㅈㅁ.qq";
        String rFileNm2 = myFileUtils.getRandomFileNm(fileNm2);
        System.out.println("rFileNm1 " + rFileNm2);
        //랜덤 문자열.qq
    }

}
