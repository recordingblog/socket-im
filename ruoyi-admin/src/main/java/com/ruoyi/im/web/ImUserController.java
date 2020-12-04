package com.ruoyi.im.web;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("im/user")
@Api(tags = "即时通讯-好友接口")
public class ImUserController extends BaseController {

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private ISysDeptService iSysDeptService;

    /**
     * 获取通讯录列表,按部门进行分组
     * @return
     */
    @GetMapping("list")
    @ApiOperation("获取通讯录列表")
    public AjaxResult getList(){
        try {
            return new AjaxResult(HttpStatus.SUCCESS,"success");
        }catch (Exception e){
            return  new AjaxResult(HttpStatus.ERROR,"系统异常,请联系在线管理员");
        }
    }

}
