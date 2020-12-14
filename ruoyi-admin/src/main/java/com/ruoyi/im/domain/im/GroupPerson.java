package com.ruoyi.im.domain.im;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
@Data
@TableName("im_group_person")
public class GroupPerson {
    /**
     * 群id
     */
    private String groupId;
    /**
     * 人员id
     */
    private String personId;
    /**
     * 人员类型
     */
    private Integer personType;
    /**
     * 入群时间
     */
    private String time;
    /**
     * 入群状态 0待审核 1已加入
     */
    private Integer status;
}
