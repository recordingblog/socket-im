package com.ruoyi.im.web;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.PageData;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.im.domain.ApplyFriends;
import com.ruoyi.im.service.IMFriendService;
import com.ruoyi.im.service.IMGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("im/friends")
@Api(tags = "即时通讯-好友接口")
public class IMFriendsController {

    @Autowired
    private IMFriendService imFriendService;

    @Autowired
    private IMGroupService groupService;

    @ApiOperation("添加好友")
    @PostMapping("addFriend")
    public AjaxResult addFriend(@ApiParam(value = "分组id",required = true) Long groupId, @ApiParam(value = "用户id",required = true)Long userId, @ApiParam(value = "好友id",required = true)Long friendId){
        try {
            PageData group = groupService.check(groupId,userId);
            if (ObjectUtil.isNotNull(group)){
                ApplyFriends applyFriends = new ApplyFriends();
                applyFriends.setGroupId(groupId);
                applyFriends.setUserId(userId);
                applyFriends.setFriendId(friendId);
                applyFriends.setApplyTime(DateUtils.getTime());
                imFriendService.save(applyFriends);
                return new AjaxResult(HttpStatus.SUCCESS,"已提交申请,请等待对方同意",group);
            }else {
                return new AjaxResult(HttpStatus.ERROR,"不存在分组");
            }
        }catch (Exception e){
            return new AjaxResult(HttpStatus.ERROR,"系统异常",e.toString());
        }
    }

    @PostMapping("applyList")
    @ApiOperation("申请添加列表")
    public AjaxResult applyList(@ApiParam(value = "用户id",required = true) Long userId,@ApiParam(value = "申请结果",required = true) Integer applySatus){
        try {
            QueryWrapper<ApplyFriends> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("friend_id",userId);
            if (applySatus!=null){
                queryWrapper.eq("apply_status",applySatus);
            }
            List<ApplyFriends> list =
                    imFriendService.list(queryWrapper);
            return new AjaxResult(HttpStatus.SUCCESS,"success",list);
        }catch (Exception e){
            return new AjaxResult(HttpStatus.ERROR,"系统异常",e.toString());
        }
    }

    @PostMapping("applyHandle")
    @ApiOperation("同意或者拒绝添加")
    public AjaxResult applyHandle(
            @ApiParam(value = "添加消息id",required = true) Long id,
            @ApiParam(value = "结果 1通过 2未通过",required = true) Integer applyStatus,
            @ApiParam(value = "分组id",required = true) Long groupId) {
        try {
            Boolean flag = true;
            if (applyStatus != 1 && applyStatus != 2) {
                return new AjaxResult(HttpStatus.ERROR, "结果不符,1通过 2未通过");
            }
            ApplyFriends byId = imFriendService.getById(id);
            if (ObjectUtil.isNull(byId)) {
                return new AjaxResult(HttpStatus.ERROR, "消息id为" + id + "的申请信息不存在");
            }
            ApplyFriends applyFriends = new ApplyFriends();
            applyFriends.setId(id);
            applyFriends.setApplyStatus(applyStatus);
            applyFriends.setResultTime(DateUtils.getTime());
            boolean b = imFriendService.updateById(applyFriends);
            if (b && applyStatus == 1) {
                ApplyFriends base = imFriendService.getById(id);
                Long userId = base.getUserId();
                Long friendId = base.getFriendId();
                base.setFriendId(userId);
                base.setUserId(friendId);
                base.setGroupId(groupId);
                flag = imFriendService.save(base);
            }
            return new AjaxResult(HttpStatus.SUCCESS, "success",flag);
        } catch (Exception e) {
            return new AjaxResult(HttpStatus.ERROR, "系统异常", e.toString());
        }
    }
}
