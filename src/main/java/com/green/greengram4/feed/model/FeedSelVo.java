package com.green.greengram4.feed.model;

import lombok.Data;

import java.util.List;

@Data
public class FeedSelVo {
    private int ifeed;
    private int writerIuser;
    private String contents;
    private String location;
    private String writerNm;
    private String createdAt;
    private String writerPic;
    private int isFav;
    private List<String> pics;
    private List<FeedCommentSelVo> comments;
    private int isMoreComments;
}
