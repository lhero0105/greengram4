package com.green.greengram4.feed;

import com.green.greengram4.feed.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FeedMapper {
    int insFeed(FeedInsDto pDto);
    List<FeedSelVo> selFeed(FeedSelDto dto);
    Integer selIfeedByFeed(FeedDelDto dto);
    int delFeed(FeedDelDto dto); // 방법 1, 2 둘다 사용
}