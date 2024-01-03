package com.green.greengram4.feed.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Builder
@Getter
public class FeedPicInsProcDto {
    private int ifeed;
    private List<String> pics;
}
