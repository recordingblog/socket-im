package com.ruoyi.im.web.im;
import com.alibaba.fastjson.JSONObject;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.im.service.IMGroupChatService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
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
}
