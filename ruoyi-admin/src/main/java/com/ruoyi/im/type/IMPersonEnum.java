package com.ruoyi.im.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IMPersonEnum {

    TYPE_ORDINARY(0, "成员"),
    TYPE_MANAGER(1, "管理员"),
    TYPE_LEADER(2,"群主"),
    ;

    private Integer code;
    private String msg;

    public static IMPersonEnum getCode(Integer code){
        for(IMPersonEnum enums: IMPersonEnum.values()){
            if(enums.code.equals(code)){
                return enums;
            }
        }
        return null;
    }
}
