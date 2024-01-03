package com.green.greengram4.feed;

import com.green.greengram4.feed.model.FeedDelDto;
import com.green.greengram4.feed.model.FeedFavDto;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// h2로 바꾸지 않겠다는 뜻 입니다.
class FeedFavMapperTest {

    @Autowired // 스프링이 들고 있는 객체 주솟값을 달라 할 때 사용
    private FeedFavMapper mapper;
    @Test
    public void insFeedFavTest(){ // 안햇갈리게 메소드명 통일
        FeedFavDto dto = new FeedFavDto();
        dto.setIfeed(1);
        dto.setIuser(3);
        // 트랜젝션이기 때문에 끝나면 자동 롤백 됩니다.
        // select가 있다면 볼 수 있습니다. 없다면 새로 만들어줌

        List<FeedFavDto> result2 = mapper.selFeedFavForTest(dto);
        System.out.println(result2);
        assertEquals(0, result2.size(), "첫번째 insert전 미리 확인"); // select해 온게 0인지 확인

        int affectedRows1 = mapper.insFeedFav(dto);
        assertEquals(1, affectedRows1, "첫번째 insert"); // 같은지 확인

        List<FeedFavDto> result = mapper.selFeedFavForTest(dto);
        System.out.println(result);
        // selFeedFavForTest를 검증할 수 있는 test를 만들기는 좀 그러니
        // 얘는 처음부터 잘 만들어 주어야 합니다.
        assertEquals(1, result.size(), "첫번째 insert 확인"); // select해 온게 null이 아닌지 확인

        dto.setIuser(4);
        dto.setIfeed(1);

        int affectedRows2 = mapper.insFeedFav(dto);
        assertEquals(1, affectedRows2);
        List<FeedFavDto> result3 = mapper.selFeedFavForTest(dto);
        System.out.println(result3);
        assertEquals(1, result.size(), "두번째 insert 확인");
    }

    @Test
    public void delFeedFavTest(){
        FeedFavDto dto = new FeedFavDto();
        // 언제 돌리더라도 에러가 뜨지 않게끔 하려면 지금처럼 원본으로 하면 안됩니다.
        dto.setIfeed(1);
        dto.setIuser(2);

        int affectedRow1 = mapper.delFeedFav(dto);
        assertEquals(1, affectedRow1);

        int affectedRow2 = mapper.delFeedFav(dto);
        assertEquals(0, affectedRow2);//검증 1

        List<FeedFavDto> result2 = mapper.selFeedFavForTest(dto);
        // 레코드가 하나 일 땐 경우의 수는 두개입니다.
        // list일 땐 list는 넘어 오지만 size는 0입니다.
        System.out.printf("result2 : ", result2);// 검증 2
        assertEquals(0, result2.size(), "delete 확인");

    }

    @Test
    public void delFeedFavAllTest(){
        final int ifeed = 1;
/*
        FeedDelDto dto = new FeedDelDto();
        FeedDelDto feedDelDto = mapper.selFeedFavForTest2(dto);
        System.out.printf("feedDelDto : ", feedDelDto);
        assertNotNull(feedDelDto);
*/

        FeedFavDto selDto = new FeedFavDto();
        selDto.setIfeed(ifeed);
        List<FeedFavDto> selList = mapper.selFeedFavForTest(selDto);
        assertNotNull(selList);

        FeedDelDto dto = new FeedDelDto();
        dto.setIfeed(ifeed);
        int delAffectedRows = mapper.delFeedFavAll(dto);
        assertEquals(selList.size(), delAffectedRows);

        List<FeedFavDto> selList2 = mapper.selFeedFavForTest(selDto);
        assertEquals(0, selList2.size());
    }
}