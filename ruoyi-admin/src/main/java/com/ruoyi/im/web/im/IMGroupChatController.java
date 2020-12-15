package com.ruoyi.im.web.im;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.PageData;
import com.ruoyi.im.service.IMGroupChatService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("im/group-chat")
@Api(tags = "即时通讯-群管理")
public class IMGroupChatController extends BaseController {

    @Autowired
    private IMGroupChatService imGroupChatService;

    @PostMapping("create")
    @ApiOperation("创建群")
    @ApiOperationSupport(includeParameters = {"groupName","createUser"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupName", value = "群名称", required = true),
            @ApiImplicitParam(name = "createUser", value = "创建人id", required = true,dataType = "Int"),
            @ApiImplicitParam(name = "remark", value = "群描述"),
    })
    public AjaxResult createGroup(@ApiParam(value = "群头像",required = true) MultipartFile image){
        try {
            JSONObject result = imGroupChatService.create(super.getPageData(),image);
            if (check(result)){
                return new AjaxResult(HttpStatus.SUCCESS,"已创建",result.getString("data"));
            }else {
                return new AjaxResult(HttpStatus.ERROR,"创建失败,"+result.getString("msg"));
            }
        }catch (Exception e){
            return new AjaxResult(HttpStatus.ERROR,"系统异常",e.toString());
        }
    }

    @GetMapping("queryGroup")
    @ApiOperation("搜索群")
    @ApiOperationSupport(includeParameters = {"queryType","value"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "queryType", value = "搜索类型 1通过群号搜索 2通过昵称搜索", required = true),
            @ApiImplicitParam(name = "value", value = "群号或者群名称", required = true),
            @ApiImplicitParam(name = "userId", value = "搜索人id", required = true),
    })
    public AjaxResult queryGroup(){
        try {
            PageData param = this.getPageData();
            return !param.getString("queryType").equals("1")&&!param.getString("queryType").equals("2")
                    ? new AjaxResult(HttpStatus.ERROR,"搜索类型不符")
                    : new AjaxResult(HttpStatus.SUCCESS,"success",imGroupChatService.queryGroup(param));
        }catch (Exception e){
            return new AjaxResult(HttpStatus.ERROR,"系统异常",e.toString());
        }
    }


    @PostMapping("addGroup")
    @ApiOperation("加入群")
    @ApiOperationSupport(includeParameters = {"groupId","userId"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "群号码", required = true),
            @ApiImplicitParam(name = "userId", value = "用户id", required = true),
    })
    public AjaxResult addGroup(){
        try {
            JSONObject result = imGroupChatService.addGroup(this.getPageData());
            return check(result)
                    ? new AjaxResult(HttpStatus.SUCCESS,result.getString("msg"),result.getString("data"))
                    : new AjaxResult(HttpStatus.ERROR,"加入失败,"+result.getString("msg"));
        }catch (Exception e){
            return new AjaxResult(HttpStatus.ERROR,"系统异常",e.toString());
        }
    }

    @PostMapping("quitGroup")
    @ApiOperation("退出群")
    @ApiOperationSupport(includeParameters = {"groupId","userId"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "群号码", required = true),
            @ApiImplicitParam(name = "userId", value = "用户id", required = true),
    })
    public AjaxResult quitGroup(){
        try {
            JSONObject result = imGroupChatService.quitGroup(this.getPageData());
            if (check(result)){
                return new AjaxResult(HttpStatus.SUCCESS,result.getString("msg"),result.getString("data"));
            }else {
                return new AjaxResult(HttpStatus.ERROR,result.getString("msg"));
            }
        }catch (Exception e){
            return new AjaxResult(HttpStatus.ERROR,"系统异常",e.toString());
        }
    }

    @PostMapping("dissolutionGroup")
    @ApiOperation("解散群|恢复解散")
    @ApiOperationSupport(includeParameters = {"groupId","createUser","status"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "群号码", required = true),
            @ApiImplicitParam(name = "createUser", value = "创建用户id(群主id)", required = true),
            @ApiImplicitParam(name = "status", value = "群状态 0恢复 1解散", required = true),
    })
    public AjaxResult dissolutionGroup(){
        try {
            JSONObject result = imGroupChatService.dissolutionGroup(this.getPageData());
            if (check(result)){
                return new AjaxResult(HttpStatus.SUCCESS,result.getString("msg"),result.getString("data"));
            }else {
                return new AjaxResult(HttpStatus.ERROR,result.getString("msg"));
            }
        }catch (Exception e){
            return new AjaxResult(HttpStatus.ERROR,"系统异常",e.toString());
        }
    }

    @GetMapping("myGroupList")
    @ApiOperationSupport(includeParameters = {"userId"})
    @ApiOperation("我的群列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true),
    })
    public AjaxResult myGroupList(){
        try {
            JSONArray result = imGroupChatService.myGroupList(this.getPageData());
            return new AjaxResult(HttpStatus.SUCCESS,"success",result);
        }catch (Exception e){
            return new AjaxResult(HttpStatus.ERROR,"系统异常",e.toString());
        }
    }

    @GetMapping("groupUser")
    @ApiOperationSupport(includeParameters = {"groupId"})
    @ApiOperation("查询群用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "群id", required = true),
    })
    public AjaxResult groupUser(){
        try {
            return new AjaxResult(HttpStatus.SUCCESS,"success", imGroupChatService.groupUser(this.getPageData()));
        }catch (Exception e){
            return new AjaxResult(HttpStatus.ERROR,"系统异常",e.toString());
        }
    }


    @PostMapping("update")
    @ApiOperation("群昵称|群描述|加群方式修改")
    @ApiOperationSupport(includeParameters = {"groupId","createUser","type","value"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "群号码", required = true),
            @ApiImplicitParam(name = "createUser", value = "创建用户id(群主id)", required = true),
            @ApiImplicitParam(name = "type", value = "0群昵称修改 1群描述修改 2加群方式修改(0审核加入 1自动加入)", required = true),
            @ApiImplicitParam(name = "value", value = "加群方式 0审核加入 1自动加入", required = true),
    })
    public AjaxResult update(){
        try {
            String type = this.getPageData().getString("type");
            if (type.equals("0") || type.equals("1") || type.equals("2")){
                JSONObject result = imGroupChatService.updateByType(type,this.getPageData());
                return check(imGroupChatService.updateByType(type,this.getPageData()))
                        ? new AjaxResult(HttpStatus.SUCCESS,result.getString("msg"),result.getString("data"))
                        : new AjaxResult(HttpStatus.ERROR,result.getString("msg"));
            }else {
                return new AjaxResult(HttpStatus.ERROR,"参数类型不符");
            }
        }catch (Exception e){
            return new AjaxResult(HttpStatus.ERROR,"系统异常",e.toString());
        }
    }
}
