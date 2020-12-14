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
}
