package com.xinshang.modular.im.server;

import java.io.IOException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.xinshang.modular.im.service.ImServerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhangjiajia
 * @date 2018年11月1日 14:23:20
 * @desc 消息处理服务中心
 */
@ServerEndpoint("/wsServer/{userId}")
@Component
@Slf4j
public class ImServer {

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static Integer onlineCount = 0;

    private static ImServerService imServerService;

    @Autowired void setImServerService(ImServerService imServerService) {
        ImServer.imServerService = imServerService;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session,@PathParam("userId") Integer userId){
        System.out.println(session);
        imServerService.onOpen(userId,session);

        addOnlineCount();
        log.info(userId+"已登录,当前在线总人数为 {}",onlineCount);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        imServerService.onClose(session);
        subOnlineCount();
        log.info("有人退出,当前在线总人数为 {}",onlineCount);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message,Session session) {
        imServerService.onMessage(message,session);
    }


    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }


    /**
     * 实现服务器主动推送
     */
    public void sendMessage(Integer userId, String message) throws IOException {


    }

    public static synchronized void addOnlineCount() {
            onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        onlineCount--;
    }
}