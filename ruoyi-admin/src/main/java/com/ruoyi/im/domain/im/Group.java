package com.ruoyi.im.domain.im;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

/**
 * 我的好友分组
 */
@Data
@TableName("im_my_group")
public class Group implements Serializable {
    /**
     * 分组id
     */
    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * 分组名称
     */
    @ApiModelProperty(value = "分组名称")
    private String groupName;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private String createTime;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long userId;
}
