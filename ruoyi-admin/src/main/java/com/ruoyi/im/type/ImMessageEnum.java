package com.ruoyi.im.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImMessageEnum {

    SEND_TYPE_ONE(1, "单发"),
    SEND_TYPE_ALL(2, "群发"),
    ;

    private Integer code;
    private String msg;

    public static ImMessageEnum getCode(Integer code){
        for(ImMessageEnum enums: ImMessageEnum.values()){
            if(enums.code.equals(code)){
                return enums;
            }
        }
        return null;
    }
}
