package com.green.greengram4.feed;

import com.green.greengram4.common.Const;
import com.green.greengram4.common.ResVo;
import com.green.greengram4.feed.model.*;
import com.green.greengram4.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
// 생성자를 통한 di 주입
@Slf4j
public class FeedService {
    private final FeedMapper mapper;
    private final FeedPicMapper feedPicMapper;
    private final FeedFavMapper feedFavMapper;
    private final FeedCommentMapper feedCommentMapper;
    private final AuthenticationFacade authenticationFacade;

    public ResVo postFeed(FeedInsDto dto) {
        dto.setIuser(authenticationFacade.getLoginUserPk());
        log.info("dto : {}", dto);
        int feedAffectedRows = mapper.insFeed(dto);
        log.info("feedAffectedRows: {}", feedAffectedRows);
        int feedPicsAffectedRows = feedPicMapper.insFeedPics(dto);
        log.info("feedPicsAffectedRows: {}", feedPicsAffectedRows);
        return new ResVo(dto.getIfeed());
    }
    public List<FeedSelVo> selFeedAll(FeedSelDto dto){
        List<FeedSelVo> list = mapper.selFeed(dto);

        FeedCommentSelProcDto fcDto = new FeedCommentSelProcDto();
        fcDto.setStardIdx(0);
        fcDto.setRowCount(Const.FEED_COMMENT_FIRST_CNT);

        for ( FeedSelVo vo : list ) {
            vo.setPics(feedPicMapper.selFeedPic(vo.getIfeed()));
            fcDto.setIfeed(vo.getIfeed());
            vo.setComments(feedCommentMapper.selFeedCommentAll(fcDto));

            if(vo.getComments().size() == Const.FEED_COMMENT_FIRST_CNT){
                vo.setIsMoreComments(1);
                vo.getComments().remove(vo.getComments().size() - 1);
            }

        }
        // return new ArrayList<>();
        return list;
    }
    public ResVo delFeed(FeedDelDto dto){
        // 방법 1 - select 해서 확인 후 차례대로 삭제
        /*Integer ifeed = mapper.selIfeedByFeed(dto);
        if(ifeed == null){
            return new ResVo(0);
        }
        feedPicMapper.delFeedPicByFeed(dto);
        feedFavMapper.delFeedFavByFeed(dto);
        feedCommentMapper.delFeedCommentByFeed(dto);
        mapper.delFeed(dto);
        return new ResVo(1);*/

        // 방법 2 = iuser ifeed에 해당하는 pic삭제 후 영향받은 행에 따라 삭제
        // not null 이기에 pic을 활용
        // 1. 이미지
        int picsAffedtedRows = feedPicMapper.delFeedPicsAll(dto);
        if(picsAffedtedRows == 0){
            return new ResVo(Const.FAIL);
            // 사실 뭐때문에 삭제 안됐는지 알려주어야 합니다.
        }
        // 원래는 트랜잭션을 걸어 하나라도 실패시 롤백 되도록 해주어야 합니다.
        // 2. 좋아요
        int favAffedtedRows = feedFavMapper.delFeedFavAll(dto);
        // 3. 댓글
        int commentAffetedRows = feedCommentMapper.delFeedCommentAll(dto);
        // 4. 피드
        int feedAffetedRows = mapper.delFeed(dto);
        return new ResVo(Const.SUCCESS);


    }

    public ResVo feedFavToggle(FeedFavDto dto){
        int affectedRows = feedFavMapper.delFeedFav(dto);
        if(affectedRows == 1){
            return new ResVo(Const.FEED_FAV_DELETE);
        }
        feedFavMapper.insFeedFav(dto);
        return new ResVo(Const.FEED_FAV_ADD);
    }
}
