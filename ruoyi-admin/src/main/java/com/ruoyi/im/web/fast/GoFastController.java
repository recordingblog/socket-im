package com.ruoyi.im.web.fast;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.im.domain.fast.GoFastResult;
import com.ruoyi.im.utils.GoFastUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("go-fast")
@Api(tags = "即时通讯-文件上传接口")
public class GoFastController extends BaseController {
    @Autowired
    private GoFastUtils goFastUtils;

    @ApiOperation("GO-FAST上传")
    @PostMapping("upload")
    public AjaxResult upload(MultipartFile multipartFile){
        try {
            JSONObject finalResult = new JSONObject();
            GoFastResult result = goFastUtils.upload(multipartFile, "im");
            finalResult.put("url",result.getUrl());
            finalResult.put("md5",result.getMd5());
            finalResult.put("path",result.getPath());
            return new AjaxResult(HttpStatus.SUCCESS,"success",finalResult);
        }catch (Exception e){
            return new AjaxResult(HttpStatus.ERROR,"系统异常",e.toString());
        }
    }
}
