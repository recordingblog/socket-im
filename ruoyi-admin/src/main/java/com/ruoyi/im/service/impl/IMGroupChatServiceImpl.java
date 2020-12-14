package com.ruoyi.im.service.impl;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.PageData;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.im.domain.fast.GoFastResult;
import com.ruoyi.im.domain.im.GroupChat;
import com.ruoyi.im.domain.im.GroupPerson;
import com.ruoyi.im.mapper.IMGroupChatMapper;
import com.ruoyi.im.service.IMGroupChatService;
import com.ruoyi.im.service.IMGroupPersonService;
import com.ruoyi.im.type.IMPersonEnum;
import com.ruoyi.im.utils.GoFastUtils;
import com.ruoyi.system.service.ISysUserService;
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

    @Autowired
    private ISysUserService userService;


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

    /**
     * 加入群
     * @param param
     * @return
     */
    @Override
    public JSONObject addGroup(PageData param) {
        JSONObject result = new JSONObject();
        Integer code = HttpStatus.SUCCESS;
        String msg = "success";
        boolean flag = true;
        try {
            SysUser user = userService.selectUserById(Long.parseLong(param.getString("userId")));
            if (ObjectUtil.isNull(user)){
                code = HttpStatus.ERROR;
                msg = "用户信息不存在";
            }else {
                // 状态校验
                GroupPerson check = imGroupPersonService.getOne(
                        new QueryWrapper<GroupPerson>().eq("group_id",
                                param.getString("groupId")).eq("person_id", param.getString("userId")));
                if (ObjectUtil.isNull(check)){
                    // 设置查询参数
                    param.put("value",param.getString("groupId"));
                    // 获取群信息
                    GroupChat info = getOne(getQueryEntity("group_id", param));
                    // 创建加群用户实体类
                    GroupPerson groupPerson = new GroupPerson();
                    groupPerson.setGroupId(param.getString("groupId"));
                    groupPerson.setPersonId(param.getString("userId"));
                    groupPerson.setPersonType(0);
                    groupPerson.setTime(DateUtils.getTime());
                    // 判断加群类型是直接加入还是审核加入
                    groupPerson.setStatus(info.getType()==0?0:1);
                    flag = imGroupPersonService.save(groupPerson);
                }else {
                    if (check.getStatus()==0) {
                        code = HttpStatus.ERROR;
                        msg = "该用户已申请加入群聊,请等待管理员进行审核";
                    }
                    else if (check.getPersonType()==1||check.getPersonType()==2) {
                        code = HttpStatus.ERROR;
                        msg = "该用户已经是群主或者管理员";
                    }
                }
            }
        }catch (Exception e){
            code = HttpStatus.ERROR;
            msg="系统异常,异常信息:"+e.toString();
        }
        result.put("code",code);
        result.put("msg",msg);
        result.put("data",flag);
        return result;
    }

    public JSONObject getResultInfo(List<GroupPerson> all,PageData param,GroupChat e){
        JSONObject info = JSONObject.parseObject(JSON.toJSONString(e));
        info.put("addStatus",all.stream().filter(j -> j.getPersonId().equals(param.getString("userId"))).collect(Collectors.toList()).size()>0?1:0);
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
        groupPerson.setStatus(1);
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
        String remark = param.getString("remark");
        if (!StrUtil.isEmpty(remark)){
            groupChat.setRemark(remark);
        }
        return groupChat;
    }
}
