package com.ruoyi.im.web;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.im.service.ImUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("im/communication")
@Api(tags = "即时通讯-通讯录接口")
public class IMUserController extends BaseController {

    @Autowired
    private ImUserService imUserService;

    /**
     * 获取通讯录列表,按部门进行分组
     * @return
     */
    @GetMapping("list")
    @ApiOperation("获取通讯录列表")
    public AjaxResult getList(){
        try {
            JSONObject result = imUserService.getList();
            return super.check(result) ? new AjaxResult(HttpStatus.SUCCESS,"success",result.getJSONArray("result"))
                    : new AjaxResult(HttpStatus.ERROR,"DB数据异常");
        }catch (Exception e){
            return new AjaxResult(HttpStatus.ERROR,"系统异常,请联系在线管理员",e);
        }
    }
}
