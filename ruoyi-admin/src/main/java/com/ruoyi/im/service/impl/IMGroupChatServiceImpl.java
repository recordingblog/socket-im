package com.ruoyi.im.service.impl;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.PageData;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.im.domain.fast.GoFastResult;
import com.ruoyi.im.domain.im.GroupChat;
import com.ruoyi.im.domain.im.GroupPerson;
import com.ruoyi.im.mapper.IMGroupChatMapper;
import com.ruoyi.im.service.IMGroupChatService;
import com.ruoyi.im.service.IMGroupPersonService;
import com.ruoyi.im.type.IMPersonEnum;
import com.ruoyi.im.utils.GoFastUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IMGroupChatServiceImpl extends ServiceImpl<IMGroupChatMapper,GroupChat> implements IMGroupChatService {

    @Autowired
    private IMGroupPersonService imGroupPersonService;

    @Autowired
    private GoFastUtils goFastUtils;


    /**
     * 创建群
     * @param param
     * @return
     */
    @Override
    public JSONObject create(PageData param, MultipartFile image) {
        JSONObject result = new JSONObject();
        Integer code = HttpStatus.SUCCESS;
        String msg = "success";
        String groupId = "";
        try {
            GroupChat groupChat = getCreateGroupParam(param,goFastUtils.upload(image, "im"));
            save(groupChat);
            imGroupPersonService.save(getGroupPersonPasram(groupChat,param));
            groupId+=groupChat.getGroupId();
        }catch (Exception e){
            code = HttpStatus.ERROR;
            msg = e.toString();
        }
        result.put("code",code);
        result.put("msg",msg);
        result.put("data",groupId);
        return result;
    }

    /**
     * 搜索群
     * @param param
     * @return
     */
    @Override
    public JSONArray queryGroup(PageData param) {
        // 获取搜索到的群信息
        JSONArray finalResult = new JSONArray();
        List<GroupChat> result = new ArrayList<>();
        result.addAll(
                param.getString("queryType").equals("1")
                ?
                list(getQueryEntity("group_id",param)) : list(getQueryEntity("group_name",param))
        );
        // 迭代群信息 通过群号和搜索人 判断是否搜索人是否已加入群聊
        result.stream().forEach(e->{
            List<GroupPerson> all = imGroupPersonService.list(
                    new QueryWrapper<GroupPerson>().eq("group_id", e.getGroupId())
            );
            finalResult.add(getResultInfo(all,param,e));
        });
        return finalResult;
    }

    public JSONObject getResultInfo(List<GroupPerson> all,PageData param,GroupChat e){
        JSONObject info = JSONObject.parseObject(JSON.toJSONString(e));
        info.put("status",all.stream().filter(j -> j.getPersonId().equals(param.getString("userId"))).collect(Collectors.toList()).size()>0?1:0);
        info.put("addNum",all.size());
        return info;
    }
    // 构造查询条件
    public QueryWrapper<GroupChat> getQueryEntity(String column,PageData param){
        QueryWrapper<GroupChat> eq = new QueryWrapper<GroupChat>().eq(column,param.getString("value"));
        return eq;
    }

    public GroupPerson getGroupPersonPasram(GroupChat groupChat,PageData param){
        GroupPerson groupPerson = new GroupPerson();
        groupPerson.setGroupId(groupChat.getGroupId());
        groupPerson.setPersonId(param.getString("createUser"));
        groupPerson.setPersonType(IMPersonEnum.TYPE_LEADER.getCode());
        groupPerson.setTime(DateUtils.getTime());
        return groupPerson;
    }

    public GroupChat getCreateGroupParam(PageData param,GoFastResult images){
        GroupChat groupChat = new GroupChat();
        // 群名称
        groupChat.setGroupName(param.getString("groupName"));
        // 群头像
        groupChat.setGroupSrc(images.getUrl());
        String groupId = RandomUtil.randomNumbers(8);
        groupChat.setGroupId(groupId);
        // 创建人
        groupChat.setCreateUser(Integer.parseInt(param.getString("createUser")));
        // 创建时间
        groupChat.setCreateTime(DateUtils.getTime());
        groupChat.setType(0);
        String remark = param.getString("remark");
        if (!StrUtil.isEmpty(remark)){
            groupChat.setRemark(remark);
        }
        return groupChat;
    }
}
