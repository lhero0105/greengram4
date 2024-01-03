package com.green.greengram4.dm;

import com.green.greengram4.dm.model.*;
import com.green.greengram4.user.model.UserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DmMapper {
    int insDm(DmUserInsDto dto);
    List<DmSelVo> selDmAll(DmSelDto dto);
    Integer selDmByIns(DmInsDto dto);
    int postDm(DmInsDto dto);
    int updDmLastMsg(DmMsgInsDto dto);
    int updDmLastMsgAfterDelByLastMsg(DmMsgDelDto dto);
    UserEntity selOtherPersonByLoginUser(DmMsgInsDto dto);
    // -------------t_dm_user
    int insDmUser(DmUserInsDto dto);
    // --------------t_dm_msg
    int insDmMsg(DmMsgInsDto dto);
    List<DmMsgSelVo> selDmMsgAll(DmMsgSelDto dto);
    int delDmMsg(DmMsgDelDto dto);
}
