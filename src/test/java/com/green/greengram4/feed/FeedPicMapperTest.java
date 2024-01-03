package com.green.greengram4.feed;

import com.green.greengram4.feed.model.FeedDelDto;
import com.green.greengram4.feed.model.FeedInsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FeedPicMapperTest {
    private FeedInsDto dto;
    public FeedPicMapperTest(){
        this.dto = new FeedInsDto();
        this.dto.setIfeed(4);

        List<String> pics = new ArrayList<>();
        pics.add("a.jpg");
        pics.add("b.jpg");
        this.dto.setPics(pics);
    }
    @Autowired
    private FeedPicMapper mapper;

    @BeforeEach
    public void beforeEach(){
        FeedDelDto delDto = new FeedDelDto();
        delDto.setIfeed(this.dto.getIfeed());
        delDto.setIuser(2);
        int affectedRows = mapper.delFeedPicsAll(delDto);
        System.out.println("delRow : " + affectedRows );
    }
    @Test
    void insFeedPic() {
/*        FeedPicInsProcDto dto = FeedPicInsProcDto.builder()
                .ifeed(this.dto.getIfeed())
                .build();
        List<String> pics1 = mapper.selFeedPic(dto.getIfeed());
        assertEquals(0, pics1.size());
        dto.getPics().add(1, "a");
        dto.getPics().add(2, "b");
        dto.getPics().add(3, "c");
        int affectedRows = mapper.insFeedPics(dto);
        assertEquals(1, affectedRows);
        List<String> pics2 = mapper.selFeedPic(dto.getIfeed());
        assertNotEquals(0, pics2.size());*/

        List<String> preList = mapper.selFeedPic(dto.getIfeed());
        assertEquals(0, preList.size());

        int insAffectedRows = mapper.insFeedPics(dto);
        assertEquals(dto.getPics().size(), insAffectedRows);

        List<String> afterList = mapper.selFeedPic(dto.getIfeed());
        assertEquals(dto.getPics().size(), afterList.size());

        // 사진 갯수가 아닌 정확한 string이 들어간지 확인
/*        assertEquals(dto.getPics().get(0), afterList.get(0));
        assertEquals(dto.getPics().get(1), afterList.get(1));*/
        for (int i = 0; i < dto.getPics().size(); i++) {
            assertEquals(dto.getPics().get(i), afterList.get(i));
        }
    }
}