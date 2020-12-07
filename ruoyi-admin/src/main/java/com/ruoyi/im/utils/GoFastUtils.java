package com.ruoyi.im.utils;

import cn.hutool.core.io.resource.InputStreamResource;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.exception.BaseException;
import com.ruoyi.im.domain.fast.GoFastPathEnum;
import com.ruoyi.im.domain.fast.GoFastResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * go-fastdfs 文件上传工具类
 */
@Component
@Slf4j
public class GoFastUtils {

    @Value("${go-fast.server.ip}")
    private String fastUrl;

    public GoFastResult upload(MultipartFile multipartFile, String path) {
        InputStreamResource inputStreamResource;
        try {
            inputStreamResource = new InputStreamResource(multipartFile.getInputStream(), creatFileName(multipartFile.getOriginalFilename()));
        } catch (IOException e) {
            throw new BaseException("获取文件输入流异常");
        }
        return uploadToGofastdfs(getParamMap(inputStreamResource,path));
    }

    private GoFastResult uploadToGofastdfs(Map<String, Object> paramMap) {
        String uploadUrl = fastUrl + GoFastPathEnum.GO_FAST_UPLOAD.getValue();
        String resultMsg = HttpUtil.post(uploadUrl, paramMap);
        log.info("文件上传返回数据：{}", resultMsg);
        GoFastResult goFastdfsResult = JSONObject.parseObject(resultMsg, GoFastResult.class);
        if (StringUtils.isEmpty(goFastdfsResult.getMd5())) {
            throw new BaseException("文件上传失败");
        }
        return goFastdfsResult;
    }

    // 获取上传参数
    private Map<String, Object> getParamMap(InputStreamResource inputStreamResource,String path){
        HashMap<String, Object> paramMap = new HashMap<>();
        //文件
        paramMap.put("file", inputStreamResource);
        //路径
        paramMap.put("path", path);
        //输出格式
        paramMap.put("output", "json");
        return paramMap;
    }


    public synchronized static String creatFileName(String suffix) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(System.currentTimeMillis());
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(suffix)) {
            stringBuffer.append(suffix);
        }
        return stringBuffer.toString();
    }

}
