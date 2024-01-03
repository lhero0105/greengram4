package com.green.greengram4.dm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.green.greengram4.common.ResVo;
import com.green.greengram4.dm.model.*;
import com.green.greengram4.user.UserMapper;
import com.green.greengram4.user.model.UserEntity;
import com.green.greengram4.user.model.UserSelDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class Dmservice {
    private final DmMapper mapper;
    private final UserMapper userMapper;
    private final ObjectMapper objMapper;

    public List<DmSelVo> getDmAll(DmSelDto dto){
        return mapper.selDmAll(dto);
    }

    public DmSelVo postDm(DmInsDto dto){
        Integer idm = mapper.selDmByIns(dto);
        if(idm != null){
            return null;
        }
        DmUserInsDto dmUserInsDto = new DmUserInsDto();
        int affectedRow1 = mapper.insDm(dmUserInsDto); // 방생성 후 idm가져옴
        dto.setIdm(dmUserInsDto.getIdm());
        log.info("dto : {}", dto);
        int affectedRow2 = mapper.postDm(dto);

        UserSelDto usDto = new UserSelDto();
        usDto.setIuser(dto.getOtherPersonIuser());
        UserEntity userEntity = userMapper.selUser(usDto);

        return DmSelVo.builder()
                .idm(dto.getIdm())
                .otherPersonIuser(userEntity.getIuser())
                .otherPersonNm(userEntity.getNm())
                .otherPersonPic(userEntity.getPic()).build();
    }

    public ResVo postDmMsg(DmMsgInsDto dto) {
        int affectedRows = mapper.insDmMsg(dto);
        if(affectedRows == 1) {
            int updAffectedRows = mapper.updDmLastMsg(dto);
        }
        LocalDateTime now = LocalDateTime.now(); // 현재 날짜 구하기
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // 포맷 정의
        String createdAt = now.format(formatter); // 포맷 적용

        //상대방의 firebaseToken값 필요. 나의 pic, iuser값 필요.
        UserEntity otherPerson = mapper.selOtherPersonByLoginUser(dto);
        try {

            if(otherPerson.getFirebaseToken() != null) {
                DmMsgPushVo pushVo = new DmMsgPushVo();
                pushVo.setIdm(dto.getIdm());
                pushVo.setSeq(dto.getSeq());
                pushVo.setWriterIuser(dto.getLoginedIuser());
                pushVo.setWriterPic(dto.getLoginedPic());
                pushVo.setMsg(dto.getMsg());
                pushVo.setCreatedAt(createdAt);

                //object to json
                // 문자열 하나로 제이슨을 바꿉니다.
                String body = objMapper.writeValueAsString(pushVo);
                log.info("pushVo: {}", pushVo);
                log.info("body: {}", body);

                Notification noti = Notification.builder()
                        .setTitle("dm") // dm적어야 프론트에서 분기합니다.
                        .setBody(body)
                        .build();

                Message message = Message.builder()
                        .setToken(otherPerson.getFirebaseToken())
                        .setNotification(noti)
                        .build();

                FirebaseMessaging.getInstance().sendAsync(message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResVo(dto.getSeq());
    }

    public List<DmMsgSelVo> getMsgAll(DmMsgSelDto dto){
        List<DmMsgSelVo> list =  mapper.selDmMsgAll(dto);
        log.info("list : {}", list);
        Collections.reverse(list);
        log.info("list : {}", list);
        return list;
    }

    public ResVo delDmMsg(DmMsgDelDto dto){
        int affectedRows = mapper.delDmMsg(dto);
        log.info("affectedRows : {}", affectedRows);
        return new ResVo(affectedRows);
    }
}
