package com.green.greengram4.feed;


import com.green.greengram4.common.ResVo;
import com.green.greengram4.feed.model.FeedCommentInsDto;
import com.green.greengram4.feed.model.FeedCommentSelProcDto;
import com.green.greengram4.feed.model.FeedCommentSelVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedCommentService {
    private final FeedCommentMapper mapper;
    public ResVo postFeedComment(FeedCommentInsDto dto){
        int affectedRows = mapper.insFeedComment(dto);
        log.info("affectedRows : {}", affectedRows);
        return new ResVo(dto.getIfeedComment());
    }
    public List<FeedCommentSelVo> getFeedCommentAll(int ifeed){
        FeedCommentSelProcDto pDto = new FeedCommentSelProcDto();
        pDto.setIfeed(ifeed);
        pDto.setStardIdx(3);
        pDto.setRowCount(999);
        return mapper.selFeedCommentAll(pDto);
    }
}
