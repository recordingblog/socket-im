package com.ruoyi.im.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.im.domain.im.ApplyFriends;
import com.ruoyi.im.service.IMFriendService;
import com.ruoyi.im.service.ImUserService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class IMUserServiceImpl implements ImUserService {

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private ISysDeptService iSysDeptService;

    @Autowired
    private IMFriendService imFriendService;

    // 获取通讯录列表
    @Override
    public JSONObject getList(Long userId) {
        JSONObject finalResult = new JSONObject();
        Integer code = HttpStatus.SUCCESS;
        JSONArray result = new JSONArray();
        try {
            // 构造查询条件
            QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
            result.addAll(getResult(userId,queryWrapper.select("nick_name", "user_id", "dept_id","online_status")));
        }catch (Exception e){
            code = HttpStatus.ERROR;
        }
        finalResult.put("code",code);
        finalResult.put("result",result);
        return finalResult;
    }

    // 获取结果集
    public JSONArray getResult(Long userId,QueryWrapper<SysUser> queryWrapper){
        JSONArray result = new JSONArray();
        List<SysUser> list = iSysUserService.list(queryWrapper);
        // 按部门对用户进行分组
        Map<Long, List<SysUser>> collect = list.stream().collect(Collectors.groupingBy(d->d.getDeptId()));
        collect.forEach((k,v)->{
            SysDept dept = iSysDeptService.selectDeptById(k);
            JSONObject info = new JSONObject();
            info.put("deptId",dept.getDeptId());
            info.put("deptName",dept.getDeptName());
            info.put("deptUser",getDeptUser(userId,v));
            result.add(info);
        });
        return result;
    }

    // 获取部门下的用户信息
    public JSONArray getDeptUser(Long userId,List<SysUser> v){
        JSONArray deptUser = new JSONArray();
        v.stream().forEach(e->{
            JSONObject user = new JSONObject();
            user.put("userId",e.getUserId());
            user.put("nickName",e.getNickName());
            user.put("onlineStatus",e.getOnlineStatus());
            QueryWrapper<ApplyFriends> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id",userId);
            queryWrapper.eq("friend_id",e.getUserId());
            ApplyFriends one = imFriendService.getOne(queryWrapper);
            user.put("addStatus",ObjectUtil.isNull(one) || one.getApplyStatus()==0?0:one.getApplyStatus());
            deptUser.add(user);
        });
        return deptUser;
    }
}
