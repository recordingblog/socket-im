package com.ruoyi.im.domain.fast;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GoFastPathEnum {
    GO_FAST_UPLOAD("/group1/upload", "go-fastdfs文件上传路径"),
    GO_FAST_DELETE("/group1/delete", "go-fastdfs文件删除路径"),
    UPLOAD_AVATAR_PATH("avatar", "头像保存路径"),
    ;
    private final String value;
    private final String msg;
}
