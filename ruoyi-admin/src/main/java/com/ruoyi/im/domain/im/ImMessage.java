package com.ruoyi.im.domain.im;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 聊天消息
 */
@Data
public class ImMessage {
    /**
     * 发送类型 1单聊 2群聊
     */
    @ApiModelProperty(value = "发送类型")
    private Integer sendType;
    /**
     * 消息类型 1文本 2图片 3文件
     */
    @ApiModelProperty(value = "消息类型")
    private Integer msgType;
    /**
     * 发送人id
     */
    @ApiModelProperty(value = "发送人id")
    private String formId;
    /**
     * 接收人id
     */
    @ApiModelProperty(value = "接收人id")
    private String toId;
    /**
     * 发送时间
     */
    @ApiModelProperty(value = "发送时间")
    private Date sendTime;
    /**
     * 消息内容
     */
    @ApiModelProperty(value = "消息内容")
    private String content;
    /**
     * 文件路径(图片消息或者文件消息,type=2为预览,type=3为下载)
     */
    @ApiModelProperty(value = "文件路径")
    private String fileUrl;
}
