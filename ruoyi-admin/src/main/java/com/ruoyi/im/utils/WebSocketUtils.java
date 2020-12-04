package com.ruoyi.im.utils;

import com.ruoyi.common.exception.im.ImException;
import com.ruoyi.im.server.WebSocketServer;
import org.springframework.util.ObjectUtils;

import javax.websocket.Session;
import java.util.concurrent.locks.ReentrantLock;

import static com.ruoyi.im.server.WebSocketServer.webSocketSet;

public class WebSocketUtils {

    /**
     * 单发消息
     * @param userId 用户id
     * @param message 用户消息
     */
    public static void sendToUserText(String userId, String message) {
        ReentrantLock takeLock = new ReentrantLock();
        takeLock.lock();
        try {
            if (isUserOnLine(userId)) {
                webSocketSet.get(userId).getSession().getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            throw new ImException("【WebSocket】：发送消息失败,异常信息:" + e.toString());
        } finally {
            takeLock.unlock();
        }
    }

    /**
     * 群发消息
     * @param message 消息体
     */
    public static void sendToUserAllText(String message) {
        ReentrantLock takeLock = new ReentrantLock();
        takeLock.lock();
        try {
            webSocketSet.forEach((k,v)->{
                Session session = v.getSession();
                session.getAsyncRemote().sendText(message);
            });
        } catch (Exception e) {
            throw new ImException("【WebSocket】：发送消息失败,异常信息:" + e.toString());
        } finally {
            takeLock.unlock();
        }
    }

    /**
     * 用户是否在线
     *
     * @param userId
     * @return
     */
    public static boolean isUserOnLine(String userId) {
        ReentrantLock takeLock = new ReentrantLock();
        takeLock.lock();
        try {
            WebSocketServer webSocketServer = webSocketSet.get(userId);
            if (!ObjectUtils.isEmpty(webSocketServer)) {
                return true;
            }
        } finally {
            takeLock.unlock();
        }
        return false;
    }
}
