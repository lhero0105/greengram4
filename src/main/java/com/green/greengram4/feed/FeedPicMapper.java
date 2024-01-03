package com.green.greengram4.feed;

import com.green.greengram4.feed.model.FeedDelDto;
import com.green.greengram4.feed.model.FeedInsDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FeedPicMapper {
    int insFeedPics(FeedInsDto p);
    List<String> selFeedPic(int ifeed);
    int delFeedPicsAll(FeedDelDto dto);

    //List<FeedPicList> selFeedPic(List<Integer> ifeeds);
    //int delFeedPicByFeed(FeedDelDto dto); 방법 1
}
