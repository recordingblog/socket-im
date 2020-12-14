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
            @ApiImplicitParam(name = "queryType", value = "搜索类型", required = true),
            @ApiImplicitParam(name = "value", value = "群号或者群名称", required = true),
            @ApiImplicitParam(name = "userId", value = "搜索人id", required = true),
    })
    public AjaxResult queryGroup(){
        try {
            PageData param = this.getPageData();
            if (!param.getString("queryType").equals("1")&&!param.getString("queryType").equals("2")){
                return new AjaxResult(HttpStatus.ERROR,"搜索类型不符");
            }
            JSONArray result = imGroupChatService.queryGroup(param);
            return new AjaxResult(HttpStatus.SUCCESS,"success",result);
        }catch (Exception e){
            return new AjaxResult(HttpStatus.ERROR,"系统异常",e.toString());
        }
    }
}
