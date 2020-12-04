package com.ruoyi.im.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.im.service.ImUserService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ImUserServiceImpl implements ImUserService {

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private ISysDeptService iSysDeptService;


    @Override
    public JSONObject getList() {
        JSONObject finalResult = new JSONObject();
        Integer code = HttpStatus.SUCCESS;
        JSONArray result = new JSONArray();
        try {
            QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
            result.addAll(getResult(queryWrapper.select("nick_name", "user_id", "dept_id","online_status")));
        }catch (Exception e){
            code = HttpStatus.ERROR;
        }
        finalResult.put("code",code);
        finalResult.put("result",result);
        return finalResult;
    }

    // 获取通讯录部门和用户信息
    public JSONArray getResult(QueryWrapper<SysUser> queryWrapper){
        JSONArray result = new JSONArray();
        List<SysUser> list = iSysUserService.list(queryWrapper);
        Map<Long, List<SysUser>> collect = list.stream().collect(Collectors.groupingBy(d->d.getDeptId()));
        collect.forEach((k,v)->{
            SysDept dept = iSysDeptService.selectDeptById(k);
            JSONObject info = new JSONObject();
            info.put("deptId",dept.getDeptId());
            info.put("deptName",dept.getDeptName());
            info.put("deptUser",getDeptUser(v));
            result.add(info);
        });
        return result;
    }

    // 获取部门下所有通讯录用户
    public JSONArray getDeptUser(List<SysUser> v){
        JSONArray deptUser = new JSONArray();
        v.stream().forEach(e->{
            JSONObject user = new JSONObject();
            user.put("userId",e.getUserId());
            user.put("nickName",e.getNickName());
            user.put("onlineStatus",e.getOnlineStatus());
            deptUser.add(user);
        });
        return deptUser;
    }
}
