package com.ruoyi.im.domain.im;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
@Data
@TableName("im_group")
public class GroupChat {
    /**
     * 群id
     */
    private String groupId;
    /**
     * 群名称
     */
    private String groupName;
    /**
     * 群头像路径
     */
    private String groupSrc;
    /**
     * 创建人id
     */
    private Integer createUser;

    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 群描述
     */
    private String remark;

    /**
     * 加群类型 1自动加入 0审核加入（默认方式）
     */
    private Integer type;

    /**
     * 允许加入群聊最大人数
     */
    private Integer maxPerson;

    /**
     * 群状态 0待审核 1已加入 2拒绝加入
     */
    private Integer status;
}
