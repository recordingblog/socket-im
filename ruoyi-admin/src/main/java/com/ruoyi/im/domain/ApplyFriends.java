package com.ruoyi.im.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("im_friends_apply")
@Data
public class ApplyFriends {
    private Long id;
    private Long groupId;
    private Long userId;
    private Long friendId;
    private String applyTime;
    private String resultTime;
    private Integer applyStatus;
}
