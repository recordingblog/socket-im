package com.ruoyi.im.domain.im;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 我的好友
 */
@TableName("im_friends_apply")
@Data
public class ApplyFriends {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * 分组id
     */
    @ApiModelProperty(value = "分组id")
    private Long groupId;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long userId;
    /**
     * 好友id
     */
    @ApiModelProperty(value = "好友id")
    private Long friendId;
    /**
     * 申请时间
     */
    @ApiModelProperty(value = "申请时间")
    private String applyTime;
    /**
     * 添加或者拒绝时间
     */
    @ApiModelProperty(value = "添加或者拒绝时间")
    private String resultTime;
    /**
     * 添加结果
     */
    @ApiModelProperty(value = "添加结果")
    private Integer applyStatus;
}
