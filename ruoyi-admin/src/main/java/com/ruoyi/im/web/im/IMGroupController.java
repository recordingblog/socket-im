package com.ruoyi.im.web.im;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.im.domain.im.ApplyFriends;
import com.ruoyi.im.domain.im.Group;
import com.ruoyi.im.service.IMFriendService;
import com.ruoyi.im.service.IMGroupService;
import com.ruoyi.system.service.ISysUserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("im/group")
@Api(tags = "即时通讯-分组接口")
public class IMGroupController extends BaseController {

    @Autowired
    private IMGroupService groupService;

    @Autowired
    private IMFriendService imFriendService;

    @Autowired
    private ISysUserService iSysUserService;

    @PostMapping("addGroup")
    @ApiOperation("创建分组")
    @ApiOperationSupport(includeParameters = {"groupName","userId"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupName", value = "分组名称", required = true),
            @ApiImplicitParam(name = "userId", value = "隶属用户", required = true),
    })
    public AjaxResult addGroup(){
        try {
            Group group = new Group();
            group.setCreateTime(DateUtils.getTime());
            group.setUserId(Long.parseLong(super.getPageData().getString("userId")));
            group.setGroupName(super.getPageData().getString("groupName"));
            boolean save = groupService.save(group);
            return new AjaxResult(HttpStatus.SUCCESS,"success",save);
        }catch (Exception e){
            return new AjaxResult(HttpStatus.ERROR,"系统异常");
        }
    }

    @PostMapping("editGroup")
    @ApiOperation("重命名分组")
    public AjaxResult editGroup(@ApiParam(value = "分组id",required = true) Long id, @ApiParam(value = "分组名称",required = true) String groupName){
        try {
            Group group = new Group();
            group.setId(id);
            group.setGroupName(groupName);
            return new AjaxResult(HttpStatus.SUCCESS,"success",groupService.updateById(group));
        }catch (Exception e){
            return new AjaxResult(HttpStatus.ERROR,"系统异常");
        }
    }

    @DeleteMapping("del")
    @ApiOperation("删除分组")
    public AjaxResult del(@ApiParam(value = "分组id",required = true) Long id){
        try {
            return new AjaxResult(HttpStatus.SUCCESS,"success",groupService.removeById(id));
        }catch (Exception e){
            return new AjaxResult(HttpStatus.ERROR,"系统异常");
        }
    }

    @ApiOperation("查询我的分组")
    @PostMapping("groupList")
    public AjaxResult groupList(Long userId){
        try {
            QueryWrapper<Group> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id",userId);
            // 查询我的分组
            List<Group> list = groupService.list(queryWrapper);
            Group group = new Group();
            // 生成系统默认分组
            group.setId(Long.parseLong("0"));
            group.setGroupName("系统默认分组");
            group.setUserId(userId);
            // 添加默认分组
            list.add(group);
            // 结果集
            JSONArray result = new JSONArray();
            list.stream().forEach(e->{
                JSONObject info = JSONObject.parseObject(JSON.toJSONString(e));
                info.remove("userId");
                // 分组用户id
                Long uId = e.getUserId();
                // 分组id
                Long gUd = e.getId();
                QueryWrapper<ApplyFriends> eq = new QueryWrapper<ApplyFriends>().eq("user_id", uId).eq("group_id", gUd);
                List<ApplyFriends> friends = imFriendService.list(eq);
                JSONArray arrFriends = new JSONArray();
                friends.stream().forEach(friend->{
                    JSONObject object = JSONObject.parseObject(JSON.toJSONString(friend));
                    object.remove("userId");
                    object.remove("applyStatus");
                    object.remove("groupId");
                    object.remove("resultTime");
                    SysUser sysUser = iSysUserService.selectUserById(friend.getFriendId());
                    object.put("nickName",sysUser.getNickName());
                    arrFriends.add(object);
                });
                info.put("groupUser",arrFriends);
                result.add(info);
            });
            return new AjaxResult(HttpStatus.SUCCESS,"success",result);
        }catch (Exception e){
            return new AjaxResult(HttpStatus.ERROR,"系统异常",e.toString());
        }
    }
}
