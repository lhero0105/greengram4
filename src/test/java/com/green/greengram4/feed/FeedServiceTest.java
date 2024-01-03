package com.green.greengram4.feed;

import com.green.greengram4.common.ResVo;
import com.green.greengram4.feed.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
// 서비스 부분은 DB랑 연관이 없습니다
@ExtendWith(SpringExtension.class)
// service에서는 @ExtendWith 로 스프링컨테이너가 객체화 됩니다. 스프링컨테이너가 올라올수있도록
// 아래 애를 빈등록합니다.
@Import({ FeedService.class })// 중괄호로 여러개 임포트가 가능합니다.
    // 빈등록 되어있는거 객체화해달라 > service 에 di 주입
class FeedServiceTest {
    // 목빈을 먼저 만들어서 주솟값을 밀어넣을 수 있도록 합니다.
    @MockBean
    // 주솟값이 있는것처럼 꾸며 진짜가 있는것 처럼 속입니다.
    private FeedMapper mapper;
    @MockBean
    private FeedPicMapper feedPicMapper;
    @MockBean
    private FeedFavMapper feedFavMapper;
    @MockBean
    private FeedCommentMapper feedCommentMapper;
    // 목 빈이 하나라도 없으면 빈 등록이 안되기에 꼭 다 적어야 합니다.

    @Autowired // 빨간 줄 무시
    private FeedService service;


    @Test
    void postFeed(){
        // 설명 : 임의의 ifeed가 리턴되어 오는지 검증
        // 가짜기에 그냥 호출하면 에러나 이상한 값이 올거기에
        when(mapper.insFeed(any())).thenReturn(1);
        // 값이 아무거나 들어 갈 때 1값을 리턴해 줘
        when(feedPicMapper.insFeedPics(any())).thenReturn(3);

        FeedInsDto dto = new FeedInsDto();
        dto.setIfeed(100);
        // autoincrement로 값이 추후에 넘어오기에 넣어주어야합니다.
        ResVo vo = service.postFeed(dto);
        assertEquals(dto.getIfeed(), vo.getResult(), "auto-increment값을 리턴하지 않음");

        // 진짜로 이 메소드 호출했는지 검증할 때 사용
        verify(mapper).insFeed(any());
        verify(feedPicMapper).insFeedPics(any());

        FeedInsDto dto2 = new FeedInsDto();
        dto2.setIfeed(200);
        ResVo vo2 = service.postFeed(dto2);
        assertEquals(dto2.getIfeed(), vo2.getResult());
    }

    @Test
    public void selFeedAll(){
        FeedSelVo feedSelVo1 = new FeedSelVo();
        feedSelVo1.setIfeed(1);
        feedSelVo1.setContents("일번 feedSelVo");

        FeedSelVo feedSelVo2 = new FeedSelVo();
        feedSelVo2.setIfeed(2);
        feedSelVo2.setContents("이번 feedSelVo");

        List<FeedSelVo> list = new ArrayList<>();
        list.add(feedSelVo1);
        list.add(feedSelVo2);
        // 작업 순서 첫번 째로 매서드 호출 시 값이 넘어오도록 사전 작업합니다.
        // 두번째로 알맞은 곳에 알맞게 값이 들어가는지 검증합니다.
        when(mapper.selFeed(any())).thenReturn(list);
        // 매퍼에서 빈 배열 두개를 리턴해주도록 합니다. // 사진 제외
        // 미들웨어가 껴서 낚아 채는 구조로 이루어져 있습니다.

        // 1번방. 배열을 리스트로하는 스트림 리스트
        List<String> feed1Pics = Arrays.stream( new String[]{"a.jpg", "b.jpg"}).toList();
        // 2번방. 리스트
        List<String> feed2Pics = new ArrayList<>();
        feed2Pics.add("가.jpg");
        feed2Pics.add("나.jpg");


        // 1. 리스트에 리스트를 담았습니다.
        List<List<String>> picsList = new ArrayList<>();
        picsList.add(feed1Pics);
        picsList.add(feed2Pics);

/*        // 리스트를 배열로
        List<String>[] picsArr2 = (List<String>[])picsList.toArray(); */
        // 2. 배열에 리스트를 담았습니다.
        List<String>[] picsArr = new List[2];
        picsArr[0] = feed1Pics;
        picsArr[1] = feed2Pics;



        when(feedPicMapper.selFeedPic(1)).thenReturn(feed1Pics);
        when(feedPicMapper.selFeedPic(2)).thenReturn(feed2Pics);
        // 이 작업을 안해주면 비어있는 []리스트 넘어옵니다.
        // 리스트가 아닐 시 디폴트 값이 넘어 옵니다.

        FeedCommentSelVo comment1SelVo = new FeedCommentSelVo();
        comment1SelVo.setIfeedComment(1);
        comment1SelVo.setComment("1");
        comment1SelVo.setCreatedAt("1");
        comment1SelVo.setWriterIuser("1");
        comment1SelVo.setWriterNm("1");
        comment1SelVo.setWriterPic("1");



        FeedCommentSelVo comment2SelVo = new FeedCommentSelVo();
        comment2SelVo.setIfeedComment(2);
        comment2SelVo.setComment("2");
        comment2SelVo.setCreatedAt("2");
        comment2SelVo.setWriterIuser("2");
        comment2SelVo.setWriterNm("2");
        comment2SelVo.setWriterPic("2");

        List<FeedCommentSelVo> commentList1 = new ArrayList<>();
        commentList1.add(comment1SelVo);
        commentList1.add(comment1SelVo);
        commentList1.add(comment2SelVo);
        commentList1.add(comment2SelVo);

        List<FeedCommentSelVo> commentList2 = new ArrayList<>();
        commentList1.add(comment1SelVo);
        commentList1.add(comment1SelVo);
        commentList1.add(comment2SelVo);
        commentList1.add(comment2SelVo);

        // 객체를 리스트에 담습니다.
        List<List<FeedCommentSelVo>> contsList = new ArrayList<>();
        contsList.add(commentList1);
        contsList.add(commentList2);

        // 객체를 리스트에 담습니다.
        List<List<FeedCommentSelVo>> contsList2 = new ArrayList<>();
        contsList2.add(commentList1);
        contsList2.add(commentList2);

        FeedCommentSelProcDto fcDto1 = new FeedCommentSelProcDto();
        fcDto1.setIfeed(feedSelVo1.getIfeed());
        fcDto1.setStardIdx(0);
        fcDto1.setRowCount(4);
        FeedCommentSelProcDto fcDto2 = new FeedCommentSelProcDto();
        fcDto2.setIfeed(feedSelVo2.getIfeed());
        fcDto2.setStardIdx(0);
        fcDto2.setRowCount(4);
        when(feedCommentMapper.selFeedCommentAll(fcDto1)).thenReturn(commentList1);
        when(feedCommentMapper.selFeedCommentAll(fcDto2)).thenReturn(commentList2);

        FeedSelDto dto = new FeedSelDto();
        List<FeedSelVo> result = service.selFeedAll(dto);// dto는 큰 의미없음
        // 같은 주솟값이 넘어옵니다.

        //System.out.println(result == list); // true 얕은복사
        assertEquals(list, result);

        for (int i = 0; i < list.size(); i++) {
            FeedSelVo vo = list.get(i);
            assertNotNull(vo.getPics());

            List<String> pics = picsList.get(i);
            assertEquals(vo.getPics(), pics);

            List<String> pics2 = picsArr[i];
            assertEquals(vo.getPics(), pics2);
        }
        for (int i = 0; i < contsList.size(); i++) {
            FeedSelVo vo = list.get(i);
            assertNotNull(vo.getComments()); //주솟값이 있는지 확인

            assertEquals(vo.getComments(), contsList.get(i));

            assertEquals(vo.getComments(), contsList2.get(i));
            System.out.println(vo);

            System.out.println(vo.getComments().size());
            // 사이즈 검증
            // 1. 사이즈가 4개일 때 분기문 작성
            if(vo.getComments().size() == 4){
                vo.getComments().remove(vo.getComments().size() - 1);
                contsList.remove(contsList.size() - 1);
                contsList2.remove(contsList2.size() - 1);

                assertEquals(vo.getComments().size(), contsList.size());
                System.out.println(vo.getComments().size());
                System.out.println(contsList.size());
                assertEquals(vo.getComments().size(), contsList2.size());
            }
            // list comment 사이즈가 맞는지 검증
            assertEquals(2, list.get(0).getComments().size());
            assertEquals(3, list.get(1).getComments().size());


        }

        // 사진을 알맞게 담고 있는지 검증합니다.
        //  위에서 주솟값이 같았기에 의미없는 작업 1
/*        for (int i = 0; i < result.size(); i++) {
            // 두개방을 순서대로 주솟값을 꺼내옵니다.
            FeedSelVo rVo = result.get(i);
            FeedSelVo pVo = list.get(i);
            assertNotNull(pVo.getPics());

            // 매서드 호출이 일어난지 검증
            verify(feedPicMapper).selFeedPic(any());

            assertEquals(pVo.getIfeed(), rVo.getIfeed());
            assertEquals(pVo.getContents(), rVo.getContents());
        }*/
        // 위에서 주솟값이 같았기에 의미없는 작업 2
/*        int i = 0;
        for (FeedSelVo vo: list) {
            // 주솟값이 제대로 오는 지 확인합니다.
            assertNotNull(vo.getPics());
            // 사진이 알맞게 들어가는지 확인합니다.
            FeedSelVo rVo = result.get(i);
            assertEquals(vo.getPics(), rVo.getPics());
            i++;
        }*/
    }
}