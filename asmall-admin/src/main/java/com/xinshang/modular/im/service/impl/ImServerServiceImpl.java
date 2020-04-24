package com.xinshang.modular.im.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xinshang.constant.Constants;
import com.xinshang.core.vo.ResultVO;
import com.xinshang.modular.im.dao.GroupUserMapper;
import com.xinshang.modular.im.dao.ImUserMapper;
import com.xinshang.modular.im.dao.MsgRecordMapper;
import com.xinshang.modular.im.model.GroupUser;
import com.xinshang.modular.im.model.ImUser;
import com.xinshang.modular.im.model.MsgRecord;
import com.xinshang.modular.im.dto.ReceiveMsgDTO;
import com.xinshang.modular.im.dto.SendMsgDTO;
import com.xinshang.modular.im.service.ImServerService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangjiajia
 * @since 19-7-4
 */
@Service
@Slf4j
public class ImServerServiceImpl implements ImServerService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ImUserMapper imUserMapper;

    @Autowired
    private MsgRecordMapper msgRecordMapper;

    @Autowired
    private GroupUserMapper groupUserMapper;
    /**
     * 保存所有
     */
    static ConcurrentHashMap<Integer, Session> concurrentHashMap = new ConcurrentHashMap();

    /**
     * 用户建立连接
     * @param userId
     * @param session
     * @return
     */
    @SneakyThrows
    @Override
    public ResultVO onOpen(Integer userId,Session session) {
        synchronized (concurrentHashMap) {
            if (isUserOnline(userId)) {
                sendMessage(userId,ResultVO.of().put("type","systemMsg").put("msg","您的账号已在别处登录").toString());
                //清除session
                if(concurrentHashMap.containsKey(userId)){
                    concurrentHashMap.get(userId).close();
                    concurrentHashMap.remove(userId);
                }
            }
            concurrentHashMap.put(userId, session);
            //设置用户状态为在线
            stringRedisTemplate.opsForValue().setBit(Constants.asmall_SYSTEM_USER_STATE_KEY, userId, true);
        }

        //通知其好友已上线
        sendOnlineOrOffline(userId, Constants.ONLINE);

        //读取该好友的离线消息
        EntityWrapper<MsgRecord> ew = new EntityWrapper();
        ew.eq("to_user",userId).eq("type",1).eq("is_readed",0);
        List<MsgRecord> list = msgRecordMapper.selectList(ew);
        list.forEach(mr->{
            ImUser iu = imUserMapper.selectById(mr.getFormUser());
            //发送到接收者
            SendMsgDTO sm = new SendMsgDTO();
                sm.setAvatar(iu.getAvatar());
                sm.setContent(mr.getMsgContent());
                sm.setFromid(mr.getFormUser());
                sm.setId(mr.getFormUser());
                sm.setMine(mr.getFormUser().equals(userId));
                sm.setTimestamp(mr.getGmtCreate().getTime());
                sm.setType(mr.getType());
                sm.setUsername(iu.getUsername());
            sendMessage(userId,JSONObject.toJSONString(sm));
            mr.setIsReaded(1);
            msgRecordMapper.updateById(mr);
        });
        return null;

    }

    @SneakyThrows
    @Override
    public ResultVO onClose(Session session) {
       Integer userId = getUserIdBySession(session);
           if(userId==null){
               return null;
           }

        synchronized (concurrentHashMap) {
            if (isUserOnline(userId)) {
                //设置用户状态为下线
                stringRedisTemplate.opsForValue().setBit(Constants.asmall_SYSTEM_USER_STATE_KEY, userId, false);
            }

            //通知其好友已下线
            sendOnlineOrOffline(userId, Constants.OFFLINE);

            //清除session
            if(concurrentHashMap.containsKey(userId)){
                concurrentHashMap.remove(userId);
            }
            session.close();
            return null;
        }
    }

    @Override
    public ResultVO onMessage(String message,Session session) {
        JSONObject userJson = JSONObject.parseObject(message);
        ReceiveMsgDTO receiveMsg = JSON.toJavaObject(userJson, ReceiveMsgDTO.class);
        MsgRecord mr = new MsgRecord();
        Integer formId =receiveMsg.getData().getMine().getId();
        Integer toId = receiveMsg.getData().getTo().getId();
        String content = receiveMsg.getData().getMine().getContent();
        mr.setFormUser(formId);
        mr.setToUser(toId);
        mr.setMsgContent(content);
        mr.setType(receiveMsg.getType());
        mr.setMsgJson(message);
        if(Constants.GROUP.equals(receiveMsg.getType())){
            EntityWrapper<GroupUser> ew = new EntityWrapper();
            List<GroupUser> groupUsers = groupUserMapper.selectList(ew.eq("group_id",toId));
            List<Integer> userIds = groupUsers.stream().filter(i->!i.getUserId().equals(formId)).map(GroupUser::getUserId).collect(Collectors.toList());
            SendMsgDTO sm = new SendMsgDTO();
            sm.setAvatar(receiveMsg.getData().getMine().getAvatar());
            sm.setContent(content);
            sm.setId(toId);
            sm.setType(receiveMsg.getType());
            sm.setUsername(receiveMsg.getData().getMine().getUsername());
            sm.setFromid(formId);
            userIds.forEach(id->{
                if(isUserOnline(id)){
                    sm.setMine(formId.equals(id));
                    sendMessage(id,JSONObject.toJSONString(sm));
                }
            });
        }else {
            //离线消息
            if (!isUserOnline(toId)) {
                mr.setIsReaded(0);
            } else {
                ImUser iu = imUserMapper.selectById(formId);
                //发送到接收者
                SendMsgDTO sm = new SendMsgDTO();
                sm.setAvatar(iu.getAvatar());
                sm.setContent(content);
                sm.setId(formId);
                sm.setMine(formId.equals(toId));
                sm.setType(receiveMsg.getType());
                sm.setUsername(iu.getUsername());
                sm.setFromid(formId);
                sendMessage(toId, JSONObject.toJSONString(sm));
            }
        }

        msgRecordMapper.insert(mr);
        return null;
    }

    @Override
    public ResultVO onError(Session session,Throwable error) {
        return null;
    }

    /**
     * 消息发送
     * */
    @Override
    public void sendMessage(Integer userId,String message) {
        try {
            if(concurrentHashMap!=null&&concurrentHashMap.get(userId)!=null){
                concurrentHashMap.get(userId).getBasicRemote().sendText(message);
            }
        } catch (IOException e) {
            log.debug("给{}发送消息失败",userId);
            e.printStackTrace();
        }
    }

    /**
     * 查询redis中用户是否在线
     * @param userId
     * @return
     */
    @Override
    public Boolean isUserOnline(Integer userId) {
        Boolean isOnline = stringRedisTemplate.opsForValue().getBit(Constants.asmall_SYSTEM_USER_STATE_KEY, userId);
        log.debug("{} 是否在线 {}",userId,isOnline);
        return isOnline;
    }

    /**
     * 通知其好友上线或者下线
     * @param userId
     * @param status
     */

    public void sendOnlineOrOffline(Integer userId, String status) {
        //通知好友
        getOnlineFriend(userId).forEach(id-> sendMessage(id,ResultVO.of().put("type",status).put("userId",userId).toString()));
    }

    /**
     * 根据session获取用户id
     * @param session
     * @return
     */
    Integer getUserIdBySession(Session session){
        synchronized (concurrentHashMap) {
            for (Map.Entry<Integer, Session> integerSessionEntry : concurrentHashMap.entrySet()) {
                if (integerSessionEntry.getValue().getId().equals(session.getId())) {
                    return integerSessionEntry.getKey();
                }
            }
           return null;
        }
    }

    /**
     * 获取在线的好友
     * @param userId
     * @return
     */
    @Override
    public List<Integer> getOnlineFriend(Integer userId){
        List<Integer> ids = imUserMapper.findFriendByUserId(userId).stream().filter(id->isUserOnline(id)).collect(Collectors.toList());
        log.debug("{} 的在线好友"+ids,userId);
        return ids;
    }

}
