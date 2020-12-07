package com.ruoyi.im.domain.fast;

import lombok.Data;

import java.math.BigInteger;

@Data
public class GoFastResult {
    /**
     * 全路径
     */
    private String url;
    /**
     * 文件MD5
     */
    private String md5;
    /**
     * 相对路径
     */
    private String path;
    /**
     * 服务器IP+端口
     */
    private String domain;
    /**
     * 场景
     */
    private String scene;
    /**
     * 文件大小
     */
    private BigInteger size;
    /**
     * 上传时间
     */
    private BigInteger mtime;
    /**
     * 场景列表
     */
    private String scenes;
    /**
     * 返回消息
     */
    private String retmsg;
    /**
     * 返回code码
     */
    private int retcode;
    /**
     * 文件存放路径
     */
    private String src;
}
