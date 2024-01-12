package com.green.greengram4.feed.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class FeedInsDto {
    //@JsonIgnore
    @Schema(hidden = true)
    private int ifeed;
    @JsonIgnore // iuser를 이제 안받습니다. JWT이용
    private int iuser;
    private String contents;
    private String location;
    @JsonIgnore
    private List<MultipartFile> pics;
}
