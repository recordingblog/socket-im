package com.ruoyi.im.web;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.im.domain.Group;
import com.ruoyi.im.service.GroupService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("im/group")
@Api(tags = "即时通讯-好友分组接口")
public class MyGroupController extends BaseController {

    @Autowired
    private GroupService groupService;

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
    public AjaxResult del(@ApiParam("分组id") Long id){
        try {
            return new AjaxResult(HttpStatus.SUCCESS,"success",groupService.removeById(id));
        }catch (Exception e){
            return new AjaxResult(HttpStatus.ERROR,"系统异常");
        }
    }
}
