/*
 * Copyright (c) ACCA Corp.
 * All Rights Reserved.
 */
package com.pps.back.frame.pupansheng.custom.pachong.myrequest;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.concurrent.SettableListenableFuture;

/**
 * @author pupansheng, 2021/3/29
 * @version OPRA v1.0
 */
public class RequestExecuteHandler extends SimpleChannelInboundHandler<FullHttpResponse> {

    private final SettableListenableFuture<ClientHttpResponse> responseFuture;

    public RequestExecuteHandler(SettableListenableFuture<ClientHttpResponse> responseFuture) {
        this.responseFuture = responseFuture;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, FullHttpResponse response) throws Exception {
        this.responseFuture.set(new MyNetty4ClientHttpResponse(context, response));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
        this.responseFuture.setException(cause);
    }
}
