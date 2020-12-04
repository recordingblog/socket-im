package com.ruoyi.im.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.im.domain.Group;
import com.ruoyi.im.service.GroupService;
import com.ruoyi.mapper.MyGroupMapper;
import org.springframework.stereotype.Service;

@Service
public class GroupServiceImpl extends ServiceImpl<MyGroupMapper, Group> implements GroupService {
}
