package com.ruoyi.im.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.PageData;
import com.ruoyi.im.domain.Group;

public interface IMGroupService extends IService<Group> {
    // 分组检查
    PageData check(Long group_id, Long user_id);
}
