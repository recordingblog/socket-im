package com.ruoyi.common.exception.im;

import com.ruoyi.common.constant.HttpStatus;
import lombok.Data;

@Data
public class ImException extends RuntimeException {
    private Integer code;
    private String message;
    public ImException(String message) {
        this.code = HttpStatus.ERROR;
        this.message = message;
    }
}
