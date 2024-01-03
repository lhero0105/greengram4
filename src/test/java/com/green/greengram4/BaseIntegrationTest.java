package com.green.greengram4;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

// 통합테스트, 피드별, 유저별 등 해서 상속합니다.
@Slf4j
@Import(CharEncodingConfig.class)
//@MockMvcConfig // 둘 중에 하나만 사용하면 됩니다.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // 통합테스트에서 사용 ()안에는 알아서 랜덤포드
@AutoConfigureMockMvc
@Transactional //
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BaseIntegrationTest {
    @Autowired protected MockMvc mvc; // test용 MVC환경을 만듦
    @Autowired protected ObjectMapper om; // 진짜 한테 호출합니다.
    // 데이터로 통신할 때 xml json 을 사용합니다.
}