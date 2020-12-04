package com.ruoyi.im.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("im_my_group")
public class Group implements Serializable {
    private Long id;
    private String groupName;
    private String createTime;
    private Long userId;
}
