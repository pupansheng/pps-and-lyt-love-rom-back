/*
 * Copyright (c) ACCA Corp.
 * All Rights Reserved.
 */
package com.pps.back.frame.pupansheng.core.http.myrequest;

import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.AbstractClientHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author pupansheng, 2021/3/29
 * @version OPRA v1.0
 */
public class MyNetty4ClientHttpResponse extends AbstractClientHttpResponse {

    private final ChannelHandlerContext context;

    private final FullHttpResponse nettyResponse;

    private final ByteBufInputStream body;

    @Nullable
    private volatile HttpHeaders headers;


    public MyNetty4ClientHttpResponse(ChannelHandlerContext context, FullHttpResponse nettyResponse) {
        Assert.notNull(context, "ChannelHandlerContext must not be null");
        Assert.notNull(nettyResponse, "FullHttpResponse must not be null");
        this.context = context;
        this.nettyResponse = nettyResponse;
        this.body = new ByteBufInputStream(this.nettyResponse.content());
        this.nettyResponse.retain(2);
    }


    @Override
    public int getRawStatusCode() throws IOException {
        return this.nettyResponse.getStatus().code();
    }

    @Override
    public String getStatusText() throws IOException {
        return this.nettyResponse.getStatus().reasonPhrase();
    }

    @Override
    public HttpHeaders getHeaders() {
        HttpHeaders headers = this.headers;
        if (headers == null) {
            headers = new HttpHeaders();
            for (Map.Entry<String, String> entry : this.nettyResponse.headers()) {
                headers.add(entry.getKey(), entry.getValue());
            }
            this.headers = headers;
        }
        return headers;
    }

    @Override
    public InputStream getBody() throws IOException {
        return this.body;
    }

    @Override
    public void close() {
        this.nettyResponse.release();
        this.context.close();
    }

}
