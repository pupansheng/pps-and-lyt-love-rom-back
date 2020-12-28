package com.pps.back.frame.pupansheng.custom.im.listener;

import com.alibaba.fastjson.JSON;
import com.pps.websocket.server.chat.ChatFromProtocl;
import com.pps.websocket.server.chat.MsgAction;
import com.pps.websocket.server.event.SocketEnvent;
import com.pps.websocket.server.listener.SocketAutoListenr;
import com.pps.websocket.server.util.SendMsgUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;


import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.pps.websocket.server.chat.MsgAction.other;
import static com.pps.websocket.server.chat.MsgAction.sendToAll;

/**
 * @author
 * @discription;
 * @time 2020/12/17 15:30
 */
public class CustomListener implements SocketAutoListenr {




    @Override
    public void loginSuccessful(SocketEnvent socketEnvent) {

        System.out.println("登录");

    }

    @Override
    public void loginFail(SocketEnvent socketEnvent) {

        System.out.println("登陆失败");

    }

    @Override
    public void loginOut(SocketEnvent socketEnvent) {


        System.out.println("用户退出：");


    }

    @Override
    public void acceptText(ChannelHandlerContext content, ChatFromProtocl chatFromProtocl, Date happenTime) {

        System.out.println("服务器收到信息：");
        System.out.println("消息类型:"+chatFromProtocl.getMsgType());
        System.out.println("消息内容："+chatFromProtocl.getData());
        int action = chatFromProtocl.getAction();
        MsgAction actionByType = MsgAction.getActionByType(action);
        System.out.print("发送者希望动作："+ actionByType);
        switch (actionByType){
            case sendToSingle:
                String to = chatFromProtocl.getTo();
                if(true){



                }else {
                    boolean existsUser=false;
                    //todo 如果用户存在 只是不在线 可以离线保存 上线时再发送
                   if(existsUser){


                    }else {//todo 如果用户不存在



                    }


                }

                break;
            case sendToAll:

                break;
            case other:

                break;
            default:
                SendMsgUtil.sendMsgToClientForErrorInfo(content,"不支持该动作！");
        }
    }

    @Override
    public void acceptBin(ChannelHandlerContext content, CharSequence fileInfo, ByteBuf fileData, Date happenTime) {
        System.out.println("服务器收到二进制消息：文件信息："+fileInfo);
        Map map = JSON.parseObject(String.valueOf(fileInfo), Map.class);


    }

    @Override
    public void other(SocketEnvent socketEnvent) {


    }
}
