package com.ruoyi.im.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.common.core.domain.PageData;
import com.ruoyi.im.domain.im.Group;
public interface IMGroupMapper extends BaseMapper<Group> {
    PageData check(PageData pageData);
}
