package com.green.greengram4.dm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class DmUserInsDto {
    private int idm;
    @JsonIgnore
    private int seq;
    private int loginedIuser;
    private String msg;
}
