package com.ruoyi.im.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.PageData;
import com.ruoyi.im.domain.im.Group;
import com.ruoyi.im.service.IMGroupService;
import com.ruoyi.im.mapper.IMGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IMGroupServiceImpl extends ServiceImpl<IMGroupMapper, Group> implements IMGroupService {

    @Autowired
    private IMGroupMapper imGroupMapper;
    @Override
    public PageData check(Long groupId, Long userId) {
        PageData param = new PageData();
        param.put("groupId",groupId);
        param.put("userId",userId);
        return imGroupMapper.check(param);
    }
}
