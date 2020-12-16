package com.ruoyi.im.service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.PageData;
import com.ruoyi.im.domain.im.GroupChat;
import org.springframework.web.multipart.MultipartFile;

public interface IMGroupChatService extends IService<GroupChat> {
    // 创建群
    JSONObject create(PageData pageData, MultipartFile image);
    // 搜索群
    JSONArray queryGroup(PageData pageData);
    // 加入群
    JSONObject addGroup(PageData pageData);
    // 我的群列表
    JSONArray myGroupList(PageData pageData);
    // 查询群用户
    JSONObject groupUser(PageData pageData);
    // 退出群
    JSONObject quitGroup(PageData pageData);
    // 解散群
    JSONObject dissolutionGroup(PageData pageData);
    // 群昵称|群描述|加群方式修改
    JSONObject updateByType(String type, PageData pageData);
    // 群管理员设置
    JSONObject groupManager(PageData pageData);
}
