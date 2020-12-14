package com.ruoyi.im.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.PageData;
import com.ruoyi.im.domain.im.GroupChat;
import org.springframework.web.multipart.MultipartFile;

public interface IMGroupChatService extends IService<GroupChat> {
    JSONObject create(PageData pageData, MultipartFile image);
}
