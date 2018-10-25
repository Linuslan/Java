package com.craftsman.webSocket;

import com.alibaba.fastjson.JSONObject;
import com.craftsman.common.constant.ClassConstants;
import com.craftsman.common.constant.enums.CommandType;
import com.craftsman.common.constant.enums.ErrorCode;
import com.craftsman.common.util.BeanUtil;
import com.craftsman.common.util.ExceptionUtil;
import com.craftsman.common.util.SerialNoUtil;
import com.craftsman.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value="/webSocket")
@Component
public class WebSocketServer {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();
    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<String, WebSocketServer>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    private String socketId;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 连接建立成功调用的方法
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
        addOnlineCount();
        logger.info("新加入连接，当前连接数为："+getOnlineCount());
        String socketId = SerialNoUtil.getRandomString(16);
        this.socketId = socketId;
        Map<String, Object> map = new HashMap<String, Object> ();
        map.put(ClassConstants.SOCKET_ID.getName(), socketId);
        webSocketMap.put(socketId, this);
        logger.info("生成的socketId为："+socketId);
        try{
            this.send(true, ClassConstants.COMMAND_OPEN.getName(), map, null);
        } catch(Exception ex) {
            logger.error("发送异常", ex);
        }
    }

    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        minusOnlineCount();
        logger.info("连接关闭");
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("接收到客户端的消息："+message);
        boolean success = false;
        Map<String, Object> dataMap = null;
        String command = null;
        List<WebSocketServer> recipients = null;
        try {
            if(StringUtils.isEmpty(message)) {
                ExceptionUtil.throwExceptionJsonByError(ErrorCode.MESSAGE_IS_EMPTY);
            }
            JSONObject json = JSONObject.parseObject(message);
            String cmd = json.getString(ClassConstants.COMMAND.getName());
            if(StringUtils.isEmpty(cmd)) {
                ExceptionUtil.throwExceptionJsonByError(ErrorCode.COMMAND_IS_EMPTY);
            }
            command = cmd;
            Map<String, Object> resultMap = (Map<String, Object>) BeanUtil.invokeMethod(this, cmd, new Class[]{JSONObject.class}, new Object[]{json});
            if(null != resultMap) {
                if(null != resultMap.get(ClassConstants.RECIPIENTS.getName())) {
                    recipients = (List<WebSocketServer>) resultMap.get(ClassConstants.RECIPIENTS.getName());
                }
                if(null != resultMap.get(ClassConstants.RETURN_DATA.getName())) {
                    dataMap = (Map<String, Object>) resultMap.get(ClassConstants.RETURN_DATA.getName());
                }
            } else {
                ExceptionUtil.throwExceptionJsonByError(ErrorCode.PROCESS_ERROR_EMPTY_RESULT);
            }
            success = true;
        } catch(Exception ex) {
            String exMsg = ex.getMessage();
            JSONObject errorJson = null;
            try {
                errorJson = JSONObject.parseObject(exMsg);
            } catch(Exception e) {
                exMsg = ExceptionUtil.getJSONByError(ErrorCode.UNKNOWN_ERROR);
                errorJson = JSONObject.parseObject(exMsg);
            }
            dataMap = errorJson.toJavaObject(HashMap.class);
        }
        if(null == dataMap) {
            dataMap = new HashMap<String, Object>();
        }
        this.send(success, command, dataMap, recipients);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.error("连接异常", throwable);
    }

    public void sendMsg(String message) throws Exception {
        this.session.getBasicRemote().sendText(message);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void minusOnlineCount() {
        WebSocketServer.onlineCount--;
    }

    public void send(boolean success, String cmd, Map<String, Object> dataMap, List<WebSocketServer> recipients) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        if(null == dataMap) {
            dataMap = new HashMap<String, Object>();
        }
        jsonMap.put(ClassConstants.RETURN_FLAG.getName(), success);
        jsonMap.put(ClassConstants.RETURN_DATA.getName(), dataMap);
        if(!StringUtils.isEmpty(cmd)) {
            jsonMap.put(ClassConstants.COMMAND.getName(), cmd);
        }
        String json = JSONObject.toJSONString(jsonMap);
        try {
            if(null != recipients && recipients.size() > 0) {
                for(WebSocketServer socketServer: recipients) {
                    socketServer.sendMsg(json);
                }
            }
            this.sendMsg(json);
        } catch(Exception ex) {
            logger.error("发送消息异常", ex);
        }
    }
}
