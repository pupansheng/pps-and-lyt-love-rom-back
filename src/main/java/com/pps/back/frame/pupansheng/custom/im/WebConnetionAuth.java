package com.pps.back.frame.pupansheng.custom.im;

import com.pps.websocket.server.hander.http.WebSocketConnetion;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author
 * @discription;定义是否允许连接socket 的自定义验证
 * @time 2020/12/24 11:32
 */
@Component
public class WebConnetionAuth extends WebSocketConnetion {

    public WebConnetionAuth() {
        super("/pps");
    }

    public WebConnetionAuth(String wsUrl) {
        super(wsUrl);
    }

    @Override
    public Boolean webSocketCanConnetion(Map<String, Object> requestParams, ChannelHandlerContext ctx) {


       return  true;

    }

    @Override
    public void customOpreate(ChannelHandlerContext ctx) {

    }
}
