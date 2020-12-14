package com.ruoyi.im.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.im.domain.im.GroupPerson;
import com.ruoyi.im.mapper.IMGroupPersonMapper;
import com.ruoyi.im.service.IMGroupPersonService;
import org.springframework.stereotype.Service;

@Service
public class IMGroupPersonServiceImpl extends ServiceImpl<IMGroupPersonMapper, GroupPerson> implements IMGroupPersonService {}
