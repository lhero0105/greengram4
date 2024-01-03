package com.green.greengram4.dm;

import com.green.greengram4.common.ResVo;
import com.green.greengram4.dm.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dm")
public class DmController {
    private final Dmservice service;

    // 무조건 방이 있는 것 만 요청이 들어옵니다. (idm을 받음)
    @GetMapping
    public List<DmSelVo> getDmAll(DmSelDto dto){
        return service.getDmAll(dto);
    }

    // 홈 화면 클릭 시 DM 방 생성
    @PostMapping
    public DmSelVo postDm(@RequestBody DmInsDto dto){
        return service.postDm(dto);
    }

    //----------------------------- t_dm_msg
    // 메세지 등록
    @PostMapping("/msg")
    public ResVo postDmMsg(@RequestBody DmMsgInsDto dto) {
        log.info("dto : {}", dto);
        return service.postDmMsg(dto);
    }

    // 방 안의 메세지 셀렉
    @GetMapping("/msg")
    public List<DmMsgSelVo> getMsgAll(DmMsgSelDto dto){
        log.info("dto : {}", dto);
        return service.getMsgAll(dto);
    }
    // page 2 가 넘어오면 방이 있단 소리니까 방이 있는지에 관한 분기문을 안씁니다

    // 메세지 삭제
    @DeleteMapping("/msg")
    public ResVo delDmMsg(DmMsgDelDto dto){
        return service.delDmMsg(dto);
    }
}
