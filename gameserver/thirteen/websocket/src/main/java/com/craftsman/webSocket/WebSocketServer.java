package com.craftsman.webSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value="/webSocket")
@Component
public class WebSocketServer {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
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
        try{
            sendMsg("连接成功");
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
}
