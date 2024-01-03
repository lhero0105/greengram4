package com.green.greengram4.feed;

import com.green.greengram4.feed.model.FeedDelDto;
import com.green.greengram4.feed.model.FeedFavDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FeedFavMapper {
    int insFeedFav(FeedFavDto dto);
    List<FeedFavDto> selFeedFavForTest(FeedFavDto dto);
    // 테스트에서만 사용합니다.
    int delFeedFav(FeedFavDto dto);
/*    int delFeedFavByFeed(FeedDelDto dto); //방법 1*/
    int delFeedFavAll(FeedDelDto dto); //방법 2
}
