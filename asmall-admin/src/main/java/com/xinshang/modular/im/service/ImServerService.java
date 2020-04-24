package com.xinshang.modular.im.service;

import com.xinshang.core.vo.ResultVO;

import javax.websocket.Session;
import java.util.List;

/**
 * @author zhangjiajia
 * @since 19-7-4
 */
public interface ImServerService {



    ResultVO onOpen(Integer userId, Session session);

    ResultVO onClose(Session session);

    ResultVO onMessage(String message,Session session);

    ResultVO onError(Session session,Throwable error);

    Boolean isUserOnline(Integer userId);

    List<Integer> getOnlineFriend(Integer userId);

    void sendMessage(Integer userId,String message);
}
