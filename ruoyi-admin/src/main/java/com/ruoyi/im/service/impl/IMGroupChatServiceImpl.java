package com.ruoyi.im.service.impl;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
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
        return groupChat;
    }
}
