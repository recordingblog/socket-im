package com.ruoyi.im.server;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.im.ImException;
import com.ruoyi.im.domain.im.ImMessage;
import com.ruoyi.im.type.IMMessageEnum;
import com.ruoyi.im.utils.WebSocketUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/msg/{userId}")
@Component
@Slf4j
@Data
public class WebSocketServer {

    private Session session;

    private String userId;

    // 存储socket链接
    public static ConcurrentHashMap<String, WebSocketServer> webSocketSet = new ConcurrentHashMap<>();

    // 连接socket
    @OnOpen
    public void onOpen(@PathParam("userId") String userId,Session session){
        this.session = session;
        this.userId = userId;
        // 保存链接
        webSocketSet.put(userId, this);
        WebSocketUtils.sendToUserText(userId,getResult(200,"连接成功").toString());
        log.info("【webSocket】:{}连接成功，当前连接人数为:={}", userId, webSocketSet.size());
    }

    // 关闭socket
    @OnClose
    public void onClose() {
        webSocketSet.remove(this.userId);
        log.info("【webSocket】:{}退出成功,当前连接人数为:={}", userId, webSocketSet.size());
    }

    // 收到消息
    @OnMessage
    public void onMessage(String message) {
        try {
            ImMessage imMessage = JSONObject.parseObject(message, ImMessage.class);
            log.info("来自客户端的消息:{},发送人:{}",message,imMessage.getFormId());
            Integer sendType = imMessage.getSendType();
            // 单发
            if (sendType == IMMessageEnum.SEND_TYPE_ONE.getCode()){
                WebSocketUtils.sendToUserText(imMessage.getToId(),message);
            }
            // 群发
            else if (sendType == IMMessageEnum.SEND_TYPE_ALL.getCode()){
                WebSocketUtils.sendToUserAllText(message);
            }else {
                throw new ImException("消息类型异常");
            }
        }catch (Exception e){
            WebSocketUtils.sendToUserText(this.userId,getResult(-1,"请使用json格式的数据发送消息").toString());
        }
    }

    // 异常消息
    @OnError
    public void onError(Throwable error){
        log.info("【webSocket】:发生异常,异常信息{}",error.getMessage());
        error.printStackTrace();
    }


    public AjaxResult getResult(Integer code,String msg){
        return new AjaxResult(code,msg);
    }

}
