package com.green.greengram4.feed;

import com.fasterxml.jackson.databind.ObjectMapper;
//import com.green.greengram4.MockMvcConfig;
import com.green.greengram4.common.ResVo;
import com.green.greengram4.feed.model.FeedDelDto;
import com.green.greengram4.feed.model.FeedInsDto;
import com.green.greengram4.feed.model.FeedSelVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@MockMvcConfig // 만들어 놓은 어노테이션을 사용합니다.
@WebMvcTest(FeedController.class) // 스프링컨테이너 올려줄꺼고 빈등록된 컨테이너들을 저거해줍니다.
class FeedControllerTest {

    @Autowired
    private MockMvc mvc; // MockMvc - 타입을 어떻게 보낼지 결정
    @Autowired private ObjectMapper mapper; // 객체를 json형태로, 또는 그 반대로 변경해주는 것 의미
    @MockBean // 가짜 service 주솟값
    private FeedService service;

    @Test
    void postFeed() throws Exception{
        ResVo result = new ResVo(5);
        when(service.postFeed(any())).thenReturn(result); // when과 given은 같은 겁니다.
        //given(service.postFeed(any())).willReturn(result); // given - when - then 순서이며 when보다 given이 순서상 첫번째가 맞습니다.

        FeedInsDto dto =new FeedInsDto();
        String json = mapper.writeValueAsString(dto);
        System.out.println("json: " + json);

        mvc.perform( // 통신을 보냅니다.
                MockMvcRequestBuilders
                        // json 타입 일 때, 쿼리스트링 땐 뺍니다.
                        .post("/api/feed") // post방식일 때 적습니다.
                        .contentType(MediaType.APPLICATION_JSON) // 날릴 때 json형식이면 이 문구가 필수 입니다. -> RequestBody
                        .content(mapper.writeValueAsString(dto)) // body부분에 josn타입이 박힙니다.
                )
                .andExpect(status().isOk()) // { "result": 5 }
                .andExpect(content().string(mapper.writeValueAsString(result))) // postFeed()가 호출 했을 때 결과값
                // 기대합니다. body에 담겨진게 문자열일 껀데 json형태의 result값
                .andDo(print()); // 프린트로 찍어줍니다.

                verify(service).postFeed(any()); // 메소드 실행한지 검증
    }

    @Test
    void selFeedAll() throws Exception{
        // spring 라이브러리에 있는 애입니다.
        MultiValueMap<String, String> params = new LinkedMultiValueMap();
        params.add("page", "2");
        params.add("loginedIuser", "4");

        List<FeedSelVo> list = new ArrayList<>();
        FeedSelVo vo1 = new FeedSelVo();
        vo1.setIfeed(1);
        vo1.setContents("안녕하세여"); // 한글 입력 시 깨집니다.
        FeedSelVo vo2 = new FeedSelVo();
        vo1.setIfeed(2);
        vo1.setContents("반가워요.");
        FeedSelVo vo3 = new FeedSelVo();
        vo1.setIfeed(3);
        list.add(vo1);
        list.add(vo2);
        list.add(vo3);
        given(service.selFeedAll(any())).willReturn(list);



        // RequestParam 에노테이션이 있다면 무조건 파람을 작성해 주어야합니다. 없을 땐 안해도 됨
        mvc.perform(
                MockMvcRequestBuilders
                        .get("/api/feed")
                        .params(params) // 알아서 쿼리스트링을 만들어 줍니다. 그냥 주솟값에 쿼리스트링을 써도 됩니다.
        )
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(list)))
                // 요청받고 응답받는 것은 무엇이든 다 텍스트입니다.
                .andDo(print());

        verify(service).selFeedAll(any()); // 호출 한 지검증
    }

    @Test
    void delFeed() throws Exception{
        ResVo result = new ResVo(1);
        when(service.delFeed(any())).thenReturn(result);

        FeedDelDto dto = new FeedDelDto();
        dto.setIuser(1);
        dto.setIfeed(2);


        mvc.perform(
                MockMvcRequestBuilders
                        .delete("/api/feed")
        )
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(result)))
                .andDo(print());

        verify(service).delFeed(any()); // 호출 한 지검증
    }
}